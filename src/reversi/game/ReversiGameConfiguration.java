/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.game;

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 21. May 2016
 */
public class ReversiGameConfiguration extends GameConfiguration {
    public static final String BOARD_SIZE = "board.size";
    public static final String HUMAN_PLAYER_COLOR = "human.player.color";
    public static final String PLAYER_WHITE_COLOR = "player.white.color";
    public static final String PLAYER_BLACK_COLOR = "player.black.color";
    public static final String ALGORITHM_SEARCH_DEPTH = "algorithm.search.depth";

    private static ReversiGameConfiguration singleton;

    public ReversiGameConfiguration() {
        super("reversi_game.properties");
    }

    public static ReversiGameConfiguration getInstance() {
        if (singleton == null) {
            singleton = new ReversiGameConfiguration();
        }

        return singleton;
    }


}
