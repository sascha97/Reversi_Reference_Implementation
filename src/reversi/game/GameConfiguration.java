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
    private Properties configurationProperties;

    private final String CONFIGURATION_FILE_NAME;

    protected GameConfiguration(String configurationFileName) {
        configurationProperties = new Properties();

        this.CONFIGURATION_FILE_NAME = configurationFileName;

        loadPropertiesFromConfigurationFile();
    }

    private void loadPropertiesFromConfigurationFile() {
        File configurationFile = new File(CONFIGURATION_FILE_NAME);

        if (configurationFile.exists()) {
            try {
                InputStream configurationFileInputStream = new FileInputStream(configurationFile);

                configurationProperties.load(configurationFileInputStream);
                configurationFileInputStream.close();
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            storePropertiesToConfigurationFile();
        }
    }

    private void storePropertiesToConfigurationFile() {
        File configurationFile = new File(CONFIGURATION_FILE_NAME);

        try {
            if (!configurationFile.exists()) {
                configurationFile.createNewFile();
            }

            OutputStream configurationFileOutputStream = new FileOutputStream(configurationFile);

            configurationProperties.store(configurationFileOutputStream, "GameConfiguration");
            configurationFileOutputStream.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public final String getProperty(String key, String defaultValue) {
        if (!configurationProperties.containsKey(key)) {
            setProperty(key, defaultValue);
        }

        return configurationProperties.getProperty(key, defaultValue);
    }

    public final void setProperty(String key, String newValue) {
        configurationProperties.setProperty(key, newValue);
        storePropertiesToConfigurationFile();
    }
}
