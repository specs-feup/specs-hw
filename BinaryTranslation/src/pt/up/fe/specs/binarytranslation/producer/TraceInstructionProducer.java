package pt.up.fe.specs.binarytranslation.producer;

import java.io.File;
import java.util.Arrays;
import java.util.function.BiFunction;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.Replacer;

public class TraceInstructionProducer extends AInstructionProducer {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    /*
     * Output from QEMU Execution
     */
    protected TraceInstructionProducer(Application app, ResourceProvider regex,
            BiFunction<String, String, Instruction> produceMethod) {
        super(app, TraceInstructionProducer.getProperProcess(app), regex, produceMethod);
    }

    /*
     * Determine process to use based on file extension and OS
     */
    private static ProcessBuilder getProperProcess(Application app) {

        var elfname = app.getElffile();
        var name = elfname.getName();
        var extension = name.subSequence(name.length() - 3, name.length());

        // Output from GNU based objdump
        if (extension.equals("elf"))
            return TraceInstructionProducer.newSimulatorBuilder(app);

        // Output from file (previous dump)
        else if (IS_WINDOWS)
            return new ProcessBuilder(Arrays.asList("cmd", "/c", "type", elfname.getAbsolutePath()));

        else // if (IS_LINUX)
            return new ProcessBuilder(Arrays.asList("cat", elfname.getAbsolutePath()));
    }

    public static ProcessBuilder newSimulatorBuilder(Application app) {

        var elfname = app.getElffile();
        var gdbtmpl = app.getGdbtmpl();
        var gdbexe = app.getGdb();
        var dtbfile = app.getDtbfile();
        var qemuexe = app.getQemuexe();
        var elfpath = elfname.getAbsolutePath();

        var gdbScript = new Replacer(gdbtmpl);
        gdbScript.replace("<ELFNAME>", elfpath);
        gdbScript.replace("<QEMUBIN>", qemuexe.getResource());

        // DTB only required by microblaze, for now
        if (dtbfile != null) {
            // copy dtb to local folder
            File fd = SpecsIo.resourceCopy(dtbfile.getResource());
            fd.deleteOnExit();
            gdbScript.replace("<DTBFILE>", fd.getAbsolutePath());
        }

        SpecsIo.write(new File("tmpscript.gdb"), gdbScript.toString());
        return new ProcessBuilder(Arrays.asList(gdbexe.getResource(), "-x", "tmpscript.gdb"));
    }
}
