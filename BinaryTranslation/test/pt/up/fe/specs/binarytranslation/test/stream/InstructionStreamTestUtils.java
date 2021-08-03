package pt.up.fe.specs.binarytranslation.test.stream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class InstructionStreamTestUtils {

    public static void printStream(InstructionStream el) {
        Instruction inst = null;
        while ((inst = el.nextInstruction()) != null) {
            inst.printInstruction();
        }
    }

    public static void rawDump(InstructionStream el) {
        el.rawDump();
    }
}
