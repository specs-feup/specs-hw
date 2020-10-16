/*
 * Copyright 2011 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
package pt.up.fe.specs.binarytranslation.legacy;

import java.util.Optional;

/**
 * Represents an executed instruction : a SimpleInstructrion and the number of cycles that instruction needed to
 * execute.
 * 
 * @author Joao Bispo
 */
public interface TraceInstruction32 extends SimpleInstruction32 {

    /**
     * How many cycles it takes to execute.
     * 
     * @return
     */
    Integer getCycles();

    // public final Integer instructionCycles;

    /**
     * True if the current instruction can signal a possible exit.
     * 
     * @return
     */
    boolean isJump();

    /**
     * 
     * @return as default, returns empty
     */
    default Optional<Integer> getEncoded() {
        return Optional.empty();
    }
}
