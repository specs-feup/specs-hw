package org.specs.Arm.asm.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.specs.Arm.ArmLivermoreELFN100;
import org.specs.Arm.stream.ArmTraceProvider;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.binarytranslation.test.profile.InstructionStreamProfilingUtils;

public class ArmProfileTester {

    /*
     * map providers to streams
     */
    private static final Map<Class<?>, Class<?>> AUXMAP = new HashMap<>();
    static {
        AUXMAP.put(ArmTraceProvider.class, ArmTraceStream.class);
    }

    @Test
    public void ArmProfile() {

        Class<?> producers[] = { /*ArmStaticProvider.class ,*/ ArmTraceProvider.class };

        var profilerList = new ArrayList<Class<?>>();
        profilerList.add(InstructionTypeHistogram.class);
        profilerList.add(InstructionHistogram.class);

        for (var file : ArmLivermoreELFN100.values()) {
            // for (var file : Arrays.asList(ArmLivermoreELFN100.innerprod)) {
            for (var producer : producers) {

                var result = InstructionStreamProfilingUtils.profile(
                        file.getResource(), producer, AUXMAP.get(producer),
                        profilerList, file.getKernelStart(), file.getKernelStop());

                for (var r : result) {
                    r.toJSON();
                }
            }
        }
    }
}
