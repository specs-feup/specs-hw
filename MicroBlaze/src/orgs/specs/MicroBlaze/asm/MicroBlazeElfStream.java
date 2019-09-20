package orgs.specs.MicroBlaze.asm;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.staticbinary.ElfStream;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.LineStream;

public class MicroBlazeElfStream implements ElfStream {

    private static final Pattern ARM_REGEX = Pattern.compile("\\s(.[0-9a-f]):\\s*([0-9a-f]+)");
    private LineStream insts;

    public MicroBlazeElfStream(String elfName) {
        var output = SpecsSystem.runProcess(Arrays.asList("cmd.exe", "/c", "arm-none-eabi-objdump", elfName), true,
                false);
        this.insts = LineStream.newInstance(output.getStdOut());
    }

    @Override
    public Instruction nextInstruction() {
        String line = null;
        while (insts.hasNextLine()) {
            line = insts.nextLine();
            if (!SpecsStrings.matches(line, ARM_REGEX)) {
                continue;
            }
        }

        var addressAndInst = SpecsStrings.getRegex(line, ARM_REGEX);
        return Instruction.newInstance(addressAndInst.get(0), addressAndInst.get(1));
    }

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub
        insts.close();
    }
}
