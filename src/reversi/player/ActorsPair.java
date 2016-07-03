/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
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
