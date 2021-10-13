/**
 * Copyright 2021 SPeCS.
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
 
package pt.up.fe.specs.binarytranslation.producer;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.threadstream.ObjectProducer;

public interface InstructionProducer extends ObjectProducer<Instruction> {

    /**
     * Advance to a given address (basically used to set a gdb breakpoint and run), returns false if the producer does
     * not implement this feature
     */
    default boolean runUntil(long addr) {
        return false;
    }

    /**
     * Advance to a given symbol name (basically used to set a gdb breakpoint and run), returns false if the producer
     * does not implement this feature
     */
    default boolean runUntil(String namedTarget) {
        return false;
    }

    /**
     * 
     * @return the next instruction of the stream, or null if there are no more instructions in the stream
     */
    public Instruction nextInstruction();

    // TODO: add a configuration for a no-Register run?

    /**
     * Only implementable by a @TraceInstructionProducer which is executing a simulator @ProcessRun
     * 
     * default public RegisterDump getRegisters() { return null; }
     */

    /**
     * 
     * @return
     */
    public Integer getInstructionWidth();

    /**
     * Outputs the unprocessed incoming stream
     */
    public void rawDump();

    /**
     * 
     * @return The final {@ApplicationInformation} object containing appname, compile info, and cpu architecture
     */
    public Application getApp();
}
