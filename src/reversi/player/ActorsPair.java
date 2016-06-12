/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.player;

import reversi.actor.Actor;

import java.util.EnumMap;
import java.util.Map;

/**
 * Add a description here...
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public class ActorsPair {
    private final Map<Player, Actor> actors;

    public ActorsPair(Map<Player, Actor> actors) {
        this.actors = actors;
    }

    public ActorsPair(Actor whitePlayer, Actor blackPlayer) {
        actors = new EnumMap<Player, Actor>(Player.class);
        actors.put(Player.WHITE, whitePlayer);
        actors.put(Player.BLACK, blackPlayer);
    }

    public void setActor(Player player, Actor actor) {
        actors.put(player, actor);
    }

    public Actor getActor(Player player) {
        return actors.get(player);
    }
}
