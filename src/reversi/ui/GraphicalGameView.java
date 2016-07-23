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
package reversi.ui;

import reversi.board.Board;
import reversi.board.GameMove;
import reversi.board.GamePosition;
import reversi.board.Square;
import reversi.board.SquareState;
import reversi.game.Game;
import reversi.game.ReversiGameConfiguration;
import reversi.player.Player;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import java.util.Observable;

/**
 * This is an implementation of a GameView for a GraphicalUserInterface
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 17. May 2016
 */
public class GraphicalGameView extends GameView {
    //The frame where the board will be displayed
    private final JFrame frame;
    //The Squares of the GUI
    private final JButton[][] squares;
    //The board dimensions
    private final int BOARD_WIDTH;
    private final int BOARD_HEIGHT;
    //the size of an square
    private final Dimension DIMENSION = new Dimension(60, 60);
    //The panel where the board will be displayed
    private JPanel boardPanel;
    //The panel where the game information will be displayed
    private JPanel gameInformationPanel;
    //The layout
    private GridBagLayout gridBagLayout;
    //The icon for the players
    private PlayerIcon ICON_WHITE_PLAYER;
    private PlayerIcon ICON_BLACK_PLAYER;
    //The labels
    private JLabel labelWhoseTurn;
    private JLabel labelWhitePlayer;
    private JLabel labelBlackPlayer;
    private JLabel labelWhitePlayerDisks;
    private JLabel labelBlackPlayerDisks;
    //The input of the user
    private String userInput;
    /**
     * The action which should be performed when a square button is clicked
     */
    private final ActionListener userInputsMove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Get the button which is clicked
            JButton button = ((JButton) e.getSource());

            //Get the constraints
            GridBagConstraints constraints = gridBagLayout.getConstraints(button);

            //Calculate the board coordinates from the constraints
            int xPos = constraints.gridx - 1;
            int yPos = constraints.gridy - 1;

            //Create the user input String
            userInput = xPos + " " + yPos;
            //disable input again
            disableInput();

            //notify the waiting thread that user input is done
            resultsReady.signal();
        }
    };

    /**
     * Constructor to set up the GraphicalGameView
     *
     * @param gameModel The game model which should be displayed.
     */
    public GraphicalGameView(Game gameModel) {
        super(gameModel);

        //Get the dimension of the board from the model.
        BOARD_WIDTH = gameModel.getGamePosition().getBoard().getBoardWidth();
        BOARD_HEIGHT = gameModel.getGamePosition().getBoard().getBoardHeight();

        updatePlayerIcons();

        //create the buttons
        squares = new JButton[BOARD_WIDTH][BOARD_HEIGHT];

        //Initialize the panels
        initializeBoardPanel();
        initializeGameInformationPanel();

        //Set the title for the window
        frame = new JFrame(RES.getString("ui.window.title"));

        //create a new menu bar
        JMenuBar menuBar = new JMenuBar();
        //create file menu
        JMenu menuFile = new JMenu(RES.getString("ui.menu.bar.file"));
        //create settings menu item
        JMenuItem menuItemSettings = new JMenuItem(RES.getString("ui.menu.bar.file.settings"));
        //Add to file menu
        menuFile.add(menuItemSettings);
        //Add to menu bar
        menuBar.add(menuFile);

        //add the action to the menu item settings
        menuItemSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //open a new preference window
                GraphicalGamePreferences preferences = new GraphicalGamePreferences(frame);
                //if preferences have changed please update the ui as well
                if (preferences.hasConfigurationChanged()) {
                    //update the icons
                    updatePlayerIcons();
                    //update the current game information panel as well
                    updateGameInformation();
                    //update the board so that the new icons are applied
                    updateBoard();
                }
            }
        });

        frame.setJMenuBar(menuBar);

        //Add the components to the window
        JPanel root = new JPanel(new BorderLayout());
        root.add(gameInformationPanel, BorderLayout.LINE_END);
        root.add(boardPanel, BorderLayout.CENTER);

        //Set the content pane
        frame.setContentPane(root);
    }

    /**
     * This method sets up the board panel.
     */
    private void initializeBoardPanel() {
        //the layout for the board panel
        gridBagLayout = new GridBagLayout();
        //set the layout to the panel
        boardPanel = new JPanel(gridBagLayout);

        //The constraints for the layout
        GridBagConstraints constraints = new GridBagConstraints();

        //SET VERTICAL NUMBERS
        for (int x = 0; x < BOARD_WIDTH + 2; x += BOARD_WIDTH + 1) {
            //set the x position of the grid
            constraints.gridx = x;
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                //set the y position of the grid
                constraints.gridy = y + 1;

                //Calculate the number of the row
                int number = 1 + y;

                //Create a new label containing the number of the row
                JLabel label = new JLabel("" + number, SwingConstants.CENTER);
                //set the label to the preferred size
                label.setPreferredSize(DIMENSION);

                //Add the label to the board panel.
                boardPanel.add(label, constraints);
            }
        }

        //SET HORIZONTAL LETTERS
        for (int y = 0; y < BOARD_HEIGHT + 2; y += BOARD_HEIGHT + 1) {
            //set the y position of the grid
            constraints.gridy = y;
            for (int x = 0; x < BOARD_WIDTH; x++) {
                //set the x position of the grid
                constraints.gridx = x + 1;

                //Calculate the letter of the column
                char letter = (char) ('A' + x);

                //Create a new label containing the letter of the row
                JLabel label = new JLabel("" + letter, SwingConstants.CENTER);
                //set the label to the preferred size
                label.setPreferredSize(DIMENSION);

                //Add the label to the board panel
                boardPanel.add(label, constraints);
            }
        }

        //SET BOARD SQUARES
        for (int x = 1; x <= BOARD_WIDTH; x++) {
            for (int y = 1; y <= BOARD_HEIGHT; y++) {
                //Set the x and y position on the grid
                constraints.gridx = x;
                constraints.gridy = y;

                //create a new button
                squares[x - 1][y - 1] = new JButton();
                //save the button on a local variable
                JButton button = squares[x - 1][y - 1];
                //set the preferred size and add the action listener on each button
                button.setPreferredSize(DIMENSION);
                button.addActionListener(userInputsMove);
                //set the button disabled so that it can not be clicked
                button.setEnabled(false);

                //add the button to the board panel
                boardPanel.add(button, constraints);
            }
        }

        //set the background color on the board panel
        boardPanel.setBackground(Color.LIGHT_GRAY);
    }

    /**
     * This method sets uo the game information panel
     */
    private void initializeGameInformationPanel() {
        //The color of the game information panel
        Color color = new Color(200, 200, 200);

        //create a new game information panel with a grid bag layout
        gameInformationPanel = new JPanel(new GridBagLayout());
        //set the background color
        gameInformationPanel.setBackground(color);
        //The constraints of the grid
        GridBagConstraints constraints = new GridBagConstraints();

        //Set the grid constraints
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        //Create the game panel with a box layout and set the background color
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
        gamePanel.setBackground(color);

        //create the panel current player, set the background and add a titled boarder
        JPanel currentPlayer = new JPanel();
        currentPlayer.setBackground(color);
        currentPlayer.setBorder(BorderFactory.createTitledBorder(RES.getString("ui.label.current.player")));

        //Init label whose turn and add it to the right panel
        labelWhoseTurn = new JLabel(ICON_WHITE_PLAYER, SwingConstants.CENTER);
        labelWhoseTurn.setPreferredSize(new Dimension(60, 60));
        currentPlayer.add(labelWhoseTurn);

        //Create a new panel for the player status, set the background color add a titled boarder.
        JPanel panelPlayerStatus = new JPanel(new GridBagLayout());
        panelPlayerStatus.setBackground(color);
        //Constraints for the player status panel
        GridBagConstraints constraintsPlayer = new GridBagConstraints();
        panelPlayerStatus.setBorder(BorderFactory.createTitledBorder(RES.getString("ui.label.status.player")));

        //Set up the constraints of the player panel
        constraintsPlayer.gridx = 0;
        constraintsPlayer.gridy = 0;
        constraintsPlayer.weightx = 0.4;
        constraintsPlayer.ipady = 10;
        //Add a new label on the player panel for the white player
        labelWhitePlayer = new JLabel(ICON_WHITE_PLAYER, SwingConstants.CENTER);
        labelWhitePlayer.setPreferredSize(new Dimension(60, 60));
        panelPlayerStatus.add(labelWhitePlayer, constraintsPlayer);

        //set the constraints of the player panel
        constraintsPlayer.gridx = 1;
        constraintsPlayer.weightx = 0.6;
        //Add a new label on the player panel for the white player
        labelWhitePlayerDisks = new JLabel("", SwingConstants.CENTER);
        panelPlayerStatus.add(labelWhitePlayerDisks, constraintsPlayer);

        //Set up the constraints of the player panel for the black player
        constraintsPlayer.gridx = 0;
        constraintsPlayer.gridy = 1;
        constraintsPlayer.weightx = 0.4;
        //Add a new label on the player panel for the black player
        labelBlackPlayer = new JLabel(ICON_BLACK_PLAYER, SwingConstants.CENTER);
        labelWhitePlayer.setPreferredSize(new Dimension(60, 60));
        panelPlayerStatus.add(labelBlackPlayer, constraintsPlayer);

        //set the constraints for the player panel
        constraintsPlayer.gridx = 1;
        constraintsPlayer.weightx = 0.6;
        //Add a new label on the player panel for the black player
        labelBlackPlayerDisks = new JLabel("", SwingConstants.CENTER);
        panelPlayerStatus.add(labelBlackPlayerDisks, constraintsPlayer);

        //add this to the game panel and then add the game panel to the game information panel
        gamePanel.add(currentPlayer);
        gamePanel.add(panelPlayerStatus);
        gameInformationPanel.add(gamePanel, constraints);

        //Set up the constraints for the grid layout
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipady = 25;
        constraints.ipadx = 40;
        //Everything needed for the GameControl Panel will be put here
        JPanel menuPanel = new JPanel(new GridLayout(2, 2));
        menuPanel.setBorder(BorderFactory.createTitledBorder(RES.getString("ui.label.menu")));
        menuPanel.setBackground(color);

        //Create the 4 control buttons
        final JButton buttonResign = new JButton(RES.getString("ui.label.resign"));
        final JButton buttonTakeBack = new JButton(RES.getString("ui.label.take.back"));
        final JButton buttonNewGame = new JButton(RES.getString("ui.label.new.game"));
        final JButton buttonExit = new JButton(RES.getString("ui.label.exit"));

        //Add the action to the control buttons
        buttonTakeBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Disable input even when it would not make any sense
                disableInput();
                //controller to handle the game action
                gameController.handleUserAction(GameController.GameAction.TAKE_BACK);
            }
        });

        buttonNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Enable the buttons that should be click able again
                buttonResign.setEnabled(true);
                buttonTakeBack.setEnabled(true);
                //input no longer needed
                disableInput();
                //controller to handle the game action
                gameController.handleUserAction(GameController.GameAction.NEW_GAME);
            }
        });

        buttonResign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Disable the buttons that should not be click able any more
                buttonTakeBack.setEnabled(false);
                buttonResign.setEnabled(false);
                //Input no longer needed
                disableInput();
                //controller to handle the game action
                gameController.handleUserAction(GameController.GameAction.RESIGN);
            }
        });

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //controller to handle the game action
                gameController.handleUserAction(GameController.GameAction.END_GAME);
                //Input no longer needed
                disableInput();
                //Get rid of the frame because it is no longer needed
                frame.dispose();
            }
        });

        //Add the control buttons on the menu panel
        menuPanel.add(buttonResign);
        menuPanel.add(buttonTakeBack);
        menuPanel.add(buttonNewGame);
        menuPanel.add(buttonExit);

        //Add the menu panel on the game information panel
        gameInformationPanel.add(menuPanel, constraints);
        gameInformationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    /**
     * This method is called to show the view.
     */
    @Override
    public void show() {
        //set frame not resizable
        frame.setResizable(false);
        //draw the frame
        frame.pack();
        //frame is closable when the window is closed
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //show window centered on screen
        frame.setLocationRelativeTo(null);

        //frame is now visible
        frame.setVisible(true);

        super.show();
    }

    /**
     * This method is used to request any input from the user
     *
     * @return The input from the user
     * @throws InterruptedException If the program is interrupted whilst requesting any user input.
     */
    @Override
    protected String requestUserInput() throws InterruptedException {
        //wait for the input
        resultsReady.await();

        //return the input
        return userInput;
    }

    /**
     * This method is used to display any messages on the console game view
     *
     * @param message The message which should be displayed
     */
    @Override
    protected void displayMessage(String message) {
        //NOT WANTED YET
    }

    /**
     * This method shows all legal moves on the graphical user interface
     */
    @Override
    protected void showAllLegalMoves() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Get the current game position and the current player
                Board board = gameModel.getGamePosition().getBoard();
                Player currentPlayer = gameModel.getGamePosition().getCurrentPlayer();

                //Get all legal moves
                List<GameMove> legalMoves = board.getAllLegalMoves(currentPlayer);
                for (GameMove move : legalMoves) {
                    //Get the square
                    Square square = move.getSquare();
                    //Get the x and y position of the square
                    int xPos = square.getXPosition();
                    int yPos = square.getYPosition();

                    //Enable the square
                    squares[xPos][yPos].setEnabled(true);
                }
            }
        });
    }

    /**
     * This method is called when the observed object has changed.
     *
     * @param o   The Observable that has changed
     * @param arg An argument
     */
    @Override
    public void update(Observable o, Object arg) {
        //updates the board
        updateBoard();
        //updates the game information panel
        updateGameInformation();
    }

    /**
     * This method is responsible for updating the board
     */
    private void updateBoard() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Get the current board
                Board board = gameModel.getGamePosition().getBoard();

                //iterate over the board
                for (int x = 0; x < board.getBoardWidth(); x++) {
                    for (int y = 0; y < board.getBoardHeight(); y++) {
                        //remove all icons from the square
                        squares[x][y].setIcon(null);

                        //get the square from the board
                        Square square = board.getSquare(x, y);
                        SquareState state = square.getSquareState();

                        //Add the corresponding icon to the square
                        if (state == SquareState.WHITE) {
                            squares[x][y].setIcon(ICON_WHITE_PLAYER);
                        } else if (state == SquareState.BLACK) {
                            squares[x][y].setIcon(ICON_BLACK_PLAYER);
                        }
                    }
                }
            }
        });
    }

    /**
     * This method is responsible for updating the game information panel
     */
    private void updateGameInformation() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Get the current game position
                GamePosition gamePosition = gameModel.getGamePosition();

                //Set the label which displays whose turn it is
                if (gamePosition.getCurrentPlayer() == Player.BLACK) {
                    labelWhoseTurn.setIcon(ICON_BLACK_PLAYER);
                } else {
                    labelWhoseTurn.setIcon(ICON_WHITE_PLAYER);
                }

                //get the internationalized format String for the number of disks
                String format = RES.getString("ui.number.disks");

                //get the number of disks for any player
                int numberWhiteDisks = gamePosition.getBoard().countPieces(Player.WHITE);
                int numberBlackDisks = gamePosition.getBoard().countPieces(Player.BLACK);

                //Update the label
                labelBlackPlayerDisks.setText(String.format(format, numberBlackDisks));
                labelWhitePlayerDisks.setText(String.format(format, numberWhiteDisks));
            }
        });
    }

    /**
     * This method is used to disable all user input from the gui
     */
    private void disableInput() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Iterates over all squares and sets disables all enabled squares.
                for (int x = 0; x < BOARD_WIDTH; x++) {
                    for (int y = 0; y < BOARD_HEIGHT; y++) {
                        JButton button = squares[x][y];
                        if (button.isEnabled()) {
                            button.setEnabled(false);
                        }
                    }
                }
            }
        });
    }

    private void updatePlayerIcons() {
        //Get the configuration to load the color of the players pieces
        ReversiGameConfiguration configuration = ReversiGameConfiguration.getInstance();

        //Load the string values of the colors of the players
        String colorBlack = configuration.getProperty(ReversiGameConfiguration.PLAYER_BLACK_COLOR, "0xFF0000");
        String colorWhite = configuration.getProperty(ReversiGameConfiguration.PLAYER_WHITE_COLOR, "0xFFFF00");

        //Decode the color values from the hexadecimal input
        ICON_BLACK_PLAYER = new PlayerIcon(Color.decode(colorBlack), DIMENSION.height);
        ICON_WHITE_PLAYER = new PlayerIcon(Color.decode(colorWhite), DIMENSION.height);

        if (labelBlackPlayer != null) {
            labelBlackPlayer.setIcon(ICON_BLACK_PLAYER);
        }

        if (labelWhitePlayer != null) {
            labelWhitePlayer.setIcon(ICON_WHITE_PLAYER);
        }
    }

    /**
     * Private class for displaying the players disks.
     */
    static class PlayerIcon implements Icon {
        private final Color color;
        private final int size;

        PlayerIcon(Color color, int size) {
            this.color = color;
            this.size = size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.drawOval(x, y, getIconWidth(), getIconHeight());
            g.fillOval(x, y, getIconWidth(), getIconHeight());
        }

        @Override
        public int getIconWidth() {
            return size - 20;
        }

        @Override
        public int getIconHeight() {
            return size - 20;
        }
    }
}
