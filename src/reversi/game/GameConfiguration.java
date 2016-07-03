/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * This class handles the configuration writing stuff.
 * <p>
 * ALL IMPLEMENTING CLASSES HAVE TO FOLLOW THE SINGLETON DESIGN PATTERN BECAUSE THERE SHOULD ONLY BE ONE INSTANCE
 * OF SUBCLASSES IN THE ENTIRE PROGRAM.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 21. May 2016
 */
public abstract class GameConfiguration {
    //The properties of the game
    private Properties configurationProperties;

    //The name of the config file
    private final String CONFIGURATION_FILE_NAME;

    /**
     * Constructor for the GameConfiguration
     *
     * @param configurationFileName The name of the configuration file.
     */
    protected GameConfiguration(String configurationFileName) {
        //Create new properties.
        configurationProperties = new Properties();

        //set the name of the configuration file
        this.CONFIGURATION_FILE_NAME = configurationFileName;

        //load all the properties from the config file to the properties
        loadPropertiesFromConfigurationFile();
    }

    /**
     * This method is responsible for loading the properties from the file to store them in the properties object.
     */
    private void loadPropertiesFromConfigurationFile() {
        //File where the configuration is stored
        File configurationFile = new File(CONFIGURATION_FILE_NAME);

        //Checks if file exists otherwise file will be created
        if (configurationFile.exists()) {
            //Try to read the config from the file by openeing a FileInputStream
            try {
                InputStream configurationFileInputStream = new FileInputStream(configurationFile);

                //Load the configuration from the file into the properties object.
                configurationProperties.load(configurationFileInputStream);
                //Close all streams so that no resource leak occurs.
                configurationFileInputStream.close();
            } catch (FileNotFoundException fnfe) {
                //Print error to console if file is not found
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                //Print error to console if another InputOutputException occurs.
                ioe.printStackTrace();
            }
        } else {
            //Create the config file
            storePropertiesToConfigurationFile();
        }
    }

    /**
     * This method is responsible for storing the configuration to the config file.
     */
    private void storePropertiesToConfigurationFile() {
        //File where the configuration should be stroed
        File configurationFile = new File(CONFIGURATION_FILE_NAME);

        try {
            //If file does not exist create new file
            if (!configurationFile.exists()) {
                configurationFile.createNewFile();
            }

            //Create an FileOutputStream so that the configuration can be written to the file.
            OutputStream configurationFileOutputStream = new FileOutputStream(configurationFile);

            //Write the config to the file with a comment.
            configurationProperties.store(configurationFileOutputStream, "GameConfiguration");
            //Close all streams so that no resource leak occurs.
            configurationFileOutputStream.close();
        } catch (FileNotFoundException fnfe) {
            //Print error to console if file is not found
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            //Print error to the console if another InputOutputException occurs
            ioe.printStackTrace();
        }
    }

    /**
     * This method is responsible for loading the value from the properties if there is one, otherwise the default value
     * will be stored and returned.
     *
     * @param key          The name of the value that should be returned.
     * @param defaultValue The value that should be returned if the value does not exist.
     * @return The value of the key stored in the properties.
     */
    public final String getProperty(String key, String defaultValue) {
        //If properteis does not have any value associated with the key store the key and value in the properties
        if (!configurationProperties.containsKey(key)) {
            setProperty(key, defaultValue);
        }

        //Return the (now or previously) stored value
        return configurationProperties.getProperty(key, defaultValue);
    }

    /**
     * This method will add/chagne the value at the given key.
     *
     * @param key The name of the value that shozld be stored.
     * @param newValue The new value that should be stored in the properties file.
     */
    public final void setProperty(String key, String newValue) {
        //Set the value in the properties
        configurationProperties.setProperty(key, newValue);
        //Store the changed properties in the configuration file.
        storePropertiesToConfigurationFile();
    }
}
