/*
 * Copyright (c) 2016. Sascha Lutzenberger. All rights reserved.
 *
 * This file is part of the project "Reversi_Reference_Implementation"
 *
 * Redistribution and use in source and binary forms, without modification,
 * are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * - The author of this source code has given you the permission to use this
 *   source code.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * - The code is not used in commercial projects, except you got the permission
 *   for using the code in any commercial projects from the author.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
    private final Properties configurationProperties;

    //The name of the config file
    private final String CONFIGURATION_FILE_NAME;

    /**
     * Constructor for the GameConfiguration
     *
     * @param configurationFileName The name of the configuration file.
     */
    GameConfiguration(String configurationFileName) {
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
            //Try to read the config from the file by opening a FileInputStream
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
        //File where the configuration should be stored
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
        //If properties does not have any value associated with the key store the key and value in the properties
        if (!configurationProperties.containsKey(key)) {
            setProperty(key, defaultValue);
        }

        //Return the (now or previously) stored value
        return configurationProperties.getProperty(key, defaultValue);
    }

    /**
     * This method will add/change the value at the given key.
     *
     * @param key The name of the value that should be stored.
     * @param newValue The new value that should be stored in the properties file.
     */
    public final void setProperty(String key, String newValue) {
        //Set the value in the properties
        configurationProperties.setProperty(key, newValue);
        //Store the changed properties in the configuration file.
        storePropertiesToConfigurationFile();
    }
}
