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
    private final String name;

    protected Actor(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract Strategy getStrategy();
}
