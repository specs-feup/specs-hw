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

package pt.up.fe.specs.binarytranslation.legacy;

import java.io.Closeable;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.InstructionStreamType;
import pt.up.fe.specs.util.asm.processor.RegisterTable;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * Represents a trace.
 * 
 * <p>
 * Delivers instructions and the corresponding instruction address.
 * 
 * @author Joao Bispo
 */
public interface TraceInstructionStream extends Closeable, InstructionStream {

    @Override
    default InstructionStreamType getType() {
        return InstructionStreamType.TRACE;
    }

    /**
     * 
     * @return the next instruction in the trace, or null if it reached the end
     */
    default Instruction nextTraceInstruction() {
        return this.nextInstruction();
    };

    /**
     * 
     * @return the total number of returned instructions up to this moment
     */
    @Override
    default long getNumInstructions() {
        return 0;
    }

    /**
     * 
     * @return the total number of cycles up to this moment
     */
    @Override
    default long getCycles() {
        return 0;
    }

    /**
     * 
     * @return
     */
    @Override
    default int getInstructionWidth() {
        return 0;
    }

    /**
     * 
     * @return True if stream has another line
     */
    @Override
    default boolean hasNext() {
        return false;
    }

    /**
     * Optional: returns the values of the registers at the current moment.
     * 
     * @return null if not implemented.
     */
    default RegisterTable getRegisters() {
        return null;
    }

    @Override
    default Instruction nextInstruction() {
        return nextTraceInstruction();
    }

    @Override
    default void close() {
        // Do nothing, do not throw Exception
    }

    @Override
    default Application getApplicationInformation() {
        throw new NotImplementedException(this);
    }

    @Override
    default void rawDump() {
        throw new NotImplementedException(this);
    }

    @Override
    default boolean isClosed() {
        throw new NotImplementedException(this);
    }
}
