package pt.up.fe.specs.binarytranslation;

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
