package org.specs.MicroBlaze.test.analysis;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.stream.MicroBlazeDetailedTraceProvider;

import pt.up.fe.specs.binarytranslation.analysis.TraceAnalysis;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeTraceAnalysisTest {

    @Test
    public void testMicroBlazeAnalysis() {
        var fd = BinaryTranslationUtils.getFile(MicroBlazeLivermoreELFN10.innerprod);
        var dip = new MicroBlazeDetailedTraceProvider(fd);
        var ta = new TraceAnalysis(dip);
        assertTrue(ta.analyze());
    }
}
