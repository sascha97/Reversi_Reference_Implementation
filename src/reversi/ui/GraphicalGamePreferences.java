/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
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
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ResourceBundle;

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 06. July 2016
 */
class GraphicalGamePreferences {
    //the configuration of the game
    private final ReversiGameConfiguration configuration = ReversiGameConfiguration.getInstance();

    //The resource bundle containing all the strings
    private final ResourceBundle RES = ResourceBundle.getBundle("strings/Values");

    //the frame containing all the preferences
    private final JDialog preferencesDialog;

    //The JPanels of the Preferences Panel
    //settings concerning the board and its size
    private JPanel panelBoardSettings;
    //settings concerning the search depth of the algorithm
    private JPanel panelComputerPlayerSettings;
    //settings concerning the color of the player
    private JPanel panelPlayerSettings;
    //settings concerning whether the human player is black or white
    private JPanel panelHumanPlayerSettings;

    //The icons that are drawn on the configuration ui
    private Icon ICON_PLAYER_WHITE;
    private Icon ICON_PLAYER_BLACK;

    //The strings containing the string values of the color
    private String stringColorWhitePlayer;
    private String stringColorBlackPlayer;

    //boolean flag used to check if the configuration has changed.
    private boolean hasConfigurationChanged = false;

    GraphicalGamePreferences(JFrame parent) {
        //Load the color values of the icons from the configuration
        stringColorWhitePlayer = getValueFromReversiGameConfiguration(
                ReversiGameConfiguration.PLAYER_WHITE_COLOR, "0xFFFF00");
        stringColorBlackPlayer = getValueFromReversiGameConfiguration(
                ReversiGameConfiguration.PLAYER_BLACK_COLOR, "0xFF0000");
        //set the player icons
        refreshPlayerIcons(stringColorWhitePlayer, stringColorBlackPlayer);

        //initialize the panel containing the board settings
        initializeBoardSettings();
        //initialize the panel containing the computer player settings
        initializeComputerPlayerSettings();
        //initialize the panel containing the human player settings
        initializeHumanPlayerSettings();
        //initialize the panel containing the player color settings
        initializePlayerSettings();

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

        //set the content frame to the window
        preferencesDialog.setContentPane(rootPane);

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
     * This method is used to initialize the panel containing the board settings
     */
    private void initializeBoardSettings() {
        //create a new board setting panel with a grid layout
        panelBoardSettings = new JPanel(new GridLayout(1, 2, 10, 10));
        //set the boarder
        panelBoardSettings.setBorder(BorderFactory.createTitledBorder(RES.getString("ui.preferences.title.board")));

        //Load the current board size from the configuration
        String boardSize = getValueFromReversiGameConfiguration(ReversiGameConfiguration.BOARD_SIZE, "8");
        int currentValue = Integer.parseInt(boardSize);

        //The values of the slider
        int minValue = 6;
        int maxValue = 16;
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


        //the button that has to be clicked to apply the settings
        JButton buttonSaveChanges = new JButton(RES.getString("ui.preferences.store"));

        //add an action listener to the button so that the new size is applied to the configuration
        buttonSaveChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the value from the slider
                int newBoardSize = sliderBoardSize.getValue();

                //set the new value to the configuration
                changeReversiGameConfiguration(ReversiGameConfiguration.BOARD_SIZE, newBoardSize);
            }
        });

        //add the button to the panel
        panelBoardSettings.add(buttonSaveChanges);
    }

    /**
     * This method is used to initialize the panel containing the computer player settings
     */
    private void initializeComputerPlayerSettings() {
        //create a new computer player setting panel with a grid layout
        panelComputerPlayerSettings = new JPanel(new GridLayout(1, 2, 10, 10));
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

        //the button that has to be clicked to apply the settings
        JButton buttonSaveChanges = new JButton(RES.getString("ui.preferences.store"));

        //add an action listener to the button so that the search depth is applied to the configuration
        buttonSaveChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the value from the slider
                int algorithmSearchDepth = sliderComputerPlayer.getValue();

                //set the new value to the configuration
                changeReversiGameConfiguration(ReversiGameConfiguration.ALGORITHM_SEARCH_DEPTH, algorithmSearchDepth);
            }
        });

        //add the button to the panel
        panelComputerPlayerSettings.add(buttonSaveChanges);
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
        JRadioButton buttonPlayerBlack = new JRadioButton(RES.getString("ui.preferences.player.black"));
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

        //the button that has to be clicked to apply the settings
        JButton buttonSaveChanges = new JButton(RES.getString("ui.preferences.store"));

        //add an action listener to the button so that the human player color is applied to the configuration
        buttonSaveChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the value of the human player from the player group
                String humanPlayerColor = playerGroup.getSelection().getActionCommand();

                //store the new value in the configuration
                changeReversiGameConfiguration(ReversiGameConfiguration.HUMAN_PLAYER_COLOR, humanPlayerColor);
            }
        });

        //add the button to the panel
        panelHumanPlayerSettings.add(buttonSaveChanges);
    }


    /**
     * This method is used to initialize the panel containing the player settings
     */
    private void initializePlayerSettings() {
        //create a new player setting panel with a grid layout
        panelPlayerSettings = new JPanel(new GridLayout(3, 2, 10, 10));
        //set the boarder
        panelPlayerSettings.setBorder(BorderFactory.createTitledBorder(
                RES.getString("ui.preferences.title.change.color")));

        //Create the label and the button for the white player
        final JLabel labelPlayerWhite = new JLabel(ICON_PLAYER_WHITE);
        JButton buttonChangeWhiteColor = new JButton(RES.getString("ui.preferences.change.color"));

        //Add the label and the button to the panel
        panelPlayerSettings.add(labelPlayerWhite);
        panelPlayerSettings.add(buttonChangeWhiteColor);

        //Create the label and the button for the black player
        final JLabel labelPlayerBlack = new JLabel(ICON_PLAYER_BLACK);
        JButton buttonChangeBlackColor = new JButton(RES.getString("ui.preferences.change.color"));

        //Add the label and the button to the panel
        panelPlayerSettings.add(labelPlayerBlack);
        panelPlayerSettings.add(buttonChangeBlackColor);

        //the action listener for the white color button
        buttonChangeWhiteColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the color from the color picker
                Color color = Color.decode(stringColorWhitePlayer);
                color = getNewPlayerColor(color);

                //get the hex code of the color
                String newColor = getHexCode(color);
                //if color has changed
                if (!newColor.equalsIgnoreCase(stringColorWhitePlayer)) {
                    //write the new color to the string
                    stringColorWhitePlayer = newColor;
                    //refresh the icons
                    refreshPlayerIcons(stringColorWhitePlayer, stringColorBlackPlayer);

                    //set the icons
                    labelPlayerWhite.setIcon(ICON_PLAYER_WHITE);
                    labelPlayerBlack.setIcon(ICON_PLAYER_BLACK);
                }
            }
        });

        //the action listener for the white color button
        buttonChangeBlackColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the color from the color picker
                Color color = Color.decode(stringColorBlackPlayer);
                color = getNewPlayerColor(color);

                //get the hex code of the color
                String newColor = getHexCode(color);
                //if color has changed
                if (!newColor.equalsIgnoreCase(stringColorBlackPlayer)) {
                    //write the new color to the string
                    stringColorBlackPlayer = newColor;
                    //refresh the icons
                    refreshPlayerIcons(stringColorWhitePlayer, stringColorBlackPlayer);

                    //set the icons
                    labelPlayerWhite.setIcon(ICON_PLAYER_WHITE);
                    labelPlayerBlack.setIcon(ICON_PLAYER_BLACK);
                }
            }
        });


        //add an empty panel to the layout so that the button is on the right side
        JPanel blankPanel = new JPanel();
        panelPlayerSettings.add(blankPanel);

        //the button that has to be clicked to apply the settings
        JButton buttonSaveChanges = new JButton(RES.getString("ui.preferences.store"));

        //add an action listener to the button so that the new color values are applied to the configuration
        buttonSaveChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeReversiGameConfiguration(ReversiGameConfiguration.PLAYER_BLACK_COLOR, stringColorBlackPlayer);
                changeReversiGameConfiguration(ReversiGameConfiguration.PLAYER_WHITE_COLOR, stringColorWhitePlayer);
            }
        });
        //add the button to the panel
        panelPlayerSettings.add(buttonSaveChanges);
    }

    /**
     * Refreshes the player icons
     *
     * @param colorWhite A hex string containing the color value of the white player
     * @param colorBlack A hex string containing the color value of the black player
     */
    private void refreshPlayerIcons(String colorWhite, String colorBlack) {
        //decode the white hex string
        Color whitePlayer = Color.decode(colorWhite);
        ICON_PLAYER_WHITE = new GraphicalGameView.PlayerIcon(whitePlayer);

        Color blackPlayer = Color.decode(colorBlack);
        ICON_PLAYER_BLACK = new GraphicalGameView.PlayerIcon(blackPlayer);
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

    /**
     * This method is used to refresh the ReversiGameConfiguration
     *
     * @param key      The key of the value that should be changed.
     * @param newValue The new value that should be assigned to the configuration.
     */
    private void changeReversiGameConfiguration(String key, int newValue) {
        changeReversiGameConfiguration(key, "" + newValue);
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
