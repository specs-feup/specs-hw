package org.specs.MicroBlaze.stream;

import java.io.File;
import java.util.regex.Pattern;

import org.specs.MicroBlaze.MicroBlazeResource;
import org.specs.MicroBlaze.instruction.MicroBlazeInstruction;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.stream.AStaticInstructionStream;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;

public class MicroBlazeElfStream extends AStaticInstructionStream {

    private static final Pattern REGEX = Pattern.compile("([0-9a-f]+):\\s([0-9a-f]+)");

    public MicroBlazeElfStream(File elfname) {
        super(elfname, MicroBlazeResource.MICROBLAZE_OBJDUMP.getResource());
        this.appInfo = new Application(
                MicroBlazeResource.MICROBLAZE_CPU_NAME.getResource(), elfname.getName(),
                BinaryTranslationUtils.getCompilationInfo(elfname.getPath(),
                        MicroBlazeResource.MICROBLAZE_READELF.getResource()));
    }

    @Override
    public Pattern getRegex() {
        return MicroBlazeElfStream.REGEX;
    }

    @Override
    public Instruction newInstance(String address, String instruction) {
        return MicroBlazeInstruction.newInstance(address, instruction);
    }

    @Override
    public int getInstructionWidth() {
        return 4; // return in bytes
    }
}
