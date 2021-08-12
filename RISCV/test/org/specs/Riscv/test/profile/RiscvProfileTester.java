package org.specs.Riscv.test.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.specs.Riscv.provider.RiscvLivermoreELFN100iam;
import org.specs.Riscv.stream.RiscvTraceProducer;
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
        AUXMAP.put(RiscvTraceProducer.class, RiscvTraceStream.class);
    }

    @Test
    public void RiscvProfile() {

        Class<?> producers[] = { /*RiscvStaticProvider.class ,*/ RiscvTraceProducer.class };

        var profilerList = new ArrayList<Class<?>>();
        profilerList.add(InstructionTypeHistogram.class);
        profilerList.add(InstructionHistogram.class);

        for (var elf : RiscvLivermoreELFN100iam.values()) {
            // for (var file : Arrays.asList(RiscvLivermoreELFN100iamf.innerprod100)) {
            for (var producer : producers) {

                var app = elf.toApplication();
                var result = InstructionStreamProfilingUtils.profile(
                        app, producer, AUXMAP.get(producer),
                        profilerList, app.getKernelStart(), app.getKernelStop());

                for (var r : result) {
                    r.toJSON();
                }
            }
        }
    }

}
