package org.specs.Arm.test.stream;

import org.junit.Test;
import org.specs.Arm.provider.ArmELFProvider;
import org.specs.Arm.provider.ArmLivermoreELFN10;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;

public class ArmHistogramTester {

    public void testInstructionTypeHistogram(ArmELFProvider elfprovider) {
        var istream = new ArmTraceStream(elfprovider);
        var profiler = new InstructionTypeHistogram();
        var result = profiler.profile(istream);
        result.toJSON();
    }

    public void testInstructionHistogram(ArmELFProvider elfprovider) {
        var istream = new ArmTraceStream(elfprovider);
        var profiler = new InstructionHistogram();
        var result = profiler.profile(istream);
        result.toJSON();
    }

    @Test
    public void singleHistogram(ArmELFProvider elfprovider) {
        this.testInstructionTypeHistogram(elfprovider);
        this.testInstructionHistogram(elfprovider);
    }

    @Test
    public void ArmHistogramBatch() {
        for (var elf : ArmLivermoreELFN10.values()) {
            this.singleHistogram(elf);
        }
    }
}
