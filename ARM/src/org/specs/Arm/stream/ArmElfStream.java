package org.specs.Arm.stream;

import java.io.File;
import java.util.regex.Pattern;

import org.specs.Arm.ArmResource;

import pt.up.fe.specs.binarytranslation.BinaryTranslationUtils;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;

public class ArmElfStream extends AStaticInstructionStream {

    private static final String OBJDUMP_EXE = "aarch64-none-elf-objdump";
    private static final Pattern REGEX = Pattern.compile("([0-9a-f]+):\\s([0-9a-f]+)");

    public ArmElfStream(File elfname) {
        super(elfname, OBJDUMP_EXE);
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

        if (this.numinsts % 1000 == 0) {
            System.out.println(this.numinsts + " instructions processed...");
        }

        return newinst;
    }

    @Override
    public int getInstructionWidth() {
        return ArmInstructionStreamMethods.getInstructionWidth();
    }
}
