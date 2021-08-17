package org.specs.Riscv.test.asm;

import java.util.ArrayList;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreN100imaf;
import org.specs.Riscv.provider.RiscvPolyBenchMiniFloat;
import org.specs.Riscv.provider.RiscvPolyBenchMiniInt;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.test.asm.ELFProviderTester;

public class RiscvELFProviderTester extends ELFProviderTester {

    @Test
    public void testStartStopAddrReading() {
        var elfs = new ArrayList<Class<? extends ELFProvider>>();
        // elfs.add(RiscvLivermoreN100im.class);
        elfs.add(RiscvLivermoreN100imaf.class);
        elfs.add(RiscvPolyBenchMiniInt.class);
        elfs.add(RiscvPolyBenchMiniFloat.class);
        testStartStopAddrReading(elfs);
    }
}
