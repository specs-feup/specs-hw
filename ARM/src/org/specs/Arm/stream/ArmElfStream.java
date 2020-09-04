package org.specs.Arm.stream;

import java.io.File;
import java.util.regex.Pattern;

import org.specs.Arm.ArmResource;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.utilities.LineStream;

public class ArmElfStream extends AStaticInstructionStream {

    private static final Pattern REGEX = Pattern.compile("([0-9a-f]+):\\s([0-9a-f]+)");

    public ArmElfStream(File elfname) {
        super(elfname, ArmResource.AARCH64_OBJDUMP.getResource());
        this.appInfo = new Application(
                ArmResource.ARMv8_CPU_NAME.getResource(), elfname.getName(),
                BinaryTranslationUtils.getCompilationInfo(elfname.getPath(),
                        ArmResource.AARCH64_READELF.getResource()));
    }

    @Override
    public Pattern getRegex() {
        return ArmElfStream.REGEX;
    }

    @Override
    public Instruction getNextInstruction(LineStream insts, Pattern regex) {
        return ArmInstructionStreamMethods.nextInstruction(insts, regex);
    }

    @Override
    public int getInstructionWidth() {
        return ArmInstructionStreamMethods.getInstructionWidth();
    }
}
