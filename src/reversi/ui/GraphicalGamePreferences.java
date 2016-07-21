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
 *   for using the code in any commerical projects from the author.
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
package reversi.ui;

import reversi.game.ReversiGameConfiguration;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * This class is used for chaning the ReversiGameConfiguration in the User Interface
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 06. July 2016
 */
public class GraphicalGamePreferences {
    //the configuration of the game
    private final ReversiGameConfiguration configuration = ReversiGameConfiguration.getInstance();

    //The resource bundle containing all the strings
    private final ResourceBundle RES = ResourceBundle.getBundle("strings/Values");

    //the frame containing all the preferences
    private final JDialog preferencesDialog;

    //the dimension field
    private final Dimension DIMENSION = new Dimension(60, 60);

    //The JPanels of the Preferences Panel
    //settings concerning the board and its size
    private JPanel panelBoardSettings;
    //settings concerning the search depth of the algorithm
    private JPanel panelComputerPlayerSettings;
    //settings concerning the color of the player
    private JPanel panelPlayerSettings;
    //settings concerning whether the human player is black or white
    private JPanel panelHumanPlayerSettings;
    //the panel containing the save configuration and cancel button
    private JPanel panelStoreCurrentConfiguration;

    //The icons that are drawn on the configuration ui
    private Icon ICON_PLAYER_WHITE;
    private Icon ICON_PLAYER_BLACK;

    //boolean flag used to check if the configuration has changed.
    private boolean hasConfigurationChanged = false;

    //the map containing all preferences of this preference dialog
    private Map<String, String> preferenceMap;

    public GraphicalGamePreferences(JFrame parent) {
        //Load all the values from the configuration to the map containing all preferences
        initializePreferencesMap();

        //set the player icons
        refreshPlayerIcons();

        //initialize the panel containing the board settings
        initializeBoardSettings();
        //initialize the panel containing the computer player settings
        initializeComputerPlayerSettings();
        //initialize the panel containing the human player settings
        initializeHumanPlayerSettings();
        //initialize the panel containing the player color settings
        initializePlayerSettings();
        //initialize the panel containing the save button for the configuration
        initializeSaveChangesPanel();

        //Set the title of the settings frame
        preferencesDialog = new JDialog(parent, RES.getString("ui.preferences.title"), true);
        //dispose window on close
        preferencesDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //Create a root pane
        JPanel rootPane = new JPanel();
        //Set up every panel in a single row
        rootPane.setLayout(new BoxLayout(rootPane, BoxLayout.Y_AXIS));

        //add the panels to the root panel
        rootPane.add(panelBoardSettings);
        rootPane.add(panelComputerPlayerSettings);
        rootPane.add(panelPlayerSettings);
        rootPane.add(panelHumanPlayerSettings);
        rootPane.add(panelStoreCurrentConfiguration);

        //set the content frame to the window
        preferencesDialog.setContentPane(rootPane);

        //set preference dialog not resizable
        preferencesDialog.setResizable(false);
        //pack the window and make it visible
        preferencesDialog.pack();
        preferencesDialog.setLocationRelativeTo(parent);
        preferencesDialog.setVisible(true);
    }

    /**
     * This method is used to check if the configuration has changed.
     *
     * @return true if the configuration has changed - false if the configuration has not changed
     */
    public boolean hasConfigurationChanged() {
        return hasConfigurationChanged;
    }

    /**
     * This method is used to initialize the preferences map that holds all the preferences
     * that can be set in this window
     */
    private void initializePreferencesMap() {
        //create a new hash map for the preferences
        preferenceMap = new HashMap<>();

        //Load all the used values from the configuration and store them in the preference map
        String blackPlayerColor = getValueFromReversiGameConfiguration(
                ReversiGameConfiguration.PLAYER_BLACK_COLOR, "0xFF0000");
        String whitePlayerColor = getValueFromReversiGameConfiguration(
                ReversiGameConfiguration.PLAYER_WHITE_COLOR, "0xFFFF00");
        String boardSize = getValueFromReversiGameConfiguration(
                ReversiGameConfiguration.BOARD_SIZE, "8");
        String algorithmSearchDepth = getValueFromReversiGameConfiguration(
                ReversiGameConfiguration.ALGORITHM_SEARCH_DEPTH, "5");
        String humanPlayerColor = getValueFromReversiGameConfiguration(
                ReversiGameConfiguration.HUMAN_PLAYER_COLOR, "black");

        preferenceMap.put(ReversiGameConfiguration.PLAYER_BLACK_COLOR, blackPlayerColor);
        preferenceMap.put(ReversiGameConfiguration.PLAYER_WHITE_COLOR, whitePlayerColor);
        preferenceMap.put(ReversiGameConfiguration.BOARD_SIZE, boardSize);
        preferenceMap.put(ReversiGameConfiguration.ALGORITHM_SEARCH_DEPTH, algorithmSearchDepth);
        preferenceMap.put(ReversiGameConfiguration.HUMAN_PLAYER_COLOR, humanPlayerColor);
    }

    /**
     * This method is used to initialize the panel containing the board settings
     */
    private void initializeBoardSettings() {
        //create a new board setting panel with a grid layout
        panelBoardSettings = new JPanel(new GridLayout(1, 1, 10, 10));
        //set the boarder
        panelBoardSettings.setBorder(BorderFactory.createTitledBorder(RES.getString("ui.preferences.title.board")));

        //Load the current board size from the configuration
        String boardSize = getValueFromReversiGameConfiguration(ReversiGameConfiguration.BOARD_SIZE, "8");
        int currentValue = Integer.parseInt(boardSize);

        //The values of the slider
        int minValue = 6;
        int maxValue = 12;
        //Set up a new JSlider
        final JSlider sliderBoardSize = new JSlider(minValue, maxValue, currentValue);
        //Set a tick at every whole number
        sliderBoardSize.setMajorTickSpacing(2);
        //paint the ticks and labels
        sliderBoardSize.setPaintTicks(true);
        sliderBoardSize.setPaintLabels(true);
        //allow that only values at the ticks can be selected
        sliderBoardSize.setSnapToTicks(true);
        //add the slider to the panel
        panelBoardSettings.add(sliderBoardSize);

        //if the slider changes refresh the map where everything is stored
        sliderBoardSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //The slider where the current value is stored
                JSlider slider = (JSlider) e.getSource();
                //get the current value as string
                String currentValue = Integer.toString(slider.getValue());
                //change the corresponding map value
                preferenceMap.put(ReversiGameConfiguration.BOARD_SIZE, currentValue);
            }
        });
    }

    /**
     * This method is used to initialize the panel containing the computer player settings
     */
    private void initializeComputerPlayerSettings() {
        //create a new computer player setting panel with a grid layout
        panelComputerPlayerSettings = new JPanel(new GridLayout(1, 1, 10, 10));
        //set the boarder
        panelComputerPlayerSettings.setBorder(BorderFactory.createTitledBorder(
                RES.getString("ui.preferences.title.algorithm")));

        //load the algorithm search depth from the configuration
        String algorithmSearchDepth = getValueFromReversiGameConfiguration(
                ReversiGameConfiguration.ALGORITHM_SEARCH_DEPTH, "5");
        int value = Integer.parseInt(algorithmSearchDepth);

        //the values of the slider
        int minValue = 2;
        int maxValue = 16;
        //Set up a new JSlider
        final JSlider sliderComputerPlayer = new JSlider(minValue, maxValue, value);
        //set a small tick at every odd number a bigger tick at every even number
        sliderComputerPlayer.setMinorTickSpacing(1);
        sliderComputerPlayer.setMajorTickSpacing(2);
        //paint the labels and the ticks
        sliderComputerPlayer.setPaintTicks(true);
        sliderComputerPlayer.setPaintLabels(true);
        //allow that only values at the ticks can be selected
        sliderComputerPlayer.setSnapToTicks(true);
        //add the slider to the panel
        panelComputerPlayerSettings.add(sliderComputerPlayer);

        //if the slider changes refresh the map where everything is stored
        sliderComputerPlayer.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                //The slider where the current value is stored
                JSlider slider = (JSlider) e.getSource();
                //get the current value as string
                String currentValue = Integer.toString(slider.getValue());
                //change the corresponding map value
                preferenceMap.put(ReversiGameConfiguration.ALGORITHM_SEARCH_DEPTH, currentValue);
            }
        });
    }

    /**
     * This method is used to initialize the panel containing the human player settings
     */
    private void initializeHumanPlayerSettings() {
        //create a new human player setting panel
        panelHumanPlayerSettings = new JPanel(new GridLayout(1, 3, 10, 10));
        //set the boarder
        panelHumanPlayerSettings.setBorder(BorderFactory.createTitledBorder(
                RES.getString("ui.preferences.title.player")));

        //Create Radio Buttons for the player
        final JRadioButton buttonPlayerBlack = new JRadioButton(RES.getString("ui.preferences.player.black"));
        //add the action command string to the button (this is the value of the config file)
        buttonPlayerBlack.setActionCommand("black");
        JRadioButton buttonWhitePlayer = new JRadioButton(RES.getString("ui.preferences.player.white"));
        //add the action command string to the button (this is the value of the config file)
        buttonWhitePlayer.setActionCommand("white");

        //Group the buttons together
        final ButtonGroup playerGroup = new ButtonGroup();
        playerGroup.add(buttonWhitePlayer);
        playerGroup.add(buttonPlayerBlack);

        //Add the buttons to the UI
        panelHumanPlayerSettings.add(buttonWhitePlayer);
        panelHumanPlayerSettings.add(buttonPlayerBlack);

        //Load the human player color from the config
        String humanPlayerColor = getValueFromReversiGameConfiguration(
                ReversiGameConfiguration.HUMAN_PLAYER_COLOR, "black");
        //if there is written black in the config color of human player is black
        if (humanPlayerColor.equalsIgnoreCase("black")) {
            buttonPlayerBlack.setSelected(true);
        } else {
            buttonWhitePlayer.setSelected(true);
        }

        //if the button gains a focus change the map
        FocusListener humanPlayerListener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                //The radio button where the current human player color value is stored
                JRadioButton button = (JRadioButton) e.getSource();
                //get the current human player color value
                String humanPlayerColor = button.getActionCommand();
                //change the corresponding map value
                preferenceMap.put(ReversiGameConfiguration.HUMAN_PLAYER_COLOR, humanPlayerColor);
            }

            @Override
            public void focusLost(FocusEvent e) {
                //nothing should be done when focus is lost
            }
        };

        buttonPlayerBlack.addFocusListener(humanPlayerListener);
        buttonWhitePlayer.addFocusListener(humanPlayerListener);
    }


    /**
     * This method is used to initialize the panel containing the player settings
     */
    private void initializePlayerSettings() {
        //create a new player setting panel with a grid layout
        panelPlayerSettings = new JPanel(new GridBagLayout());
        //constraints for the gird bag layout
        GridBagConstraints constraints = new GridBagConstraints();

        //set the boarder
        panelPlayerSettings.setBorder(BorderFactory.createTitledBorder(
                RES.getString("ui.preferences.title.change.color")));

        //Create the label and the button for the white player
        final JLabel labelPlayerWhite = new JLabel(ICON_PLAYER_WHITE);
        labelPlayerWhite.setPreferredSize(DIMENSION);
        JButton buttonChangeWhiteColor = new JButton(RES.getString("ui.preferences.change.color"));

        //Create the label and the button for the black player
        final JLabel labelPlayerBlack = new JLabel(ICON_PLAYER_BLACK);
        labelPlayerBlack.setPreferredSize(DIMENSION);
        JButton buttonChangeBlackColor = new JButton(RES.getString("ui.preferences.change.color"));

        //set up the constraints of the panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        //Add the labels to the panel
        panelPlayerSettings.add(labelPlayerBlack, constraints);
        constraints.gridx = 1;
        panelPlayerSettings.add(labelPlayerWhite, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        //Add the buttons to the panel
        panelPlayerSettings.add(buttonChangeBlackColor, constraints);
        constraints.gridx = 1;
        panelPlayerSettings.add(buttonChangeWhiteColor, constraints);

        //the action listener for the white color button
        buttonChangeWhiteColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the color from the color picker
                Color color = Color.decode(preferenceMap.get(ReversiGameConfiguration.PLAYER_WHITE_COLOR));
                color = getNewPlayerColor(color);

                //get the hex code of the color
                String newColor = getHexCode(color);

                //write the new color to the string
                preferenceMap.put(ReversiGameConfiguration.PLAYER_WHITE_COLOR, newColor);
                //refresh the icons
                refreshPlayerIcons();
                //set the icon
                labelPlayerWhite.setIcon(ICON_PLAYER_WHITE);
            }
        });

        //the action listener for the white color button
        buttonChangeBlackColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the color from the color picker
                Color color = Color.decode(preferenceMap.get(ReversiGameConfiguration.PLAYER_BLACK_COLOR));
                color = getNewPlayerColor(color);

                //get the hex code of the color
                String newColor = getHexCode(color);

                //write the new color to the string
                preferenceMap.put(ReversiGameConfiguration.PLAYER_BLACK_COLOR, newColor);
                //refresh the icons
                refreshPlayerIcons();
                //set the icon
                labelPlayerBlack.setIcon(ICON_PLAYER_BLACK);
            }
        });
    }

    /**
     * This method is used to initialize the panel containing the save changes
     */
    private void initializeSaveChangesPanel() {
        panelStoreCurrentConfiguration = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton buttonSaveConfig = new JButton(RES.getString("ui.preferences.store"));
        JButton buttonCancel = new JButton(RES.getString("ui.preferences.cancel"));

        panelStoreCurrentConfiguration.add(buttonCancel);
        panelStoreCurrentConfiguration.add(buttonSaveConfig);

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                preferencesDialog.dispose();
            }
        });

        buttonSaveConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChangesToReversiGameConfiguration();
                preferencesDialog.dispose();
            }
        });
    }

    /**
     * This method is used to store the changed preferences to the game config
     */
    private void saveChangesToReversiGameConfiguration() {
        //Get all the set keys of the preference map
        Set<String> keys = preferenceMap.keySet();

        //for each key set the value to the reversi game configuration
        for (String key : keys) {
            changeReversiGameConfiguration(key, preferenceMap.get(key));
        }

        //set the flag that the config has changed
        hasConfigurationChanged = true;
    }

    /**
     * Refreshes the player icons
     */
    private void refreshPlayerIcons() {
        //decode the white hex string
        Color whitePlayer = Color.decode(preferenceMap.get(ReversiGameConfiguration.PLAYER_WHITE_COLOR));
        ICON_PLAYER_WHITE = new GraphicalGameView.PlayerIcon(whitePlayer, DIMENSION.height);

        Color blackPlayer = Color.decode(preferenceMap.get(ReversiGameConfiguration.PLAYER_BLACK_COLOR));
        ICON_PLAYER_BLACK = new GraphicalGameView.PlayerIcon(blackPlayer, DIMENSION.height);
    }

    private String getHexCode(Color color) {
        //get the rgb value of the color with the alpha value
        int rgb = color.getRGB();
        //convert the int to hex
        String hexCode = Integer.toHexString(rgb);

        //Return the color value without the alpha value in caps
        return "0x" + hexCode.substring(2).toUpperCase();
    }

    private Color getNewPlayerColor(Color currentColor) {
        //Create a new ColorChooser with an initial color
        JColorChooser chooser = new JColorChooser(currentColor);

        //Remove all unwanted preview panels using a different color model than RGB
        for (AbstractColorChooserPanel panel : chooser.getChooserPanels()) {
            if (!panel.getDisplayName().equals("RGB")) {
                chooser.removeChooserPanel(panel);
            }
        }

        //Set the preview panel of the ColorChooser
        chooser.setPreviewPanel(new ColorChooserPreview(chooser));
        //the tracker which keeps track of which color is currently selected by the user
        ColorTracker tracker = new ColorTracker(chooser);

        //The dialog that should be displayed to the user so that the user is able to select a color
        String title = RES.getString("ui.preferences.change.color");

        JDialog colorDialog = JColorChooser.createDialog(null, title, true, chooser, tracker, null);
        //show the dialog to the user
        colorDialog.setVisible(true);

        //return the color
        return tracker.getColor();
    }

    /**
     * This method is used to get the current values from the ReversiGameConfiguration.
     *
     * @param key          The key of the value that should be returned.
     * @param defaultValue The default value that will be returned if there is no value with this key.
     * @return A string containing the value of the corresponding key.
     */
    private String getValueFromReversiGameConfiguration(String key, String defaultValue) {
        return configuration.getProperty(key, defaultValue);
    }

    /**
     * This method is used to refresh the ReversiGameConfiguration
     *
     * @param key      The key of the value that should be changed.
     * @param newValue The new value that should be assigned to the configuration.
     */
    private void changeReversiGameConfiguration(String key, String newValue) {
        //set the new value to the configuration
        configuration.setProperty(key, newValue);

        //the configuration has changed
        hasConfigurationChanged = true;
    }

    private static class ColorTracker implements ActionListener {
        //The ColorChooser where the color will be selected
        private final JColorChooser chooser;
        //The currently selected color of the ColorChooser
        private Color color;

        /**
         * Constructor of the ColorTracker
         *
         * @param chooser The ColorChooser which color change should be tracked
         */
        private ColorTracker(JColorChooser chooser) {
            //set the chooser to the class
            this.chooser = chooser;
            //set the initial color to the color tracker to avoid a java.lang.NullPointerException
            this.color = this.chooser.getColor();
        }

        //If the user clicks on any button the current stored color will be reloaded
        @Override
        public void actionPerformed(ActionEvent e) {
            //set the color to the currently selected color
            color = chooser.getColor();
        }

        /**
         * This method returns the selected color of the color tracker. If no color was selected the
         * initial color of the chooser will be returned.
         *
         * @return The color that was selected by the user
         */
        public Color getColor() {
            return color;
        }
    }

    /**
     * This class is responsible for previewing the color that is selected by the user
     */
    private static class ColorChooserPreview extends JComponent {
        //The initial color of the ColorChooser
        private final Color START_COLOR;
        //The currently selected color of the ColorChooser
        private Color currentColor;

        /**
         * Sets up the class.
         *
         * @param chooser The ColorChooser which should be "watched"
         */
        private ColorChooserPreview(JColorChooser chooser) {
            //set the initial color
            START_COLOR = chooser.getColor();
            //set the current color
            currentColor = chooser.getColor();

            //Get the color selection model and add an change listener so that changes are registered.
            ColorSelectionModel model = chooser.getSelectionModel();
            model.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    //Get the model from the event
                    ColorSelectionModel model = (ColorSelectionModel) e.getSource();
                    //store the new selected color in the current color
                    currentColor = model.getSelectedColor();
                }
            });

            //set the preferred size to 60
            setPreferredSize(new Dimension(60, 60));
        }

        @Override
        public void paint(Graphics g) {
            //calculate the height where the other rectangle should start
            int height = getHeight() / 2;

            //set the initial color to draw the first half of the preview square
            g.setColor(START_COLOR);
            g.fillRect(0, 0, getWidth() - 1, height);
            //set the current color to draw the second half of the preview square
            g.setColor(currentColor);
            g.fillRect(0, height, getWidth() - 1, getHeight() - 1);
        }
    }
}
