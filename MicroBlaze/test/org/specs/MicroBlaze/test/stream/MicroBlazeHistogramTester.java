package org.specs.MicroBlaze.test.stream;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.stream.profiler.InstructionTypeHistogram;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeHistogramTester {

    public void testMicroBlazeHistogram(String elfname) {
        File fd = SpecsIo.resourceCopy(elfname);
        fd.deleteOnExit();
        var istream = new MicroBlazeTraceStream(fd);
        var profiler = new InstructionTypeHistogram(istream);
        var result = profiler.profile();
        result.toJSON();
    }

    @Test
    public void singleHistogram() {
        this.testMicroBlazeHistogram("org/specs/MicroBlaze/asm/cholesky.elf");
    }

    @Test
    public void MicroBlazeHistogramBatch() {
        this.testMicroBlazeHistogram("org/specs/MicroBlaze/asm/cholesky.elf");
        this.testMicroBlazeHistogram("org/specs/MicroBlaze/asm/diffpredict.elf");
        this.testMicroBlazeHistogram("org/specs/MicroBlaze/asm/glinearrec.elf");
        this.testMicroBlazeHistogram("org/specs/MicroBlaze/asm/hydro.elf");
        this.testMicroBlazeHistogram("org/specs/MicroBlaze/asm/hydro2d.elf");
        this.testMicroBlazeHistogram("org/specs/MicroBlaze/asm/innerprod.elf");
        this.testMicroBlazeHistogram("org/specs/MicroBlaze/asm/matmul.elf");
    }
}
