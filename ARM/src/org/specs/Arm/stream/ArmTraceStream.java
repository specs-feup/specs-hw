package org.specs.Arm.stream;

import java.io.File;
import java.util.regex.Pattern;

import org.specs.Arm.ArmResource;

import pt.up.fe.specs.binarytranslation.BinaryTranslationUtils;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;

public class ArmTraceStream extends ATraceInstructionStream {

    private static final String GDB_EXE = "aarch64-none-elf-gdb";
    private static final String QEMU_EXE = "/usr/bin/qemu-system-aarch64";
    private static final Pattern REGEX = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");

    public ArmTraceStream(File elfname) {
        super(elfname, ArmResource.QEMU_AARCH64_GDB_TEMPLATE, GDB_EXE, null, QEMU_EXE);
        this.appName = elfname.getName();
        this.compilationInfo = BinaryTranslationUtils.getCompilationInfo(elfname.getPath(),
                ArmResource.AARCH64_READELF.getResource());
    }

    @Override
    public Instruction nextInstruction() {

        var newinst = ArmInstructionStreamMethods.nextInstruction(this.insts, REGEX);
        if (newinst == null) {
            return null;
        }
        this.numcycles += newinst.getLatency();
        this.numinsts++;
        return newinst;
    }

    @Override
    public int getInstructionWidth() {
        return ArmInstructionStreamMethods.getInstructionWidth();
    }
}
