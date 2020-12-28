package org.specs.MicroBlaze.test.profile;

import org.junit.Test;
import org.specs.MicroBlaze.stream.MicroBlazeTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.binarytranslation.test.profile.InstructionStreamProfilingUtils;

public class MicroBlazeProfileTester {

    private final String[] FILES = {
            "org/specs/MicroBlaze/asm/cholesky.elf",
            "org/specs/MicroBlaze/asm/diffpredict.elf",
            "org/specs/MicroBlaze/asm/glinearrec.elf",
            "org/specs/MicroBlaze/asm/hydro.elf",
            "org/specs/MicroBlaze/asm/hydro2d.elf",
            "org/specs/MicroBlaze/asm/innerprod.elf",
            "org/specs/MicroBlaze/asm/matmul.elf"
    };

    // TODO move to resource provider in MicroBlaze (MicroBlazeELFsParameters.java ??)
    private final Number[] START_ADDR = {
            0x4b54,
            0x2ec8,
            0x55b4,
            0x4a84,
            0x4d80,
            0x2e64,
            0x517c
    };

    private final Number[] STOP_ADDR = {
            0x4c44,
            0x3008,
            0x5698,
            0x4b50,
            0x5178,
            0x2ec4,
            0x5284
    };

    @Test
    public void MicroBlazeProfile() {

        Class<?> streams[] = { /*MicroBlazeElfStream.class ,*/ MicroBlazeTraceStream.class };
        Class<?> profilers[] = { InstructionTypeHistogram.class, InstructionHistogram.class };

        int i = 0;
        for (var file : FILES) {
            for (var stream : streams) {
                for (var profile : profilers) {
                    var result = InstructionStreamProfilingUtils.profile(
                            file, stream, profile, START_ADDR[i], STOP_ADDR[i]);
                    result.toJSON();

                }
            }
            i++;
        }
    }
}
