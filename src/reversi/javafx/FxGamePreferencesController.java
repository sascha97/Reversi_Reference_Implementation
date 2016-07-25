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

import reversi.actor.ComputerActors;
import reversi.evaluation.Evaluations;
import reversi.game.ReversiGameConfiguration;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * This class handles all the user input of the GamePreferences.fxml view.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. July 2016
 */
public class FxGamePreferencesController implements Initializable {
    /*
     * Tab Board
     */
    //the slider for the board size settings
    @FXML
    private Slider sliderBoardSize;

    //the group which is used to distinguish if the coordinates of the board should be shown
    @FXML
    private ToggleGroup toggleGroupShowLegalMoves;

    //the group which is used to distinguish if the animations of the board should be shown
    @FXML
    private ToggleGroup toggleGroupShowAnimations;
    /*
     * Tab Player
     */
    //the group which is used to distinguish which player is the human player
    @FXML
    private ToggleGroup toggleGroupHumanPlayer;

    //the preview circle for the black player
    @FXML
    private Circle circlePlayerBlack;

    //the preview circle for the white player
    @FXML
    private Circle circlePlayerWhite;

    //the color picker that represents the color picked for the black player
    @FXML
    private ColorPicker colorPickerPlayerBlack;

    //the color picker that represents the color picked for the white player
    @FXML
    private ColorPicker colorPickerPlayerWhite;

    /*
     * Tab Computer
     */
    //the slider for the algorithm search depth settings
    @FXML
    private Slider sliderAlgorithmSearchDepth;

    //the choice box which allows it to change the actor
    @FXML
    private ChoiceBox<ComputerActors> choiceBoxSelectComputerActor;

    //the choice box which allows it to change the actors evaluation method
    @FXML
    private ChoiceBox<Evaluations> choiceBoxSelectEvaluation;

    //The local map containing all configurations from the configuration
    private Map<String, String> preferenceMap;

    //The game configuration
    private ReversiGameConfiguration configuration = ReversiGameConfiguration.getInstance();

    //boolean if config has changed
    private boolean hasConfigurationChanged = false;

    //the window on which the dialog is displayed
    private Stage window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //create local copy of the preferences
        initializePreferenceMap();

        //Bind the components to the config and to other properties
        initializeTabBoard();
        initializeTabPlayer();
        initializeTabComputer();
    }

    /**
     * This method is called when ever the button cancel config is clicked
     *
     * @param event The event.
     */
    @FXML
    private void buttonCancelClicked(ActionEvent event) {
        //close the modal view
        window.close();
    }

    /**
     * This method is called when ever the button save config is clicked
     *
     * @param event The event.
     */
    @FXML
    private void buttonSaveConfigurationClicked(ActionEvent event) {
        saveChangesToReversiGameConfiguration();

        //close the modal view
        window.close();
    }

    /**
     * Method is used to set the right window for the controller
     *
     * @param window The window in which the dialog should be displayed
     */
    public void initWindow(Stage window) {
        this.window = window;
    }

    /**
     * This method is used that it can be distinguished that a setting was changed
     *
     * @return true if the configuration has changed - false if the configuration has not changed
     */
    public boolean hasConfigurationChanged() {
        return hasConfigurationChanged;
    }

    /**
     * This method creates the local copy of the config entries that can be changed by this ui
     */
    private void initializePreferenceMap() {
        preferenceMap = new HashMap<>();

        //Load the values from the reversi game configuration
        String algorithmDepth = configuration.getProperty(ReversiGameConfiguration.ALGORITHM_SEARCH_DEPTH, "5");
        String boardSize = configuration.getProperty(ReversiGameConfiguration.BOARD_SIZE, "8");
        String computerActor = configuration.getProperty(ReversiGameConfiguration.COMPUTER_ALGORITHM, "MINI_MAX");
        String computerEvaluation = configuration.getProperty(
                ReversiGameConfiguration.COMPUTER_EVALUATION, "MIXED_EVALUATION");
        String humanPlayerColor = configuration.getProperty(ReversiGameConfiguration.HUMAN_PLAYER_COLOR, "black");
        String blackPlayerColor = configuration.getProperty(ReversiGameConfiguration.PLAYER_BLACK_COLOR, "0xFF0000");
        String whitePlayerColor = configuration.getProperty(ReversiGameConfiguration.PLAYER_WHITE_COLOR, "0xFFFF00");
        String showLegalMoves = configuration.getProperty(
                ReversiGameConfiguration.USER_INTERFACE_SHOW_LEGAL_MOVES, "true");
        String showAnimation = configuration.getProperty(
                ReversiGameConfiguration.USER_INTERFACE_SHOW_ANIMATIONS, "true");

        preferenceMap.put(ReversiGameConfiguration.ALGORITHM_SEARCH_DEPTH, algorithmDepth);
        preferenceMap.put(ReversiGameConfiguration.BOARD_SIZE, boardSize);
        preferenceMap.put(ReversiGameConfiguration.COMPUTER_ALGORITHM, computerActor);
        preferenceMap.put(ReversiGameConfiguration.COMPUTER_EVALUATION, computerEvaluation);
        preferenceMap.put(ReversiGameConfiguration.HUMAN_PLAYER_COLOR, humanPlayerColor);
        preferenceMap.put(ReversiGameConfiguration.PLAYER_BLACK_COLOR, blackPlayerColor);
        preferenceMap.put(ReversiGameConfiguration.PLAYER_WHITE_COLOR, whitePlayerColor);
        preferenceMap.put(ReversiGameConfiguration.USER_INTERFACE_SHOW_LEGAL_MOVES, showLegalMoves);
        preferenceMap.put(ReversiGameConfiguration.USER_INTERFACE_SHOW_ANIMATIONS, showAnimation);
    }

    private void initializeTabBoard() {
        //set up the algorithm search depth slider
        int boardSize = Integer.parseInt(preferenceMap.get(ReversiGameConfiguration.BOARD_SIZE));
        sliderBoardSize.setValue(boardSize);

        //refresh the local preference copy
        sliderBoardSize.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int currentValue = newValue.intValue();

                //Make sure that only valid board sizes will be set
                if (currentValue % 2 == 0) {
                    updateConfiguration(ReversiGameConfiguration.BOARD_SIZE, Integer.toString(currentValue));
                }
            }
        });

        //toggle the right item in show labels
        String showLabels = preferenceMap.get(ReversiGameConfiguration.USER_INTERFACE_SHOW_LEGAL_MOVES);
        for (Toggle toggle : toggleGroupShowLegalMoves.getToggles()) {
            if (toggle.getUserData().equals(showLabels)) {
                toggle.setSelected(true);
            }
        }

        //update the local preference copy if the value changes
        toggleGroupShowLegalMoves.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                String showCoordinates = newValue.getUserData().toString();

                updateConfiguration(ReversiGameConfiguration.USER_INTERFACE_SHOW_LEGAL_MOVES, showCoordinates);
            }
        });

        //toggle the right item in show animations
        String showAnimations = preferenceMap.get(ReversiGameConfiguration.USER_INTERFACE_SHOW_ANIMATIONS);
        for (Toggle toggle : toggleGroupShowAnimations.getToggles()) {
            if (toggle.getUserData().equals(showAnimations)) {
                toggle.setSelected(true);
            }
        }

        //update the local preference copy if the value changes
        toggleGroupShowAnimations.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                String showAnimations = newValue.getUserData().toString();

                updateConfiguration(ReversiGameConfiguration.USER_INTERFACE_SHOW_ANIMATIONS, showAnimations);
            }
        });
    }

    private void initializeTabPlayer() {
        //set up the color pickers
        Color blackPlayerColor = Color.valueOf(preferenceMap.get(ReversiGameConfiguration.PLAYER_BLACK_COLOR));
        colorPickerPlayerBlack.setValue(blackPlayerColor);
        Color whitePlayerColor = Color.valueOf(preferenceMap.get(ReversiGameConfiguration.PLAYER_WHITE_COLOR));
        colorPickerPlayerWhite.setValue(whitePlayerColor);

        //initialize the binding of the properties so that a changed color is refreshed automatically
        circlePlayerBlack.fillProperty().bind(colorPickerPlayerBlack.valueProperty());
        circlePlayerWhite.fillProperty().bind(colorPickerPlayerWhite.valueProperty());

        //update the local config if black color changes
        colorPickerPlayerBlack.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                updateConfiguration(ReversiGameConfiguration.PLAYER_BLACK_COLOR, getColorForPreferences(newValue));
            }
        });

        //update the local config if white color changes
        colorPickerPlayerWhite.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                updateConfiguration(ReversiGameConfiguration.PLAYER_WHITE_COLOR, getColorForPreferences(newValue));
            }
        });

        //Toggle the right item
        String humanPlayerColor = preferenceMap.get(ReversiGameConfiguration.HUMAN_PLAYER_COLOR);
        for (Toggle toggle : toggleGroupHumanPlayer.getToggles()) {
            if (toggle.getUserData().equals(humanPlayerColor)) {
                toggle.setSelected(true);
            }
        }

        //refresh the local preference copy
        toggleGroupHumanPlayer.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                String colorHumanPlayer = newValue.getUserData().toString();

                updateConfiguration(ReversiGameConfiguration.HUMAN_PLAYER_COLOR, colorHumanPlayer);
            }
        });
    }

    private void initializeTabComputer() {
        //set up the algorithm search depth slider
        int algorithmSearchDepth = Integer.parseInt(preferenceMap.get(ReversiGameConfiguration.ALGORITHM_SEARCH_DEPTH));
        sliderAlgorithmSearchDepth.setValue(algorithmSearchDepth);

        //refresh the local preference copy
        sliderAlgorithmSearchDepth.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int currentValue = newValue.intValue();

                updateConfiguration(ReversiGameConfiguration.ALGORITHM_SEARCH_DEPTH, Integer.toString(currentValue));
            }
        });

        //add all items to the choice box
        choiceBoxSelectComputerActor.getItems().setAll(ComputerActors.values());

        String algorithmName = preferenceMap.get(ReversiGameConfiguration.COMPUTER_ALGORITHM);

        for (ComputerActors computerActor : choiceBoxSelectComputerActor.getItems()) {
            if (computerActor.name().equals(algorithmName)) {
                choiceBoxSelectComputerActor.setValue(computerActor);
            }
        }

        //refresh the settings if actor is changed
        choiceBoxSelectComputerActor.valueProperty().addListener(new ChangeListener<ComputerActors>() {
            @Override
            public void changed(ObservableValue<? extends ComputerActors> observable, ComputerActors oldValue,
                                ComputerActors newValue) {
                String computerActor = newValue.name();

                updateConfiguration(ReversiGameConfiguration.COMPUTER_ALGORITHM, computerActor);
            }
        });

        //add all items to the choice box
        choiceBoxSelectEvaluation.getItems().setAll(Evaluations.values());

        String evaluationName = preferenceMap.get(ReversiGameConfiguration.COMPUTER_EVALUATION);

        for (Evaluations evaluation : choiceBoxSelectEvaluation.getItems()) {
            if (evaluation.name().equals(evaluationName)) {
                choiceBoxSelectEvaluation.setValue(evaluation);
            }
        }

        //refresh the settings if actor is changed
        choiceBoxSelectEvaluation.valueProperty().addListener(new ChangeListener<Evaluations>() {
            @Override
            public void changed(ObservableValue<? extends Evaluations> observable, Evaluations oldValue, Evaluations newValue) {
                String evaluationName = newValue.name();

                updateConfiguration(ReversiGameConfiguration.COMPUTER_EVALUATION, evaluationName);
            }
        });
    }

    /**
     * This method is used for converting an javafx.scene.paint.Color to the corresponding hex value used in the
     * configuration
     *
     * @param color The color that should be converted
     * @return The hex value of that color
     */
    private String getColorForPreferences(Color color) {
        //get the hex color value with the opacity value
        String stringValue = color.toString();
        //get the length of the hex color code
        int length = stringValue.length();

        //remove the opacity value (last two signs of hex code)
        return stringValue.substring(0, length - 2);
    }

    /**
     * This method is used so that the config will only be updated in one method.
     *
     * @param key      The key of the config which should be updated
     * @param newValue The new value that should be stored for that key
     */
    private void updateConfiguration(String key, String newValue) {
        preferenceMap.put(key, newValue);
    }

    /**
     * This method is used to store all the configurations in the reversi game config file
     */
    private void saveChangesToReversiGameConfiguration() {
        hasConfigurationChanged = true;

        //get all the set keys of the preference map
        Set<String> keys = preferenceMap.keySet();

        //for each key set set the value to the reversi game config
        for (String key : keys) {
            configuration.setProperty(key, preferenceMap.get(key));
        }
    }
}
