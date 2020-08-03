package org.specs.Riscv.stream;

import java.io.File;
import java.util.regex.Pattern;

import org.specs.Riscv.RiscvResource;

import pt.up.fe.specs.binarytranslation.BinaryTranslationUtils;
import pt.up.fe.specs.binarytranslation.asm.ApplicationInformation;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;

public class RiscvElfStream extends AStaticInstructionStream {

    private static final Pattern REGEX = Pattern.compile("([0-9a-f]+):\\s([0-9a-f]+)");

    public RiscvElfStream(File elfname) {
        super(elfname, RiscvResource.RISCV_OBJDUMP.getResource());
        this.appInfo = new ApplicationInformation(
                RiscvResource.RISCV_CPU_NAME.getResource(), elfname.getName(),
                BinaryTranslationUtils.getCompilationInfo(elfname.getPath(),
                        RiscvResource.RISCV_READELF.getResource()));
    }

    @Override
    public Instruction nextInstruction() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getInstructionWidth() {
        return 4; // return in bytes
        // TODO replace this with something smarter
    }
}
