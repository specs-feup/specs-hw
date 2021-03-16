package pt.up.fe.specs.binarytranslation.gdb;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;
import pt.up.fe.specs.util.collections.concurrentchannel.ChannelProducer;
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
    private final ConcurrentChannel<String> stdout, stdin;
    private final ChannelConsumer<String> stdoutConsumer;
    private final ChannelProducer<String> stdinProducer;
    // private final ConcurrentChannel<String> stdin;

    /*
     * Constructor for a non-interactive scripting run
     
    public GDBRun(Application app, ResourceProvider gdbtmpl) {
        this.app = app;
        this.proc = BinaryTranslationUtils.newGDB(this.app);
    
    }*/

    protected ConcurrentChannel<String> getStdin() {
        return stdin;
    }

    protected ConcurrentChannel<String> getStdout() {
        return stdout;
    }

    protected Process getProc() {
        return proc;
    }

    /*
     * 
     */
    public GDBRun(Application app) {

        // channels
        this.stdout = new ConcurrentChannel<String>(1);
        this.stdoutConsumer = this.stdout.createConsumer();

        this.stdin = new ConcurrentChannel<String>(1);
        this.stdinProducer = this.stdin.createProducer();

        this.app = app;
        this.proc = BinaryTranslationUtils.newProcess(
                new ProcessBuilder(Arrays.asList(app.getGdb().getResource())));

        GDBRunUtils.attachThreads(this);
    }
    /*
    private static void attachThreads(GDBRun run) {
    
        // stdout thread
        Executors.newSingleThreadExecutor()
                .execute(() -> run.stdoutThread(run));
    
        // stderr thread
        Executors.newSingleThreadExecutor()
                .execute(() -> run.stderrThread(run));
    
        // stdin thread
        Executors.newSingleThreadExecutor()
                .execute(() -> run.stdinThread(run));
    
        // discard header
        run.consumeAllGDBResponse();
        return;
    }
    
    private void stdoutThread(GDBRun run) {
    
        var lstream = LineStream.newInstance(run.getProc().getInputStream(), "gdb_stdout");
        var producer = run.getStdout().createProducer();
    
        // this thread will block here if "nextLine" is waiting for content
        // of if main thread has not read the concurrentchannel for the
        // previous stdout line
        while (lstream.hasNextLine()) {
            producer.put(lstream.peekNextLine());
            lstream.nextLine();
        }
    
        lstream.close();
    
        // thread end
        return;
    }
    
    private void stderrThread(GDBRun run) {
    
        var lstream = LineStream.newInstance(run.getProc().getErrorStream(), "gdb_stderr");
        while (lstream.hasNextLine()) {
            lstream.nextLine();
        }
        lstream.close();
    
        // thread end
        return;
    }
    
    private void stdinThread(GDBRun run) {
    
        var bw = new BufferedWriter(new OutputStreamWriter(run.getProc().getOutputStream()));
        var consumer = run.getStdin().createConsumer();
        try {
            while (run.getProc().isAlive()) {
                bw.write(consumer.take());
                bw.newLine();
                bw.flush();
            }
            bw.close();
    
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    
        // thread end
        return;
    }
    */
    /*
     * set confirm off
        undisplay
        set print address off
        set height 0
        file <ELFNAME>
        
        
    MB:        target remote | <QEMUBIN> -nographic -M microblaze-fdt-plnx -m 64 -display none -kernel <ELFNAME> -dtb <DTBFILE> -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
    ARM        target remote | <QEMUBIN> -M virt -cpu cortex-a53 -nographic -kernel <ELFNAME> -chardev stdio,mux=on,id=char0 -mon chardev=char0,mode=readline -serial chardev:char0 -gdb chardev:char0 -S
     */

    public void setupForTracing() {

        this.sendGDBCommand("set confirm off");
        this.sendGDBCommand("undisplay");
        this.sendGDBCommand("set print address off");
        this.sendGDBCommand("set height 0");

        var fd = this.app.getElffile();
        var elfpath = fd.getAbsolutePath();
        this.sendGDBCommand("file " + elfpath);

        // discard output
        this.consumeAllGDBResponse();
    }

    public void sendGDBCommand(String cmd) {
        this.stdinProducer.put(cmd);
    }

    public String consumeAllGDBResponse() {
        var output = new StringBuilder();
        String line = null;
        try {
            while ((line = this.getGDBResponse()) != null) {
                output.append(line + "\n");
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return output.toString();
    }

    public String getGDBResponse() throws InterruptedException {
        return this.stdoutConsumer.poll(1, TimeUnit.SECONDS);

        /*var output = new StringBuilder();
        
        String line = null;
        while ((line = this.stdoutConsumer.poll(1, TimeUnit.SECONDS)) != null) {
            output.append(line);
        }
        return output.toString();*/
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
