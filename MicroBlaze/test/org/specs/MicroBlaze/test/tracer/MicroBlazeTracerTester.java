package org.specs.MicroBlaze.test.tracer;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.tracer.StreamTracer;
import pt.up.fe.specs.binarytranslation.tracer.TraceBasicBlock;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeTracerTester {

    @Test
    public void testBasicBlockTrace() {

        int i = 5;
        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        try (var istream = new MicroBlazeTraceStream(fd)) {

            // basic blocks
            var tracer = new StreamTracer(istream);
            TraceBasicBlock tbb = null;
            while ((tbb = tracer.nextBasicBlock()) != null && i > 0) {
                System.out.println(tbb.toString());
                i--;
            }

            istream.close();
        }
    }

    @Test
    public void testSuperBlockTrace() {

        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN100.pic1d100);
        try (var istream = new MicroBlazeTraceStream(fd)) {

            // super block? max size 5
            var tracer = new StreamTracer(istream);
            // TraceSuperBlock sblock = null;
            // while ((sblock = tracer.nextSuperBlock(20)) != null) {
            while (!istream.isClosed()) {
                var sblock = tracer.nextSuperBlock(20);
                if (sblock != null)
                    System.out.println(sblock.toString());
            }

            istream.close();
        }
    }

}
