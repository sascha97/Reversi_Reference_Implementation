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

import reversi.actor.HumanActor;
import reversi.board.Board;
import reversi.board.GameMove;
import reversi.board.GamePosition;
import reversi.board.Square;
import reversi.board.SquareState;
import reversi.game.Game;
import reversi.game.ReversiGameConfiguration;
import reversi.game.ThreadEvent;
import reversi.player.Player;

import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Add a description
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 21. July 2016
 */
public class FxGameViewController implements Initializable, HumanActor.HumanActable {
    //The grid pane in which the board will be displayed
    @FXML
    private GridPane boardPane;

    //The circle which is representing the current player icon
    @FXML
    private Circle currentPlayer;

    //The circle which is representing the black player
    @FXML
    private Circle blackPlayer;

    //The circle which is representing the white player
    @FXML
    private Circle whitePlayer;

    //The label which holds the number of black disks
    @FXML
    private Label labelBlackPlayerDisks;

    //The label which holds the number of white disks
    @FXML
    private Label labelWhitePlayerDisks;

    //The buttons which can be clicked by the user
    private ButtonCircle[][] squares;

    //The game which is displayed in this window
    private Game gameModel;

    //The configuration where all the values are loaded from
    private ReversiGameConfiguration configuration = ReversiGameConfiguration.getInstance();

    //A boolean flag indicating whether the animations should be played or not
    private boolean showAnimations;

    //The resources
    private ResourceBundle resources;

    //The last input of the user
    private String userInput;

    //used for waiting for user input
    private ThreadEvent resultsReady = new ThreadEvent();

    //this is called if user inputs move
    private EventHandler<MouseEvent> userInputsMove = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            //get the button which was clicked
            Button button = (Button) event.getSource();
            //store the user data as input
            userInput = button.getUserData().toString();

            //disable the input again
            disableInput();

            //notify the waiting thread that user input is done
            resultsReady.signal();
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        initializeBoardPanel();
        initializeGameInformationPanel();
        updateShowAnimations();
    }

    /**
     * This method sets the GameModel to the controller
     *
     * @param gameModel The game model which should be added to the controller
     */
    public void setGameModel(Game gameModel) {
        this.gameModel = gameModel;

        disableInput();
    }

    //Init the baord panel
    private void initializeBoardPanel() {
        int boardSize;

        //Get the board size
        if (gameModel == null) {
            boardSize = Integer.parseInt(configuration.getProperty(ReversiGameConfiguration.BOARD_SIZE, "8"));
        } else {
            boardSize = gameModel.getGamePosition().getBoard().getBoardWidth();
        }

        //Create the buttons
        squares = new ButtonCircle[boardSize][boardSize];
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                ButtonCircle button = new ButtonCircle();
                button.setOnMouseClicked(userInputsMove);
                button.setUserData(x + " " + y);

                boardPane.add(button, x, y);

                squares[x][y] = button;
            }
        }

        //this adds the right style class to the button
        updateShowLegalMoves();
    }

    private void initializeGameInformationPanel() {
        initializePlayerIcons();
    }

    private void initializePlayerIcons() {
        //Load the string values of the colors of the players
        String colorBlack = configuration.getProperty(ReversiGameConfiguration.PLAYER_BLACK_COLOR, "0xFF0000");
        String colorWhite = configuration.getProperty(ReversiGameConfiguration.PLAYER_WHITE_COLOR, "0xFFFF00");

        Color blackColor = Color.valueOf(colorBlack);
        Color whiteColor = Color.valueOf(colorWhite);

        blackPlayer.fillProperty().setValue(blackColor);
        whitePlayer.fillProperty().setValue(whiteColor);
    }

    @FXML
    private void buttonTakeBackClicked(ActionEvent event) {
        //disable input even when it would not make any sense the actor will ask for legal moves
        disableInput();

        //take the move back
        gameModel.takeBackMove();
    }

    @FXML
    private void buttonStartNewGameClicked(ActionEvent event) {
        //input no longer needed
        disableInput();

        //start a new game
        gameModel.startNewGame();

        //reload board because of animations
        initializeBoardPanel();
        //play the new game
        gameModel.play();
    }

    @FXML
    private void buttonResignClicked(ActionEvent event) {
        //input no longer needed
        disableInput();

        if (gameModel.hasGameAnyLegalMoves()) {
            gameModel.endGame();
        }
    }

    @FXML
    private void buttonEndGameClicked(ActionEvent event) {
        if (gameModel.hasGameAnyLegalMoves()) {
            gameModel.endGame();
        }
        //input no longer needed
        disableInput();

        //end the application
        Platform.exit();
    }

    @FXML
    private void showSettingsClicked(ActionEvent event) {
        FxGamePreferences preferences = new FxGamePreferences();
        if (preferences.hasConfigurationChanged()) {
            initializePlayerIcons();

            updateBoard();
            updateCurrentPlayer();
            updateShowAnimations();
            updateShowLegalMoves();
        }
    }


    void update() {
        //updates the board
        updateBoard();
        //updates the game information panel
        updateGameInformation();
    }

    private void updateBoard() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Get the current board
                Board board = gameModel.getGamePosition().getBoard();

                //iterate over the board
                for (int x = 0; x < board.getBoardWidth(); x++) {
                    for (int y = 0; y < board.getBoardHeight(); y++) {
                        //get the square state from the board
                        Square square = board.getSquare(x, y);
                        SquareState state = square.getSquareState();

                        //add the corresponding icon to the square
                        if (state == SquareState.WHITE) {
                            squares[x][y].setPlayerIcon(whitePlayer.getFill());
                        } else if (state == SquareState.BLACK) {
                            squares[x][y].setPlayerIcon(blackPlayer.getFill());
                        } else {
                            squares[x][y].removePlayerIcon();
                        }
                    }
                }
            }
        });
    }

    private void updateGameInformation() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Get the current game position
                GamePosition gamePosition = gameModel.getGamePosition();

                updateCurrentPlayer();

                //get the internationalized format String for the number of disks
                String format = resources.getString("ui.label.disk.number");

                int numberWhiteDisks = gamePosition.getBoard().countPieces(Player.WHITE);
                int numberBlackDisks = gamePosition.getBoard().countPieces(Player.BLACK);

                labelBlackPlayerDisks.setText(String.format(format, numberBlackDisks));
                labelWhitePlayerDisks.setText(String.format(format, numberWhiteDisks));
            }
        });
    }

    private void updateCurrentPlayer() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Get the current game position
                GamePosition gamePosition = gameModel.getGamePosition();

                //set the label which displays whose turn it is
                if (gamePosition.getCurrentPlayer() == Player.BLACK) {
                    currentPlayer.fillProperty().setValue(blackPlayer.getFill());
                } else {
                    currentPlayer.fillProperty().setValue(whitePlayer.getFill());
                }
            }
        });
    }

    private void updateShowAnimations() {
        String showAnimations = configuration.getProperty(
                ReversiGameConfiguration.USER_INTERFACE_SHOW_ANIMATIONS, "true");

        this.showAnimations = Boolean.parseBoolean(showAnimations);
    }

    private void updateShowLegalMoves() {
        String sShowLegalMoves = configuration.getProperty(
                ReversiGameConfiguration.USER_INTERFACE_SHOW_LEGAL_MOVES, "true");

        boolean showLegalMoves = Boolean.parseBoolean(sShowLegalMoves);

        String styleClass = showLegalMoves ? "board-squares" : "board-squares-hidden";
        String removeStyleClass = showLegalMoves ? "board-squares-hidden" : "board-squares";

        for (int x = 0; x < squares.length; x++) {
            for (int y = 0; y < squares.length; y++) {
                Button button = squares[x][y];
                button.getStyleClass().removeAll(removeStyleClass);
                button.getStyleClass().add(styleClass);
            }
        }
    }

    private void disableInput() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int x = 0; x < squares.length; x++) {
                    for (int y = 0; y < squares[x].length; y++) {
                        if (!squares[x][y].isDisabled()) {
                            squares[x][y].setDisable(true);
                        }
                    }
                }
            }
        });
    }

    private void showAllLegalMoves() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Board board = gameModel.getGamePosition().getBoard();
                Player currentPlayer = gameModel.getGamePosition().getCurrentPlayer();

                List<GameMove> legalMoves = board.getAllLegalMoves(currentPlayer);
                for (GameMove move : legalMoves) {
                    //get the square
                    Square square = move.getSquare();
                    //get the x and y position of the square
                    int xPos = square.getXPosition();
                    int yPos = square.getYPosition();

                    //enable the square
                    squares[xPos][yPos].setDisable(false);
                }
            }
        });
    }

    /**
     * This method is implemented so that the controller can act as HumanActable
     *
     * @throws InterruptedException if teh game is interrupted whilst requesting an input
     */
    @Override
    public final void requestUserInput() throws InterruptedException {
        showAllLegalMoves();

        resultsReady.await();
    }

    /**
     * This method allows the controller to display the messages send by HumanActable to the user.
     *
     * @param message The message that should be displayed on the screen.
     */
    @Override
    public void sendMessage(String message) {
        //This functionality is not used...
    }

    /**
     * This method allows the controller to pass the GameMove back to the HumanActable.
     *
     * @return The requested GameMove.
     */
    @Override
    public final GameMove getUserInput() {
        return convertUserInput(userInput);
    }

    private GameMove convertUserInput(String userInput) {
        //if user input is null it can not be converted to a game move
        if (userInput == null) {
            return null;
        }

        //trim the input
        userInput = userInput.trim().toLowerCase();

        //split the user input
        String[] coordinates = userInput.split(" ");

        //convert to board coordinates
        int yPos = Integer.parseInt(coordinates[1]);
        int xPos = Integer.parseInt(coordinates[0]);

        //Get the square from the board and create a game move
        Square square = gameModel.getGamePosition().getBoard().getSquare(xPos, yPos);

        return new GameMove(square);
    }

    private class ButtonCircle extends Button {
        private Circle player;

        ButtonCircle() {
            player = new Circle(16);
            player.getStyleClass().add("circle");

            //Set Properties is not possible in JavaFX 2.2
            setMinHeight(50);
            setMinWidth(50);
        }

        void setPlayerIcon(Paint value) {
            if (getGraphic() == null) {
                setGraphic(player);
                player.setFill(value);

                if (showAnimations) {
                    FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), player);
                    fadeTransition.setFromValue(0);
                    fadeTransition.setToValue(1);

                    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), player);
                    scaleTransition.setFromX(0);
                    scaleTransition.setFromY(0);
                    scaleTransition.setToX(1);
                    scaleTransition.setToY(1);

                    fadeTransition.play();
                    scaleTransition.play();
                }
            } else {
                if (showAnimations) {
                    Color start = (Color) player.getFill();
                    Color end = (Color) value;
                    FillTransition fillTransition = new FillTransition(Duration.millis(1000), player, start, end);
                    fillTransition.play();
                } else {
                    player.setFill(value);
                }
            }
        }

        void removePlayerIcon() {
            if (getGraphic() != null) {
                if (showAnimations) {
                    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000), player);
                    scaleTransition.setToX(0);
                    scaleTransition.setToY(0);

                    scaleTransition.play();

                    scaleTransition.setOnFinished(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            setGraphic(null);
                        }
                    });
                }
            } else {
                setGraphic(null);
            }
        }
    }
}
