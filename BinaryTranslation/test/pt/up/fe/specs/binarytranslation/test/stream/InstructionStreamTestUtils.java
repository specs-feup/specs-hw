package pt.up.fe.specs.binarytranslation.test.stream;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class InstructionStreamTestUtils {

    public static void printStream(InstructionStream el) {
        // el.advanceTo(el.getApp().getELFProvider().getKernelStart().longValue());
        // el.advanceTo(0x1600);
        try {
            Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                inst.printInstruction();
            }
            el.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void rawDump(InstructionStream el) {
        try {
            el.rawDump();
            el.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
