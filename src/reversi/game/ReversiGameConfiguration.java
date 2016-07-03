/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.game;

/**
 * This is the configuration for a ReversiGame.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 21. May 2016
 */
public class ReversiGameConfiguration extends GameConfiguration {
    //All the keys that exist in the ReversiGameConfiguration
    public static final String BOARD_SIZE = "board.size";
    public static final String HUMAN_PLAYER_COLOR = "human.player.color";
    public static final String PLAYER_WHITE_COLOR = "player.white.color";
    public static final String PLAYER_WHITE_CHAR = "player.white.char";
    public static final String PLAYER_BLACK_COLOR = "player.black.color";
    public static final String PLAYER_BLACK_CHAR = "player.black.char";
    public static final String PLAYER_EMPTY_CHAR = "player.empty.char";
    public static final String ALGORITHM_SEARCH_DEPTH = "algorithm.search.depth";

    //Singleton design pattern should be applied here
    private static ReversiGameConfiguration singleton;

    /**
     * Constructor that creates the Configuration.
     */
    private ReversiGameConfiguration() {
        //The file name of the properties file
        super("reversi_game.properties");
    }

    /**
     * This method returns one single instance of the configuration only.
     *
     * @return Returns the only instance of the configuration
     */
    public static ReversiGameConfiguration getInstance() {
        //If there is no instance created create an instance
        if (singleton == null) {
            singleton = new ReversiGameConfiguration();
        }

        //Return the instance of the ReversiGameConfiguration.
        return singleton;
    }
}
