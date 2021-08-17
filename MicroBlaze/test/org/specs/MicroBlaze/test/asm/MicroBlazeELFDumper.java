package org.specs.MicroBlaze.test.asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN100;
import org.specs.MicroBlaze.provider.MicroBlazePolyBenchMiniFloat;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.test.asm.ELFDumperTester;

public class MicroBlazeELFDumper extends ELFDumperTester {

    @Test
    public void sequentialDump() {
        var test = new ArrayList<ELFProvider>();
        // test.addAll((Arrays.asList(MicroBlazeLivermoreN100.values())));
        // test.addAll((Arrays.asList(MicroBlazePolyBenchMiniInt.values())));
        test.addAll((Arrays.asList(MicroBlazePolyBenchMiniFloat.values())));
        sequentialDump(test);
    }

    @Test
    public void parallelDump() {
        var test = new ArrayList<ELFProvider>();
        test.addAll((Arrays.asList(MicroBlazeLivermoreN100.values())));
        // test.addAll((Arrays.asList(MicroBlazePolyBenchMiniInt.values())));
        // test.addAll((Arrays.asList(MicroBlazePolyBenchMiniFloat.values())));
        parallelDump(test);
    }
}
