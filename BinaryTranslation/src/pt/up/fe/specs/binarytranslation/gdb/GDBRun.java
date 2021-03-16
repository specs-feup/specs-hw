package pt.up.fe.specs.binarytranslation.gdb;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.SpecsStrings;
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

    // USEFUL: https://os.mbed.com/docs/mbed-os/v6.7/debug-test/debug-microbit.html

    /*
     * Internal status
     */
    private boolean targetOpen = false;
    private final Process proc;
    // private final Application app;
    private final ConcurrentChannel<String> stdout, stdin;
    private final ChannelConsumer<String> stdoutConsumer;
    private final ChannelProducer<String> stdinProducer;

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

        // this.app = app;
        this.proc = BinaryTranslationUtils.newProcess(
                new ProcessBuilder(Arrays.asList(app.getGdb().getResource())));

        GDBRunUtils.attachThreads(this);
        this.basicSetup();
    }

    /*
     * 
     */
    private void basicSetup() {
        this.sendGDBCommand("set confirm off");
        this.sendGDBCommand("undisplay");
        this.sendGDBCommand("set print address off");
        this.sendGDBCommand("set height 0");
    }

    /*
     * 
     */
    public String loadFile(Application app) {
        var fd = app.getElffile();
        var elfpath = fd.getAbsolutePath();
        this.sendGDBCommand("file " + elfpath);
        return this.consumeAllGDBResponse();
    }

    /*
     * 
     */
    public String launchTarget(String remoteCommand) {
        this.sendGDBCommand("target remote | " + remoteCommand);
        this.targetOpen = true; // TODO: no way to make sure if true?
        return this.consumeAllGDBResponse();
    }

    /*
     * 
     */
    public String killTarget() {
        this.sendGDBCommand("kill");
        this.targetOpen = false;
        return this.consumeAllGDBResponse();
    }

    /*
     * 
     */
    public void quit() {
        if (targetOpen == true)
            this.killTarget();
        this.sendGDBCommand("quit");
        this.close();
    }

    /*
     * 
     */
    public void stepi() {
        this.sendGDBCommand("stepi 1");
        // this.sendGDBCommand("x/x $pc");
        // return this.consumeAllGDBResponse();
    }

    /*
     * 
     */
    public String readMemory(long addr) {
        this.sendGDBCommand("x/x " + Long.toHexString(addr));
        return this.getGDBResponse();
    }

    /*
     * 
     */
    public String getAddrAndInstruction() {
        this.sendGDBCommand("x/x $pc");
        return this.getGDBResponse();
    }

    /*
     * 
     */
    public String getRegisters() {
        this.sendGDBCommand("info registers");
        return this.consumeAllGDBResponse();
    }

    /*
     * 
     */
    public HashMap<Number, String> getVariableList() {

        // send command
        this.sendGDBCommand("info variables");

        // get list
        var map = new HashMap<Number, String>();
        var regex = Pattern.compile("0x([0-9a-f]+)\\s*(.*)");
        String line = null;
        while ((line = this.getGDBResponse()) != null) {

            if (!SpecsStrings.matches(line, regex))
                continue;

            var addrAndVar = SpecsStrings.getRegex(line, regex);
            var addr = addrAndVar.get(0).trim();
            var varname = addrAndVar.get(1).trim();
            map.put(Long.parseLong(addr, 16), varname);
        }
        return map;
    }

    /*
     * Send any command
     */
    public void sendGDBCommand(String cmd) {
        this.stdinProducer.put(cmd);
    }

    /*
     * Get all lines available from GDB process
     */
    public String consumeAllGDBResponse() {
        var output = new StringBuilder();
        String line = null;
        while ((line = this.getGDBResponse()) != null) {
            output.append(line + "\n");
        }
        return output.toString();
    }

    /*
     * Get single output line from GDB process
     */
    public String getGDBResponse() {
        String ret = null;
        try {
            ret = this.stdoutConsumer.poll(10, TimeUnit.MILLISECONDS); // TODO: best?

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /*
     * 
     */
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
