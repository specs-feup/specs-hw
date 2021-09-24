package org.specs.Arm.test.asm;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.specs.Arm.provider.ArmLivermoreN100;
import org.specs.Arm.provider.ArmPolyBenchMiniFloat;

import pt.up.fe.specs.binarytranslation.ELFProvider;
import pt.up.fe.specs.binarytranslation.asm.ELFDumper;

public class ArmELFDumper extends ELFDumper {

    @Test
    public void sequentialDump() {
        var test = new ArrayList<ELFProvider>();
        // test.addAll((Arrays.asList(ArmLivermoreN100.values())));
        // test.addAll((Arrays.asList(ArmPolyBenchMiniInt.values())));
        test.addAll((Arrays.asList(ArmPolyBenchMiniFloat.values())));
        sequentialDump(test);
    }

    @Test
    public void parallelDump() {
        var test = new ArrayList<ELFProvider>();
        test.addAll((Arrays.asList(ArmLivermoreN100.values())));
        parallelDump(test);
    }

}
