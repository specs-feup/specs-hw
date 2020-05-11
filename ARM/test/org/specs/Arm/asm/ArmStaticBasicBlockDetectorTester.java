package org.specs.Arm.asm;

import java.io.*;

import org.junit.Test;
import org.specs.Arm.stream.ArmElfStream;

import pt.up.fe.specs.binarytranslation.binarysegments.BinarySegment;
import pt.up.fe.specs.binarytranslation.binarysegments.detection.*;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.SpecsIo;

public class ArmStaticBasicBlockDetectorTester {

    @Test
    public void testToFile() {

        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/cholesky.txt");
        fd.deleteOnExit();

        try (ArmElfStream el = new ArmElfStream(fd)) {
            var bbd = new StaticBasicBlockDetector(el);
            var bundle = bbd.detectSegments();
            try {
                bundle.serializeToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testToConsole() {
        File fd = SpecsIo.resourceCopy("org/specs/Arm/asm/cholesky.txt");
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
