package pt.up.fe.specs.binarytranslation.stream;

import java.io.File;
import java.util.Arrays;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.Replacer;

public abstract class ATraceInstructionStream extends AInstructionStream {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    /*
     * 
     */
    protected ATraceInstructionStream(File elfname, ResourceProvider gdbtmpl,
            ResourceProvider gdbexe, ResourceProvider dtbfile, ResourceProvider qemuexe) {

        super(ATraceInstructionStream.getProperProcess(elfname,
                gdbtmpl, gdbexe, dtbfile, qemuexe));
    }

    /*
     * Determine process to use based on file extension and OS
     */
    private static ProcessBuilder getProperProcess(File elfname,
            ResourceProvider gdbtmpl, ResourceProvider gdbexe, ResourceProvider dtbfile, ResourceProvider qemuexe) {

        var name = elfname.getName();
        var extension = name.subSequence(name.length() - 3, name.length());

        // Output from GNU based objdump
        if (extension.equals("elf"))
            return ATraceInstructionStream.newSimulatorBuilder(elfname, gdbtmpl, gdbexe, dtbfile, qemuexe);

        // Output from file (previous dump)
        else if (IS_WINDOWS)
            return new ProcessBuilder(Arrays.asList("cmd", "/c", "type", elfname.getAbsolutePath()));

        else // if (IS_LINUX)
            return new ProcessBuilder(Arrays.asList("cat", elfname.getAbsolutePath()));
    }

    public static ProcessBuilder newSimulatorBuilder(File elfname,
            ResourceProvider gdbtmpl, ResourceProvider gdbexe,
            ResourceProvider dtbfile, ResourceProvider qemuexe) {

        String elfpath = elfname.getAbsolutePath();
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

    @Override
    public InstructionStreamType getType() {
        return InstructionStreamType.TRACE;
    }
}
