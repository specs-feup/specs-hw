package pt.up.fe.specs.binarytranslation.gdb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.collections.concurrentchannel.ConcurrentChannel;

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
    private final BufferedReader stdout, stderr;
    private final BufferedWriter stdin;

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
        this.proc = BinaryTranslationUtils.newProcess(
                new ProcessBuilder(Arrays.asList(app.getGdb().getResource())));
        // this.stdout = GDBRun.getStdIO(LineStream.newInstance(proc.getInputStream(), "proc_stdout"));
        // this.stderr = GDBRun.getStdIO(LineStream.newInstance(proc.getErrorStream(), "proc_stderr"));

        this.stdout = GDBRun.getStdIO(new BufferedReader(new InputStreamReader(proc.getInputStream())));
        this.stderr = GDBRun.getStdIO(new BufferedReader(new InputStreamReader(proc.getErrorStream())));
        this.stdin = GDBRun.getStdIO(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())));

        // consume GDB header text
        // while (this.getGDBResponse() != null)
        // ;
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

    /*
    public static Process newGDB(Application app) {
    
        // launch gdb
        var pbuilder = ;
        var proc = 
        return proc;
    
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
    return new ProcessBuilder(Arrays.asList(gdbexe.getResource(), "-x", "tmpscript.gdb"));
    }*/

    /*
     * Default function to get stdout of GDB
     
    private static LineStream getStdIO(InputStream istream, String name) {
    
        LineStream stdio = null;
        try {
            var lineStreamChannel = new ConcurrentChannel<LineStream>(1);
            lineStreamChannel.createProducer().offer(LineStream.newInstance(istream, name));
            stdio = lineStreamChannel.createConsumer().poll(1, TimeUnit.SECONDS);
    
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    
        if (stdio == null) {
            throw new RuntimeException("Could not obtain output stream of stream generation process");
        }
    
        return stdio;
    }*/

    private static <T> T getStdIO(T channelContent) {

        T stdio = null;
        try {
            var lineStreamChannel = new ConcurrentChannel<T>(1);
            lineStreamChannel.createProducer().offer(channelContent);
            stdio = lineStreamChannel.createConsumer().poll(1, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (stdio == null) {
            throw new RuntimeException("Could not obtain stdio of GDB process");
        }

        return stdio;
    }

    public void sendGDBCommand(String cmd) throws IOException {
        this.stdin.write(cmd);
        this.stdin.flush();
    }

    public String getGDBResponse() throws IOException {
        if (this.stdout.ready())
            return this.stdout.readLine();
        else
            return null;
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
