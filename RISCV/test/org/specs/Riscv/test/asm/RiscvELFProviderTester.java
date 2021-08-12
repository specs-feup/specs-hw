package org.specs.Riscv.test.asm;

import java.util.ArrayList;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreELFN100iam;
import org.specs.Riscv.provider.RiscvLivermoreELFN100iamf;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.test.asm.ELFProviderTester;

public class RiscvELFProviderTester extends ELFProviderTester {

    @Test
    public void testStartStopAddrReading() {
        var elfs = new ArrayList<Class<? extends ELFProvider>>();
        elfs.add(RiscvLivermoreELFN100iam.class);
        elfs.add(RiscvLivermoreELFN100iamf.class);
        testStartStopAddrReading(elfs);
    }
}
