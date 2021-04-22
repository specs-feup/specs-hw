package org.specs.MicroBlaze.test.tracer;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.tracer.StreamTracer;
import pt.up.fe.specs.binarytranslation.tracer.TraceBasicBlock;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeTracerTester {

    @Test
    public void testBasicBlockTrace() {

        /*
        var istream = ClassBuilders.buildStream(MicroBlazeTraceStream.class, MicroBlazeLivermoreELFN10.innerprod);
        var tracer = new StreamTracer(istream);
        
        
        // get one basic block
        var bb1 = tracer.nextBasicBlock();
        System.out.println(bb1.toString());
        
        // get another basic block
        var bb2 = tracer.nextBasicBlock();
        System.out.println(bb2.toString());
        
        // consume everything else
        while (istream.nextInstruction() != null) {
        }
        ;*/

        int i = 10;
        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        try (var istream = new MicroBlazeTraceStream(fd)) {

            var tracer = new StreamTracer(istream);
            TraceBasicBlock tbb = null;
            while ((tbb = tracer.nextBasicBlock()) != null && i > 0) {
                System.out.println(tbb.toString());
                i--;
            }

            istream.close();
        }
    }

}
