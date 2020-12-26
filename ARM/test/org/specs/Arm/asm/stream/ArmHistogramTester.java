package org.specs.Arm.asm.stream;

import java.io.File;

import org.junit.Test;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.util.SpecsIo;

public class ArmHistogramTester {

    public void testInstructionTypeHistogram(String elfname) {
        File fd = SpecsIo.resourceCopy(elfname);
        fd.deleteOnExit();
        var istream = new ArmTraceStream(fd);
        var profiler = new InstructionTypeHistogram();
        var result = profiler.profile(istream);
        result.toJSON();
    }

    public void testInstructionHistogram(String elfname) {
        File fd = SpecsIo.resourceCopy(elfname);
        fd.deleteOnExit();
        var istream = new ArmTraceStream(fd);
        var profiler = new InstructionHistogram();
        var result = profiler.profile(istream);
        result.toJSON();
    }

    @Test
    public void singleHistogram() {
        this.testInstructionTypeHistogram("org/specs/Arm/asm/cholesky_trace.txt");
        this.testInstructionHistogram("org/specs/Arm/asm/cholesky_trace.txt");
    }
    /*
    @Test
    public void ArmHistogramBatch() {
        this.testArmHistogram("org/specs/Arm/asm/cholesky.elf");
        this.testArmHistogram("org/specs/Arm/asm/diffpredict.elf");
        this.testArmHistogram("org/specs/Arm/asm/glinearrec.elf");
        this.testArmHistogram("org/specs/Arm/asm/hydro.elf");
        this.testArmHistogram("org/specs/Arm/asm/hydro2d.elf");
        this.testArmHistogram("org/specs/Arm/asm/innerprod.elf");
        this.testArmHistogram("org/specs/Arm/asm/matmul.elf");
    }*/
}
