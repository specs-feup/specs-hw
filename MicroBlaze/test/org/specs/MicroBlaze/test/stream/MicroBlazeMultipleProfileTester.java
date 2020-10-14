package org.specs.MicroBlaze.test.stream;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.binarytranslation.profiling.data.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.data.InstructionProfileResult;
import pt.up.fe.specs.binarytranslation.stream.multistream.InstructionStreamConsumer;
import pt.up.fe.specs.binarytranslation.stream.multistream.InstructionStreamEngine;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeMultipleProfileTester {

    private void testParallel(String elfname) {

        // file
        File fd = SpecsIo.resourceCopy(elfname);
        fd.deleteOnExit();

        // host for threads
        var streamengine = new InstructionStreamEngine(new MicroBlazeTraceStream(fd));

        // consumers
        var profiler1 = new InstructionTypeHistogram();
        var profiler2 = new InstructionHistogram();

        // consumer thread hosters
        var consumer1 = new InstructionStreamConsumer<InstructionProfileResult>(istream -> profiler1.profile(istream));
        var consumer2 = new InstructionStreamConsumer<InstructionProfileResult>(istream -> profiler2.profile(istream));

        // host threads
        streamengine.subscribe(consumer1);
        streamengine.subscribe(consumer2);

        // launch all threads (blocking)
        streamengine.launch();

        var profilerresults1 = consumer1.getConsumeResult();
        var profilerresults2 = consumer2.getConsumeResult();

        profilerresults1.toJSON();
        profilerresults2.toJSON();
    }

    private void testSequential(String elfname) {

        // file
        File fd = SpecsIo.resourceCopy(elfname);
        fd.deleteOnExit();

        // profile 1
        var istream1 = new MicroBlazeTraceStream(fd);
        var profiler1 = new InstructionTypeHistogram();
        var result1 = profiler1.profile(istream1);
        result1.toJSON();

        // profile 2
        var istream2 = new MicroBlazeTraceStream(fd);
        var profiler2 = new InstructionTypeHistogram();
        var result2 = profiler2.profile(istream2);
        result2.toJSON();
    }

    @Test
    public void testSequential() {
        this.testSequential("org/specs/MicroBlaze/asm/cholesky.elf");
    }

    @Test
    public void testParallel() {
        this.testParallel("org/specs/MicroBlaze/asm/cholesky.elf");
    }

}
