package org.specs.MicroBlaze.test.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeLivermoreELFN100;
import org.specs.MicroBlaze.stream.MicroBlazeTraceProvider;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.binarytranslation.test.profile.InstructionStreamProfilingUtils;

public class MicroBlazeProfileTester {

    /*
     * map providers to streams
     */
    private static final Map<Class<?>, Class<?>> AUXMAP = new HashMap<>();
    static {
        AUXMAP.put(MicroBlazeTraceProvider.class, MicroBlazeTraceStream.class);
    }

    @Test
    public void MicroBlazeProfile() {

        Class<?> producers[] = { /*MicroBlazeStaticProvider.class ,*/ MicroBlazeTraceProvider.class };

        var profilerList = new ArrayList<Class<?>>();
        profilerList.add(InstructionTypeHistogram.class);
        profilerList.add(InstructionHistogram.class);

        for (var file : MicroBlazeLivermoreELFN100.values()) {
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

    /*
    public static void main(String[] args) {
        MicroBlazeProfileTester.MicroBlazeProfile();
    }*/
}
