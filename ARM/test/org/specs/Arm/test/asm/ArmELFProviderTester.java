package org.specs.Arm.test.asm;

import java.util.ArrayList;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreELFN10;
import org.specs.Arm.provider.ArmLivermoreELFN100;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.test.asm.ELFProviderTestTester;

public class ArmELFProviderTester extends ELFProviderTestTester {

    @Test
    public void testStartStopAddrReading() {
        var elfs = new ArrayList<Class<? extends ELFProvider>>();
        elfs.add(ArmLivermoreELFN10.class);
        elfs.add(ArmLivermoreELFN100.class);
        testStartStopAddrReading(elfs);
    }
}
