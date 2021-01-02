package org.specs.Riscv.test.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.specs.Riscv.RiscvLivermoreELFN100iam;
import org.specs.Riscv.stream.RiscvTraceProvider;
import org.specs.Riscv.stream.RiscvTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.binarytranslation.test.profile.InstructionStreamProfilingUtils;

public class RiscvProfileTester {

    /*
     * map providers to streams
     */
    private static final Map<Class<?>, Class<?>> AUXMAP = new HashMap<>();
    static {
        AUXMAP.put(RiscvTraceProvider.class, RiscvTraceStream.class);
    }

    @Test
    public void RiscvProfile() {

        Class<?> producers[] = { /*RiscvStaticProvider.class ,*/ RiscvTraceProvider.class };

        var profilerList = new ArrayList<Class<?>>();
        profilerList.add(InstructionTypeHistogram.class);
        profilerList.add(InstructionHistogram.class);

        for (var file : RiscvLivermoreELFN100iam.values()) {
            // for (var file : Arrays.asList(RiscvLivermoreELFN100.innerprod100)) {
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
