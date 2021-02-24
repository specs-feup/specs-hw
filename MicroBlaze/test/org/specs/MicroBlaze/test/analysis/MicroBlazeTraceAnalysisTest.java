package org.specs.MicroBlaze.test.analysis;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeDetailedTraceProvider;
import org.specs.traceanalysis.TraceAnalysis;

import pt.up.fe.specs.binarytranslation.producer.detailed.DetailedRegisterInstructionProducer;
import pt.up.fe.specs.util.SpecsIo;

public class MicroBlazeTraceAnalysisTest {
    private DetailedRegisterInstructionProducer getProducer(String elfname) {
        String path = "org/specs/MicroBlaze/asm/" + elfname + ".elf";
        File fd = SpecsIo.resourceCopy(path);
        fd.deleteOnExit();
        
        DetailedRegisterInstructionProducer tr = new MicroBlazeDetailedTraceProvider(fd);
        return tr;
    }

    
    @Test
    public void testAnalyzer() {
        DetailedRegisterInstructionProducer tr = getProducer("dotprod-O2");
        
        TraceAnalysis ta = new TraceAnalysis(tr);
        assertTrue(ta.analyze());
    }
}
