package org.specs.MicroBlaze.test.asm;

import java.util.ArrayList;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN10;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreELFN100;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.test.asm.ELFProviderTester;

public class MicroBlazeELFProviderTester extends ELFProviderTester {

    @Test
    public void testStartStopAddrReading() {
        var elfs = new ArrayList<Class<? extends ELFProvider>>();
        elfs.add(MicroBlazeLivermoreELFN10.class);
        elfs.add(MicroBlazeLivermoreELFN100.class);
        testStartStopAddrReading(elfs);
    }
}
