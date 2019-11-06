package org.specs.Arm.asm;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.StaticBasicBlockDetector;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.SpecsIo;

public class ArmStaticBasicBlockDetectorTester {

    @Test
    public void test() {
        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/test/dump.txt");
        fd.deleteOnExit();

        try (ArmElfStream el = new ArmElfStream(fd)) {
            var bbd = new StaticBasicBlockDetector(el);
            List<BinarySegment> bblist = bbd.detectSegments();

            for (BinarySegment bs : bblist) {
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
