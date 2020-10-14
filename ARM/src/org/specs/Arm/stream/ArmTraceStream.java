package org.specs.Arm.stream;

import java.io.File;
import java.util.regex.Pattern;

import org.specs.Arm.ArmResource;
import org.specs.Arm.instruction.ArmInstruction;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.ATraceInstructionStream;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class ArmTraceStream extends ATraceInstructionStream {

    // NOTE: should use the qemu that comes pacakged with Vivado

    private static final Pattern REGEX = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");

    public ArmTraceStream(File elfname) {
        super(elfname, ArmResource.QEMU_AARCH64_GDB_TEMPLATE,
                ArmResource.AARCH64_GDB,
                ArmResource.QEMU_AARCH64_BAREMETAL_DTB,
                ArmResource.QEMU_AARCH64_EXE);

        this.appInfo = new Application(
                ArmResource.ARMv8_CPU_NAME.getResource(), elfname.getName(),
                BinaryTranslationUtils.getCompilationInfo(elfname.getPath(),
                        ArmResource.AARCH64_READELF.getResource()));
    }

    @Override
    public Pattern getRegex() {
        return ArmTraceStream.REGEX;
    }

    @Override
    public Instruction newInstance(String address, String instruction) {
        return ArmInstruction.newInstance(address, instruction);
    }

    @Override
    public int getInstructionWidth() {
        return 4; // return in bytes
    }
}
