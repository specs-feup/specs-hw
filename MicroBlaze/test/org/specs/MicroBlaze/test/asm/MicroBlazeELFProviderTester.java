package org.specs.MicroBlaze.test.asm;

import java.util.ArrayList;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchMiniFloat;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchMiniInt;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.test.asm.ELFProviderTester;

public class MicroBlazeELFProviderTester extends ELFProviderTester {

    @Test
    public void testStartStopAddrReading() {
        var elfs = new ArrayList<Class<? extends ELFProvider>>();
        // elfs.add(MicroBlazeLivermoreN10.class);
        elfs.add(MicroBlazeLivermoreN100.class);
        elfs.add(MicroBlazePolyBenchMiniInt.class);
        elfs.add(MicroBlazePolyBenchMiniFloat.class);
        testStartStopAddrReading(elfs);
    }
}
