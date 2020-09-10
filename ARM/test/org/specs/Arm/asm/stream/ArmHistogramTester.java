package org.specs.Arm.asm.stream;

import java.io.File;

import org.junit.Test;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.stream.profiler.InstructionTypeHistogram;
import pt.up.fe.specs.util.SpecsIo;

public class ArmHistogramTester {

    public void testArmHistogram(String elfname) {
        File fd = SpecsIo.resourceCopy(elfname);
        fd.deleteOnExit();
        var istream = new ArmTraceStream(fd);
        var profiler = new InstructionTypeHistogram(istream);
        var result = profiler.profile();
        result.toJSON();
    }

    @Test
    public void singleHistogram() {
        this.testArmHistogram("org/specs/Arm/asm/cholesky.elf");
    }

    @Test
    public void ArmHistogramBatch() {
        this.testArmHistogram("org/specs/Arm/asm/cholesky.elf");
        this.testArmHistogram("org/specs/Arm/asm/diffpredict.elf");
        this.testArmHistogram("org/specs/Arm/asm/glinearrec.elf");
        this.testArmHistogram("org/specs/Arm/asm/hydro.elf");
        this.testArmHistogram("org/specs/Arm/asm/hydro2d.elf");
        this.testArmHistogram("org/specs/Arm/asm/innerprod.elf");
        this.testArmHistogram("org/specs/Arm/asm/matmul.elf");
    }
}
