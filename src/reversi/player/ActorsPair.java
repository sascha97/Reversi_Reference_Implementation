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
package reversi.player;

import reversi.actor.Actor;
import reversi.actor.HumanActor;

import java.util.EnumMap;
import java.util.Map;

/**
 * This class is responsible for mapping an Actor to an player.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public class ActorsPair {
    private final Map<Player, Actor> actors;

    /**
     * Constructor for the ActorsPair.
     *
     * @param actors An map where the player is already mapped to the actor.
     */
    public ActorsPair(Map<Player, Actor> actors) {
        this.actors = actors;
    }

    /**
     * Constructor for the ActorsPair.
     *
     * @param whitePlayer the actor playing white.
     * @param blackPlayer the actor playing black.
     */
    public ActorsPair(Actor whitePlayer, Actor blackPlayer) {
        //Create a new enum map and map the actors to the corresponding player.
        actors = new EnumMap<>(Player.class);
        actors.put(Player.WHITE, whitePlayer);
        actors.put(Player.BLACK, blackPlayer);
    }

    /**
     * This method sets the human player to the given color.
     *
     * @param player The color of the human player.
     */
    public void setHumanPlayer(Player player) {
        Actor whiteActor = getActor(Player.WHITE);
        Actor blackActor = getActor(Player.BLACK);

        Actor humanActor;
        Actor computerActor;

        if (whiteActor instanceof HumanActor) {
            humanActor = whiteActor;
            computerActor = blackActor;
        } else {
            humanActor = blackActor;
            computerActor = whiteActor;
        }

        setActor(player, humanActor);
        setActor(player.getOpponent(), computerActor);
    }

    public void refreshAllActors() {
        for (Actor actor : actors.values()) {
            actor.refreshActor();
        }
    }

    /**
     * This method sets an actor to a given player.
     *
     * @param player The player whose actor should be changed.
     * @param actor  The actor who should be mapped to the player.
     */
    private void setActor(Player player, Actor actor) {
        actors.put(player, actor);
    }

    /**
     * This method returns the mapped actor to a given player.
     *
     * @param player The player whose actor should be returned.
     *
     * @return The Actor of the player.
     */
    public Actor getActor(Player player) {
        return actors.get(player);
    }

    @Override
    public String toString() {
        return "ActorsPair{" +
                "actors=" + actors +
                '}';
    }
}
