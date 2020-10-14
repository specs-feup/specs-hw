package org.specs.Riscv.stream;

import java.io.File;
import java.util.regex.Pattern;

import org.specs.Riscv.RiscvResource;
import org.specs.Riscv.instruction.RiscvInstruction;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

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
    public Instruction newInstance(String address, String instruction) {
        return RiscvInstruction.newInstance(address, instruction);
    }

    @Override
    public int getInstructionWidth() {
        return 4; // return in bytes
        // TODO replace this with something smarter
    }
}
