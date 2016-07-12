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
