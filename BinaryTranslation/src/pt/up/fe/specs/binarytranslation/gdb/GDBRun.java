package pt.up.fe.specs.binarytranslation.gdb;

import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;
import pt.up.fe.specs.util.system.ProcessOutput;
import pt.up.fe.specs.util.utilities.LineStream;

/**
 * Represents an interactive underlying GDB + QEMU process
 * 
 * @author nuno
 *
 */
public class GDBRun implements AutoCloseable {

    /*
     * Internal status
     */
    private final Process proc;
    private final Application app;
    private final LineStream /*stdin,*/ stdout;
    private final String stderr;

    /*
     * Constructor for a non-interactive scripting run
     
    public GDBRun(Application app, ResourceProvider gdbtmpl) {
        this.app = app;
        this.proc = BinaryTranslationUtils.newGDB(this.app);
    
    }*/

    /*
     * 
     */
    public GDBRun(Application app) {
        this.app = app;
        this.proc = GDBRun.newGDB(this.app);
        this.stdout = output.getStdOut();
        this.stderr = output.getStdErr();
    }

    /*
     * set confirm off
        undisplay
        set print address off
        set height 0
        file <ELFNAME>
        
        
    MB:        target remote | <QEMUBIN> -nographic -M microblaze-fdt-plnx -m 64 -display none -kernel <ELFNAME> -dtb <DTBFILE> -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
    ARM        target remote | <QEMUBIN> -M virt -cpu cortex-a53 -nographic -kernel <ELFNAME> -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
     */

    public static ProcessOutput<LineStream, String> newGDB(Application app) {

        // launch gdb
        var pbuilder = new ProcessBuilder(Arrays.asList(app.getGdb().getResource()));
        var proc = BinaryTranslationUtils.newProcess(builder);

        /*
        var output = SpecsSystem.runProcess(pbuilder,
                stdio -> GDBRun.getStdIO(stdio, "proc_stdout"),
                stdio -> {
                    return "no stderr";
                }, // stdio -> GDBRun.getStdIO(stdio, "proc_stderr"),
                null, null);
        
        return output;
        */

        // get streams

        // send inital steup commands

        /*
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
        return new ProcessBuilder(Arrays.asList(gdbexe.getResource(), "-x", "tmpscript.gdb"));*/
    }

    /*
     * Default function to get stdout of GDB
     */
    private static LineStream getStdIO(InputStream istream, String name) {

        LineStream stdout = null;
        try {
            var lineStreamChannel = new ConcurrentChannel<LineStream>(1);
            lineStreamChannel.createProducer().offer(LineStream.newInstance(istream, name));
            stdout = lineStreamChannel.createConsumer().poll(1, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (stdout == null) {
            throw new RuntimeException("Could not obtain output stream of stream generation process");
        }

        return stdout;
    }

    /*
     * 
     
    private LineStream inputProcessor(OutputStream ostream) {
    
    }*/

    /*
    public String sendGDBCommand(String cmd) {
    
    }*/

    public String getGDBResponse() {
        var line = this.stdout.nextLine();
        return line;
    }

    @Override
    public void close() {

        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
