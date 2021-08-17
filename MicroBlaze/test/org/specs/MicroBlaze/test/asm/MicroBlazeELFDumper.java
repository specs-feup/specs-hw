package org.specs.MicroBlaze.test.asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.specs.MicroBlaze.provider.MicroBlazeLivermoreN10;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.test.asm.ELFDumperTester;

public class MicroBlazeELFDumper extends ELFDumperTester {

    @Test
    public void sequentialDump() {
        var test = new ArrayList<ELFProvider>();
        test.addAll((Arrays.asList(MicroBlazeLivermoreN10.values())));
        sequentialDump(test);
    }

    @Test
    public void parallelDump() {
        var test = new ArrayList<ELFProvider>();
        test.addAll((Arrays.asList(MicroBlazeLivermoreN10.values())));
        parallelDump(test);
    }
}
