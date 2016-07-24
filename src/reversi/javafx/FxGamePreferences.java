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

package reversi.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * This class represents the GamePreference window.
 * Here all settings of the program can be changed.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 23. July 2016
 */
class FxGamePreferences {
    private FxGamePreferencesController controller;

    FxGamePreferences() {
        //create new application modal window
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        //load the layout from the fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(FxGamePreferences.class.getResource("/layout/GamePreferences.fxml"));

        //get the resource bundle that contains the internationalized strings
        ResourceBundle resourceBundle = ResourceBundle.getBundle("strings/Values");
        fxmlLoader.setResources(resourceBundle);

        Parent parent;
        try {
            //load the parent from the xml file
            parent = fxmlLoader.load();

            //get the controller of the view
            controller = fxmlLoader.getController();
            //set the window
            controller.initWindow(window);

            //create a new scene that contains the parent layout as root
            Scene scene = new Scene(parent);
            //add the scene to the window
            window.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //make the window not resizable
        window.setResizable(false);

        window.setTitle(resourceBundle.getString("ui.preferences.title"));

        //wait until the window is closed
        window.showAndWait();
    }

    /**
     * This method is used to check if an configuration was changed by this application dialog.
     *
     * @return true if config was changed - false if not
     */
    boolean hasConfigurationChanged() {
        return controller.hasConfigurationChanged();
    }
}