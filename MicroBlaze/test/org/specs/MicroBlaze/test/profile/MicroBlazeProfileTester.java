package org.specs.MicroBlaze.test.profile;

import java.util.Arrays;

import org.junit.Test;
import org.specs.MicroBlaze.MicroBlazeELF;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.binarytranslation.test.profile.InstructionStreamProfilingUtils;

public class MicroBlazeProfileTester {

    @Test
    public void MicroBlazeProfile() {

        Class<?> streams[] = { /*MicroBlazeElfStream.class ,*/ MicroBlazeTraceStream.class };
        Class<?> profilers[] = { InstructionTypeHistogram.class, InstructionHistogram.class };

        // for (var file : MicroBlazeELF.values()) {
        for (var file : Arrays.asList(MicroBlazeELF.pic1d)) {
            for (var stream : streams) {
                for (var profile : profilers) {
                    var result = InstructionStreamProfilingUtils.profile(
                            file.getResource(), stream, profile, file.getKernelStart(), file.getKernelStop());
                    result.toJSON();

                }
            }
        }
    }
}
