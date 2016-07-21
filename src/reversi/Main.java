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
package reversi;

import reversi.actor.Actor;
import reversi.actor.AlphaBetaActor;
import reversi.actor.HumanActor;
import reversi.actor.MiniMaxActor;
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
