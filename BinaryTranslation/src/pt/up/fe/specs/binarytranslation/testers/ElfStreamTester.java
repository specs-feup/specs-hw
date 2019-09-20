package pt.up.fe.specs.binarytranslation.testers;

import orgs.specs.MicroBlaze.asm.MicroBlazeElfStream;
import pt.up.fe.specs.binarytranslation.Instruction;

public class ElfStreamTester {

    public static void main(String[] args) {

        MicroBlazeElfStream el1 = new MicroBlazeElfStream("test.elf");
        Instruction i1 = el1.nextInstruction();
    }
}
