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

package reversi.evaluation;

import java.util.ResourceBundle;

/**
 * This enum holds all available evaluations of the game.
 *
 * @author Sascha Lutzenberger
 * @version 1.0 - 25. July 2016
 */
public enum Evaluations {
    CHANGING_EVALUATION("evaluation.changing", new ChangingEvaluation()),
    COUNT_DIFFERENCE_EVALUATION("evaluation.count.difference", new CountDifferenceEvaluation()),
    FRONT_TILE_EVALUATION("evaluation.front.tile", new FrontTileEvaluation()),
    MIXED_EVALUATION("evaluation.mixed", new MixedEvaluation()),
    POSITIONAL_EVALUATION("evaluation.positional", new PositionalEvaluation());

    private final static ResourceBundle RES = ResourceBundle.getBundle("strings/Values");

    private final String resKey;
    private final Evaluation evaluation;

    Evaluations(String resKey, Evaluation evaluation) {
        this.resKey = resKey;
        this.evaluation = evaluation;
    }

    public static Evaluation getEvaluation(String name) {
        for (Evaluations evaluation : Evaluations.values()) {
            if (evaluation.name().equals(name)) {
                return evaluation.getEvaluation();
            }
        }

        return null;
    }

    /**
     * This method is used to get the evaluation from the enumeration.
     *
     * @return The evaluation of the enum field.
     */
    public Evaluation getEvaluation() {
        return this.evaluation;
    }

    /**
     * Gets teh name defined in the properties file for displaying it.
     *
     * @return The name of the Evaluation
     */
    @Override
    public String toString() {
        return RES.getString(resKey);
    }
}
