package pt.up.fe.specs.binarytranslation.stream;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.utilities.LineStream;

public abstract class AStaticInstructionStream extends AInstructionStream {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    /*
     * Output from GNU based objdump
     */
    protected AStaticInstructionStream(File elfname, String objdumpexe) {
        super(getProperProcess(elfname, objdumpexe));
    }

    private static ProcessBuilder getProperProcess(File elfname, String objdumpexe) {

        var name = elfname.getName();
        var extension = name.subSequence(name.length() - 3, name.length());

        // Output from GNU based objdump
        if (extension.equals("elf"))
            return new ProcessBuilder(Arrays.asList(objdumpexe, "-d", elfname.getAbsolutePath()));

        // Output from file (previous dump)
        else if (IS_WINDOWS)
            return new ProcessBuilder(Arrays.asList("cmd", "/c", "type", elfname.getAbsolutePath()));

        else // if (IS_LINUX)
            return new ProcessBuilder(Arrays.asList("cat", elfname.getAbsolutePath()));
    }

    @Override
    public InstructionStreamType getType() {
        return InstructionStreamType.STATIC_ELF;
    }

    /*
     * Must be implemented by children
     */
    public abstract Instruction getNextInstruction(LineStream insts, Pattern regex);

    /*
     * Must be implemented by children
     */
    public abstract Pattern getRegex();

    @Override
    public Instruction nextInstruction() {

        var newinst = getNextInstruction(this.insts, getRegex());
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
}
