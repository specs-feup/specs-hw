package org.specs.MicroBlaze.stream;

import java.io.File;
import java.util.regex.Pattern;

import org.specs.MicroBlaze.MicroBlazeResource;

import pt.up.fe.specs.binarytranslation.BinaryTranslationUtils;
import pt.up.fe.specs.binarytranslation.asm.ApplicationInformation;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;

public class MicroBlazeElfStream extends AStaticInstructionStream {

    private static final String OBJDUMP_EXE = "mb-objdump";
    private static final Pattern REGEX = Pattern.compile("([0-9a-f]+):\\s([0-9a-f]+)");

    public MicroBlazeElfStream(File elfname) {
        super(elfname, OBJDUMP_EXE);
        this.appInfo = new ApplicationInformation(
                MicroBlazeResource.MICROBLAZE_CPU_NAME.getResource(), elfname.getName(),
                BinaryTranslationUtils.getCompilationInfo(elfname.getPath(),
                        MicroBlazeResource.MICROBLAZE_READELF.getResource()));
    }

    @Override
    public Instruction nextInstruction() {

        var newinst = MicroBlazeInstructionStreamMethods.nextInstruction(this.insts, REGEX);
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
        return MicroBlazeInstructionStreamMethods.getInstructionWidth();
    }
}
