package org.specs.Arm.test.asm;

import java.util.ArrayList;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN100;
import org.specs.Arm.provider.ArmPolyBenchMiniFloat;
import org.specs.Arm.provider.ArmPolyBenchMiniInt;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.test.asm.ELFProviderTester;

public class ArmELFProviderTester extends ELFProviderTester {

    @Test
    public void testStartStopAddrReading() {
        var elfs = new ArrayList<Class<? extends ELFProvider>>();
        // elfs.add(ArmLivermoreN10.class);
        elfs.add(ArmLivermoreN100.class);
        elfs.add(ArmPolyBenchMiniInt.class);
        elfs.add(ArmPolyBenchMiniFloat.class);
        testStartStopAddrReading(elfs);
    }
}
