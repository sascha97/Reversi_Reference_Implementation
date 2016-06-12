/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.game;

import reversi.actor.Actor;
import reversi.actor.HumanActor;
import reversi.board.GamePosition;
import reversi.board.ReversiBoard;
import reversi.player.ActorsPair;
import reversi.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 12. June 2016
 */
public class ReversiGame extends Game {
    private GamePosition currentGamePosition;

    private List<GamePosition> gamePositionList;

    private ReversiGameConfiguration config = ReversiGameConfiguration.getInstance();
    private Player humanPlayer;

    public ReversiGame(ActorsPair actorsPair) {
        super(actorsPair);

        gamePositionList = new ArrayList<>();

        String humanColor = config.getProperty(ReversiGameConfiguration.HUMAN_PLAYER_COLOR, "black");

        Actor whiteActor = actorsPair.getActor(Player.WHITE);
        Actor blackActor = actorsPair.getActor(Player.BLACK);

        Actor humanActor;
        Actor computerActor;

        if (whiteActor instanceof HumanActor) {
            humanActor = whiteActor;
            computerActor = blackActor;
        } else {
            humanActor = blackActor;
            computerActor = whiteActor;
        }

        if (humanColor.equals("black")) {
            actorsPair.setActor(Player.BLACK, humanActor);
            actorsPair.setActor(Player.WHITE, computerActor);
            humanPlayer = Player.BLACK;
        } else {
            actorsPair.setActor(Player.WHITE, humanActor);
            actorsPair.setActor(Player.BLACK, computerActor);
            humanPlayer = Player.WHITE;
        }

        startNewGame();
    }

    @Override
    public GamePosition getGamePosition() {
        return currentGamePosition;
    }

    @Override
    protected void setGamePosition(GamePosition gamePosition) {
        this.currentGamePosition = gamePosition;
        if (!gamePositionList.contains(gamePosition)) {
            this.gamePositionList.add(gamePosition);
        }
    }

    @Override
    public void startNewGame() {
        super.startNewGame();

        ReversiBoard reversiBoard = new ReversiBoard();
        GamePosition startPosition = new GamePosition(reversiBoard, Player.BLACK);

        this.currentGamePosition = startPosition;
        gamePositionList.clear();
        gamePositionList.add(this.currentGamePosition);

        super.gameHasChanged();
    }

    @Override
    public void takeBackMove() {
        if (this.gamePositionList.size() > 2) {
            super.takeBackMove();

            this.gamePositionList.remove(gamePositionList.size() - 1);
            this.gamePositionList.remove(gamePositionList.size() - 1);

            GamePosition currentGamePosition = this.gamePositionList.get(gamePositionList.size() - 1);

            setGamePosition(currentGamePosition);

            super.play();
            super.gameHasChanged();
        }
    }
}
