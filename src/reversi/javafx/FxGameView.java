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

import reversi.actor.Actor;
import reversi.actor.HumanActor;
import reversi.game.Game;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * This class is used for displaying the board to the user.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 23. July 2016
 */
class FxGameView implements Observer {
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("strings/Values");

    private Stage window;
    private Game gameModel;
    private FxGameViewController controller;

    FxGameView(Stage window, Game gameModel, Actor actor) {
        this.window = window;
        this.gameModel = gameModel;

        gameModel.addObserver(this);

        initializeWindow(actor);
    }

    private void initializeWindow(Actor actor) {
        FXMLLoader fxmlLoader = new FXMLLoader(FxGameView.class.getResource("/layout/GameView.fxml"));
        fxmlLoader.setResources(resourceBundle);

        try {
            Parent parent = fxmlLoader.load();
            controller = fxmlLoader.getController();
            controller.setGameModel(gameModel);

            Scene scene = new Scene(parent);
            window.setScene(scene);

            window.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    gameModel.endGame();
                }
            });

            window.setResizable(false);

            setTitle(actor);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Actor) {
            setTitle((Actor) arg);
        } else {
            controller.update();
        }
    }

    void show() {
        window.show();
        gameModel.play();
    }

    HumanActor.HumanActable getHumanActable() {
        return controller;
    }

    private void setTitle(Actor actor) {
        window.setTitle(resourceBundle.getString("ui.window.title") + " - " + actor.getName());
    }

}
