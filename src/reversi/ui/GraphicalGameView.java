/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Q11_Reversi".
 * <p>
 * The distribution or use of this project is prohibited.
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
import javax.swing.JPanel;
import javax.swing.SwingConstants;
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
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 17. May 2016
 */
public class GraphicalGameView extends GameView {
    private JFrame frame;
    private JPanel boardPanel;
    private JPanel gameInformationPanel;

    private GridBagLayout gridBagLayout;

    private JButton[][] squares;

    private PlayerIcon iconWhitePlayer = new PlayerIcon(Color.YELLOW);
    private PlayerIcon iconBlackPlayer = new PlayerIcon(Color.RED);

    private JLabel labelWhoseTurn;
    private JLabel labelWhitePlayerDisks;
    private JLabel labelBlackPlayerDisks;

    private final int BOARD_WIDTH;
    private final int BOARD_HEIGHT;
    private final Dimension DIMENSION = new Dimension(60, 60);

    private ThreadEvent resultsReady = new ThreadEvent();
    private String userInput;

    public GraphicalGameView(Game gameModel) {
        super(gameModel);

        ReversiGameConfiguration config = ReversiGameConfiguration.getInstance();

        BOARD_WIDTH = Integer.parseInt(config.getProperty(ReversiGameConfiguration.BOARD_SIZE, "8"));
        BOARD_HEIGHT = Integer.parseInt(config.getProperty(ReversiGameConfiguration.BOARD_SIZE, "8"));

        squares = new JButton[BOARD_WIDTH][BOARD_HEIGHT];

        initializeBoardPanel();
        initializeGameInformationPanel();

        frame = new JFrame(RES.getString("ui.window.title"));

        JPanel root = new JPanel(new BorderLayout());
        root.add(gameInformationPanel, BorderLayout.LINE_END);
        root.add(boardPanel, BorderLayout.CENTER);

        frame.setContentPane(root);
    }

    private void initializeBoardPanel() {
        gridBagLayout = new GridBagLayout();
        boardPanel = new JPanel(gridBagLayout);

        GridBagConstraints constraints = new GridBagConstraints();

        //SET VERTICAL NUMBERS
        for (int x = 0; x < BOARD_WIDTH + 2; x += BOARD_WIDTH + 1) {
            constraints.gridx = x;
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                constraints.gridy = y + 1;

                int number = 1 + y;

                JLabel label = new JLabel("" + number, SwingConstants.CENTER);
                label.setPreferredSize(DIMENSION);

                boardPanel.add(label, constraints);
            }
        }

        //SET HORIZONTAL LETTERS
        for (int y = 0; y < BOARD_HEIGHT + 2; y += BOARD_HEIGHT + 1) {
            constraints.gridy = y;
            for (int x = 0; x < BOARD_WIDTH; x++) {
                constraints.gridx = x + 1;

                char letter = (char) ('A' + x);

                JLabel label = new JLabel("" + letter, SwingConstants.CENTER);
                label.setPreferredSize(DIMENSION);

                boardPanel.add(label, constraints);
            }
        }

        //SET BOARD SQUARES
        for (int x = 1; x <= BOARD_WIDTH; x++) {
            for (int y = 1; y <= BOARD_HEIGHT; y++) {
                constraints.gridx = x;
                constraints.gridy = y;

                squares[x - 1][y - 1] = new JButton();
                JButton button = squares[x - 1][y - 1];
                button.setPreferredSize(DIMENSION);
                button.addActionListener(userInputsMove);
                button.setEnabled(false);
                button.setForeground(Color.GREEN);

                boardPanel.add(button, constraints);
            }
        }

        boardPanel.setBackground(Color.LIGHT_GRAY);
    }

    private void initializeGameInformationPanel() {
        Color color = new Color(200, 200, 200);

        gameInformationPanel = new JPanel(new GridBagLayout());
        gameInformationPanel.setBackground(color);
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
        gamePanel.setBackground(color);

        JPanel currentPlayer = new JPanel();
        currentPlayer.setBackground(color);
        currentPlayer.setBorder(BorderFactory.createTitledBorder(RES.getString("ui.label.current.player")));

        labelWhoseTurn = new JLabel(iconWhitePlayer, SwingConstants.CENTER);
        labelWhoseTurn.setPreferredSize(new Dimension(60, 60));
        currentPlayer.add(labelWhoseTurn);

        JPanel panelPlayerStatus = new JPanel(new GridBagLayout());
        panelPlayerStatus.setBackground(color);
        GridBagConstraints constraintsPlayer = new GridBagConstraints();
        panelPlayerStatus.setBorder(BorderFactory.createTitledBorder(RES.getString("ui.label.status.player")));

        constraintsPlayer.gridx = 0;
        constraintsPlayer.gridy = 0;
        constraintsPlayer.weightx = 0.4;
        constraintsPlayer.ipady = 10;
        JLabel labelWhitePlayer = new JLabel(iconWhitePlayer, SwingConstants.CENTER);
        labelWhitePlayer.setPreferredSize(new Dimension(60, 60));
        panelPlayerStatus.add(labelWhitePlayer, constraintsPlayer);

        constraintsPlayer.gridx = 1;
        constraintsPlayer.weightx = 0.6;
        labelWhitePlayerDisks = new JLabel("", SwingConstants.CENTER);
        panelPlayerStatus.add(labelWhitePlayerDisks, constraintsPlayer);

        constraintsPlayer.gridx = 0;
        constraintsPlayer.gridy = 1;
        constraintsPlayer.weightx = 0.4;
        JLabel labelBlackPlayer = new JLabel(iconBlackPlayer, SwingConstants.CENTER);
        labelWhitePlayer.setPreferredSize(new Dimension(60, 60));
        panelPlayerStatus.add(labelBlackPlayer, constraintsPlayer);

        constraintsPlayer.gridx = 1;
        constraintsPlayer.weightx = 0.6;
        labelBlackPlayerDisks = new JLabel("", SwingConstants.CENTER);
        panelPlayerStatus.add(labelBlackPlayerDisks, constraintsPlayer);

        gamePanel.add(currentPlayer);
        gamePanel.add(panelPlayerStatus);
        gameInformationPanel.add(gamePanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipady = 25;
        constraints.ipadx = 40;
        //Everything needed for the GameControl Panel will be put here
        JPanel menuPanel = new JPanel(new GridLayout(2, 2));
        menuPanel.setBorder(BorderFactory.createTitledBorder(RES.getString("ui.label.menu")));
        menuPanel.setBackground(color);

        JButton buttonResign = new JButton(RES.getString("ui.label.resign"));
        buttonResign.setEnabled(false);
        JButton buttonTakeBack = new JButton(RES.getString("ui.label.take.back"));
        JButton buttonNewGame = new JButton(RES.getString("ui.label.new.game"));
        JButton buttonExit = new JButton(RES.getString("ui.label.exit"));

        //TODO MOVE OUT HERE
        buttonTakeBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Disable input even when it would not make any sense
                disableInput();
                gameModel.takeBackMove();
            }
        });

        //TODO MOVE OUT HERE
        buttonNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disableInput();
                gameModel.startNewGame();
                gameModel.play();
            }
        });

        //TODO: move out here
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameModel.endGame();
                //Input no longer needed
                disableInput();
            }
        });

        menuPanel.add(buttonResign);
        menuPanel.add(buttonTakeBack);
        menuPanel.add(buttonNewGame);
        menuPanel.add(buttonExit);

        gameInformationPanel.add(menuPanel, constraints);
        gameInformationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    @Override
    public void show() {
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setLocationByPlatform(true);

        frame.setVisible(true);

        super.show();
    }

    @Override
    protected String requestUserInput() throws InterruptedException {
        resultsReady.await();

        return userInput;
    }

    @Override
    protected void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    protected void showAllLegalMoves() {
        Board board = gameModel.getGamePosition().getBoard();
        Player currentPlayer = gameModel.getGamePosition().getCurrentPlayer();

        List<GameMove> legalMoves = board.getAllLegalMoves(currentPlayer);
        for (GameMove move : legalMoves) {
            Square square = move.getSquare();
            int xPos = square.getXPosition();
            int yPos = square.getYPosition();

            squares[xPos][yPos].setEnabled(true);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        updateBoard();
        updateGameInformation();
    }

    private void updateBoard() {
        Board board = gameModel.getGamePosition().getBoard();

        for (int x = 0; x < board.getBoardWidth(); x++) {
            for (int y = 0; y < board.getBoardHeight(); y++) {
                squares[x][y].setIcon(null);

                Square square = board.getSquare(x, y);
                SquareState state = square.getSquareState();

                if (state == SquareState.WHITE) {
                    squares[x][y].setIcon(iconWhitePlayer);
                } else if (state == SquareState.BLACK) {
                    squares[x][y].setIcon(iconBlackPlayer);
                }
            }
        }
    }

    private void updateGameInformation() {
        GamePosition gamePosition = gameModel.getGamePosition();

        if (gamePosition.getCurrentPlayer() == Player.BLACK) {
            labelWhoseTurn.setIcon(iconBlackPlayer);
        } else {
            labelWhoseTurn.setIcon(iconWhitePlayer);
        }

        String format = RES.getString("ui.number.disks");

        int numberWhiteDisks = gamePosition.getBoard().countPieces(Player.WHITE);
        int numberBlackDisks = gamePosition.getBoard().countPieces(Player.BLACK);

        labelBlackPlayerDisks.setText(String.format(format, numberBlackDisks));
        labelWhitePlayerDisks.setText(String.format(format, numberWhiteDisks));
    }

    private void disableInput() {
        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                JButton button = squares[x][y];
                if (button.isEnabled()) {
                    button.setEnabled(false);
                }
            }
        }
    }

    private ActionListener userInputsMove = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = ((JButton) e.getSource());

            GridBagConstraints constraints = gridBagLayout.getConstraints(button);

            int xPos = constraints.gridx - 1; //Weil um 1 Verschoben
            int yPos = constraints.gridy - 1; //Weil um 1 Verschoben

            userInput = xPos + "" + yPos;
            disableInput();

            resultsReady.signal();
        }
    };

    private static class PlayerIcon implements Icon {
        private Color color;

        PlayerIcon(Color color) {
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.drawOval(x, y, getIconWidth(), getIconHeight());
            g.fillOval(x, y, getIconWidth(), getIconHeight());
        }

        @Override
        public int getIconWidth() {
            return 40;
        }

        @Override
        public int getIconHeight() {
            return 40;
        }
    }

    private static class ThreadEvent {
        private final Object lock = new Object();

        void await() throws InterruptedException {
            synchronized (lock) {
                lock.wait();
            }
        }

        void signal() {
            synchronized (lock) {
                lock.notify();
            }
        }
    }
}
