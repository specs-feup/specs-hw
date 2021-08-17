package org.specs.Riscv.test.asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreN100im;
import org.specs.Riscv.provider.RiscvPolyBenchMiniFloat;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.test.asm.ELFDumperTester;

public class RIscvELFDumper extends ELFDumperTester {

    @Test
    public void sequentialDump() {
        var test = new ArrayList<ELFProvider>();
        // test.addAll((Arrays.asList(RiscvLivermoreN100im.values())));
        // test.addAll((Arrays.asList(RiscvPolyBenchMiniInt.values())));
        test.addAll((Arrays.asList(RiscvPolyBenchMiniFloat.values())));
        sequentialDump(test);
    }

    @Test
    public void parallelDump() {
        var test = new ArrayList<ELFProvider>();
        test.addAll((Arrays.asList(RiscvLivermoreN100im.values())));
        parallelDump(test);
    }
}
