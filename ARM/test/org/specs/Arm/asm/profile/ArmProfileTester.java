package org.specs.Arm.asm.profile;

import java.util.Arrays;

import org.junit.Test;
import org.specs.Arm.ArmELF;
import org.specs.Arm.stream.ArmTraceStream;

import pt.up.fe.specs.binarytranslation.profiling.InstructionHistogram;
import pt.up.fe.specs.binarytranslation.profiling.InstructionTypeHistogram;
import pt.up.fe.specs.binarytranslation.test.profile.InstructionStreamProfilingUtils;

public class ArmProfileTester {

    @Test
    public void ArmProfile() {

        Class<?> streams[] = { /*ArmElfStream.class ,*/ ArmTraceStream.class };
        Class<?> profilers[] = { InstructionTypeHistogram.class, InstructionHistogram.class };

        // for (var file : ArmELF.values()) {
        for (var file : Arrays.asList(ArmELF.innerprod)) {
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
