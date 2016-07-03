/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.actor;

/**
 * This is the base class for any Actor needed in the ReversiGame.
 *
 * An Actor is responsible for making a move in the game.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 22. May 2016
 */
public abstract class Actor {
    //The name of the actor
    private final String name;

    /**
     * Constructor to create a new Actor
     *
     * @param name The name of the Actor
     */
    Actor(String name) {
        this.name = name;
    }

    /**
     * This method returns the name of the Actor.
     *
     * @return The name of the Actor
     */
    public String getName() {
        return this.name;
    }

    /**
     * This method should return the Strategy of the Actor.
     *
     * @return An implementation of the Strategy interface.
     */
    public abstract Strategy getStrategy();

    @Override
    public String toString() {
        return "Actor{" +
                "name='" + name + '\'' +
                '}';
    }
}
