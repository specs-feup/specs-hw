/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.binarytranslation.stream;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.threadstream.ObjectStream;

/**
 * Stream of instructions. The source can be either finite (e.g., ELF file) or potentially infinite (e.g., trace). It
 * can also already exist completely (e.g., file with instructions) or is being generated (e.g., simulator).
 * 
 * @author JoaoBispo
 *
 */
public interface InstructionStream extends ObjectStream<Instruction> {

    /**
     * 
     * @return the next instruction of the stream, or null if there are no more instructions in the stream
     */
    Instruction nextInstruction();

    /**
     * 
     * @return the type of instruction stream.
     */
    InstructionStreamType getType();

    /**
     * 
     * @return
     */
    Integer getInstructionWidth();

    /**
     * 
     * @param isSilen
     */
    public void silent(boolean isSilent);

    /**
     * 
     * @return The final {@ApplicationInformation} object containing appname, compile info, and cpu architecture
     */
    public Application getApp();

    /**
     * Outputs the unprocessed incoming stream
     */
    public void rawDump();

    /**
     * 
     * @return Total number of returned instructions up to this moment
     */
    default long getNumInstructions() {
        return 0;
    }

    /**
     * 
     * @return Total number of cycles up to this moment
     */
    default long getCycles() {
        return 0;
    }
}
