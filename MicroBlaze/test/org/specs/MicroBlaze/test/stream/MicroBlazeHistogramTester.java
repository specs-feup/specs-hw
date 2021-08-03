package org.specs.MicroBlaze.test.stream;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeELFProvider;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;

public class MicroBlazeHistogramTester {

    public void testMicroBlazeHistogram(MicroBlazeELFProvider elf) {
        var istream = new MicroBlazeTraceStream(elf);
        var profiler = new InstructionTypeHistogram();
        var result = profiler.profile(istream);
        result.toJSON();
    }

    @Test
    public void singleHistogram() {
        this.testMicroBlazeHistogram(MicroBlazeLivermoreELFN10.cholesky);
    }

    @Test
    public void MicroBlazeHistogramBatch() {
        for (var elf : MicroBlazeLivermoreELFN10.values()) {
            this.testMicroBlazeHistogram(elf);
        }
    }
}
