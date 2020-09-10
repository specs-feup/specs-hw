package org.specs.Riscv.stream;

import java.io.File;
import java.util.regex.Pattern;

import org.specs.Riscv.RiscvResource;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.utilities.LineStream;

public class RiscvElfStream extends AStaticInstructionStream {

    private static final Pattern REGEX = Pattern.compile("([0-9a-f]+):\\s([0-9a-f]+)");

    public RiscvElfStream(File elfname) {
        super(elfname, RiscvResource.RISCV_OBJDUMP.getResource());
        this.appInfo = new Application(
                RiscvResource.RISCV_CPU_NAME.getResource(), elfname.getName(),
                BinaryTranslationUtils.getCompilationInfo(elfname.getPath(),
                        RiscvResource.RISCV_READELF.getResource()));
    }

    @Override
    public Pattern getRegex() {
        return RiscvElfStream.REGEX;
    }

    @Override
    public Instruction getNextInstruction(LineStream insts, Pattern regex) {
        return RiscvInstructionStreamMethods.nextInstruction(insts, regex);
    }

    @Override
    public int getInstructionWidth() {
        return RiscvInstructionStreamMethods.getInstructionWidth();
    }
}
