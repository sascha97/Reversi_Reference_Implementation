/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi;

import reversi.actor.Actor;
import reversi.actor.AlphaBetaActor;
import reversi.actor.HumanActor;
import reversi.game.Game;
import reversi.game.ReversiGame;
import reversi.player.ActorsPair;
import reversi.ui.ConsoleGameController;
import reversi.ui.ConsoleGameView;
import reversi.ui.GameController;
import reversi.ui.GameView;
import reversi.ui.GraphicalGameController;
import reversi.ui.GraphicalGameView;

/**
 * This is the main class setting up the program and connecting the model, view and the controller
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 21. May 2016
 */
public class Main {
    public static void main(String[] args) {
        Actor actor = new AlphaBetaActor();
        HumanActor humanActor = new HumanActor();

        ActorsPair actorsPair = new ActorsPair(actor, humanActor);

        Game gameModel = new ReversiGame(actorsPair);
        GameView view = new GraphicalGameView(gameModel);
        GameController controller = new GraphicalGameController(gameModel);
        view.setGameController(controller);
        controller.setGameView(view);

        humanActor.addHumanActable(controller);
        view.show();
    }
}
