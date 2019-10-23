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

import pt.up.fe.specs.binarytranslation.instruction.Instruction;

/**
 * Stream of instructions. The source can be either finite (e.g., ELF file) or potentially infinite (e.g., trace). It
 * can also already exist completely (e.g., file with instructions) or is being generated (e.g., simulator).
 * 
 * @author JoaoBispo
 *
 */
public interface InstructionStream extends AutoCloseable {

    enum InstructionStreamType {
        TRACE,
        STATIC_ELF;
    }

    /**
     * 
     * @return true if there are still instructions in the stream, false otherwise
     */
    // boolean hasNextInstruction();

    /**
     * 
     * @return the next instruction of the stream, or null if there are no more instructions in the stream
     */
    Instruction nextInstruction();

    /**
     * 
     * 
     * @return the type of instruction stream.
     */
    InstructionStreamType getType();

    int getInstructionWidth();
}
