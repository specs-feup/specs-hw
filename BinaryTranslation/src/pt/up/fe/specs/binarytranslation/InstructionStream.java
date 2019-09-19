package pt.up.fe.specs.binarytranslation;

import java.io.Closeable;

public interface InstructionStream extends Closeable {

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
}
