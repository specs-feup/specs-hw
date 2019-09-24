package orgs.specs.MicroBlaze.asm;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.Instruction;
import pt.up.fe.specs.binarytranslation.StaticStream;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.LineStream;

public class MicroBlazeElfStream implements StaticStream {

    private static final Pattern MB_REGEX = Pattern.compile("\\s(.[0-9a-f]):\\s*([0-9a-f]+)");
    private LineStream insts;

    // TODO modify MB_REGEX

    public MicroBlazeElfStream(File elfname) {
        var output = SpecsSystem.runProcess(Arrays.asList("mb-objdump", "-d",
                elfname.getAbsolutePath()), new File("."), true, false);
        insts = LineStream.newInstance(output.getStdOut());
    }

    @Override
    public Instruction nextInstruction() {
        String line = null;
        while (((line = insts.nextLine()) != null) && !SpecsStrings.matches(line, MB_REGEX))
            ;

        if (line == null) {
            return null;
        }

        var addressAndInst = SpecsStrings.getRegex(line, MB_REGEX);
        return Instruction.newInstance(addressAndInst.get(0), addressAndInst.get(1));
    }

    @Override
    public void close() {
        insts.close();
    }
}
