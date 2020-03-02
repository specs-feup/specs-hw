package org.specs.Arm.asm;

import java.io.File;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.SpecsIo;

public class ArmStaticBasicBlockDetectorTester {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/helloworld64_from_vivado_flow/helloworld64.elf");
        fd.deleteOnExit();

        try (ArmElfStream el = new ArmElfStream(fd)) {
            var bbd = new StaticBasicBlockDetector(el);
            var bundle = bbd.detectSegments();
            var segments = bundle.getSegments();

            for (BinarySegment bs : segments) {
                System.out.print("Basic Block: \n");
                for (Instruction is : bs.getInstructions()) {
                    is.printInstruction();
                }
                System.out.print("\n\n");
            }
        }
        return;
    }

}
