package org.specs.Riscv.stream;

import java.io.File;
import java.util.regex.Pattern;

import org.specs.Riscv.RiscvResource;
import org.specs.Riscv.instruction.RiscvInstruction;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class RiscvTraceStream extends ATraceInstructionStream {

    private static final Pattern REGEX = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");

    public RiscvTraceStream(File elfname) {
        super(elfname, RiscvResource.QEMU_RISCV_GDB_TEMPLATE,
                RiscvResource.RISCV_GDB,
                RiscvResource.QEMU_RISCV_BAREMETAL_DTB,
                RiscvResource.QEMU_RISCV_EXE);

        this.appInfo = new Application(
                RiscvResource.RISCV_CPU_NAME.getResource(), elfname.getName(),
                BinaryTranslationUtils.getCompilationInfo(elfname.getPath(),
                        RiscvResource.RISCV_READELF.getResource()));
    }

    @Override
    public Pattern getRegex() {
        return RiscvTraceStream.REGEX;
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
