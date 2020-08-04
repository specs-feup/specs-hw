package org.specs.MicroBlaze.stream;

import java.io.File;
import java.util.regex.Pattern;

import org.specs.MicroBlaze.MicroBlazeResource;

import pt.up.fe.specs.binarytranslation.BinaryTranslationUtils;
import pt.up.fe.specs.binarytranslation.asm.ApplicationInformation;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.util.utilities.LineStream;

public class MicroBlazeElfStream extends AStaticInstructionStream {

    private static final Pattern REGEX = Pattern.compile("([0-9a-f]+):\\s([0-9a-f]+)");

    public MicroBlazeElfStream(File elfname) {
        super(elfname, MicroBlazeResource.MICROBLAZE_OBJDUMP.getResource());
        this.appInfo = new ApplicationInformation(
                MicroBlazeResource.MICROBLAZE_CPU_NAME.getResource(), elfname.getName(),
                BinaryTranslationUtils.getCompilationInfo(elfname.getPath(),
                        MicroBlazeResource.MICROBLAZE_READELF.getResource()));
    }

    @Override
    public Pattern getRegex() {
        return MicroBlazeElfStream.REGEX;
    }

    @Override
    public Instruction getNextInstruction(LineStream insts, Pattern regex) {
        return MicroBlazeInstructionStreamMethods.nextInstruction(insts, regex);
    }

    @Override
    public int getInstructionWidth() {
        return MicroBlazeInstructionStreamMethods.getInstructionWidth();
    }
}
