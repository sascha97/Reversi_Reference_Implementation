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

package reversi.actor;

import java.util.ResourceBundle;

/**
 * This enum contains all computer actors implemented in this program
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 24. July 2016
 */
public enum ComputerActors {
    ALPHA_BETA_ACTOR("computer.actor.alpha.beta", new AlphaBetaActor()),
    ALPHA_BETA_ACTOR2("computer.actor.alpha.beta2", new AlphaBetaActor2()),
    MINI_MAX_ACTOR("computer.actor.mini.max", new MiniMaxActor()),
    RANDOM_ACTOR("computer.actor.random", new RandomActor());

    private final static ResourceBundle RES = ResourceBundle.getBundle("strings/Values");

    private final String resKey;
    private final ComputerActor computerActor;

    ComputerActors(String resKey, ComputerActor computerActor) {
        this.resKey = resKey;
        this.computerActor = computerActor;
    }

    public static ComputerActor getComputerActor(String name) {
        for (ComputerActors actor : ComputerActors.values()) {
            if (actor.name().equals(name)) {
                return actor.getComputerActor();
            }
        }

        return null;
    }

    /**
     * This method is used to get the computer actor from the enumeration
     *
     * @return The computer actor of the enum field.
     */
    public ComputerActor getComputerActor() {
        return computerActor;
    }

    /**
     * Gets the name defined in the properties file for displaying it.
     *
     * @return The name of the actor
     */
    @Override
    public String toString() {
        return RES.getString(resKey);
    }
}
