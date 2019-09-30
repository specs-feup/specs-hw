package org.specs.Arm.asm;

import java.io.File;
import java.math.BigInteger;

import org.junit.Test;
import org.specs.Arm.asmparser.ArmInstructionParsers;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.asmparser.IsaParser;
import pt.up.fe.specs.binarytranslation.generic.GenericInstruction;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.utilities.LineStream;

public class ArmElfStreamTester {

    @Test
    public void test() {

        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/test/test.elf");
        fd.deleteOnExit();

        try (ArmElfStream el = new ArmElfStream(fd);) {
            Instruction inst = null;
            while ((inst = el.nextInstruction()) != null) {
                System.out.println(inst);
            }
        }

    }

    @Test
    public void testArmParser() {
        IsaParser parser = ArmInstructionParsers.getArmIsaParser();

        try (var lines = LineStream.newInstance(SpecsIo.resourceToStream("org/specs/Arm/asm/test/asm.txt"), "");) {
            while (lines.hasNextLine()) {
                String asmInstruction = lines.nextLine();
                var inst = new GenericInstruction(0, new BigInteger(asmInstruction, 16));

                var data = parser.parse(inst);
                System.out.println("DATA: " + data);
            }
        }

    }

}
