package pt.up.fe.specs.binarytranslation.producer;

import java.io.File;
import java.util.Arrays;
import java.util.function.BiFunction;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.instruction.Instruction;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
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

    /*public static ProcessBuilder newSimulatorBuilder(Application app) {
        
        var gdb = new GDBRun(app);
        gdb.loadFile(app);
        
        
    }*/

    public static ProcessBuilder newSimulatorBuilder(Application app) {

        var elfpath = app.getElffile().getAbsolutePath();
        var qemuexe = app.getQemuexe().getResource();
        var gdbexepath = app.getGdb().getResource();
        if (IS_WINDOWS) {
            qemuexe += ".exe";
            gdbexepath += ".exe";
            elfpath = elfpath.replace("\\", "/");
        }

        var gdbScript = new Replacer(app.getGdbtmpl());
        gdbScript.replace("<ELFNAME>", elfpath);
        gdbScript.replace("<QEMUBIN>", qemuexe);

        if (app.getDtbfile() != null) {
            var fd = BinaryTranslationUtils.getFile(app.getDtbfile().getResource());
            var dtbpath = fd.getAbsolutePath();
            if (IS_WINDOWS)
                dtbpath = dtbpath.replace("\\", "/");
            gdbScript.replace("<DTBFILE>", dtbpath);
        }

        if (IS_WINDOWS)
            gdbScript.replace("<KILL>", "");
        else
            gdbScript.replace("<KILL>", "kill");

        SpecsIo.write(new File("tmpscript.gdb"), gdbScript.toString());
        return new ProcessBuilder(Arrays.asList(gdbexepath, "-x", "tmpscript.gdb"));
    }
}
