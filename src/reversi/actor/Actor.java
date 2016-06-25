/**
 * (C) Copyright 2016 Sascha Lutzenberger
 * <p>
 * This file is part of the project "Reversi_Reference_Implementation".
 * <p>
 * The distribution or use of this project is prohibited.
 */
package reversi.actor;

/**
 * Add a description here...
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
    protected Actor(String name) {
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
}
