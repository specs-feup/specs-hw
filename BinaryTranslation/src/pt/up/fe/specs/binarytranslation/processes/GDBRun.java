package pt.up.fe.specs.binarytranslation.processes;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.Replacer;

/**
 * Represents an interactive underlying GDB + QEMU process
 * 
 * @author nuno
 *
 */
public class GDBRun extends StringProcessRun {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");
    // USEFUL: https://os.mbed.com/docs/mbed-os/v6.7/debug-test/debug-microbit.html

    /*
     * Internal status
     */
    private QEMU qemurun;
    private int exitAddr;
    private boolean exitReached = false;
    private boolean targetOpen = false;
    private boolean amInteractiveRun = true;

    /*
     * 
     */
    private static List<String> getArgs(Application app, File scriptFile) {
        var args = new ArrayList<String>();
        args.add(app.get(Application.GDB));
        args.add("-q"); // supresses intro and copywright text at start!
        if (scriptFile != null) {
            args.add("-x");
            args.add(scriptFile.getAbsolutePath());
        }
        return args;
    }

    /*
     * 
     */
    public GDBRun(Application app) {
        this(app, null, null, true);
    }

    /*
     * NOTE: For windows, the run environment should have this var set SET HOME=%USERPROFILE%
     * in case gdb is called from a prompt which is not MSYS (which is what happens when running via Eclipse)
     */
    private GDBRun(Application app, File scriptFile, QEMU qemu, boolean amInteractiveRun) {

        super(GDBRun.getArgs(app, scriptFile));
        this.amInteractiveRun = amInteractiveRun;

        /*
         * QEMU will wait on localhost:1234 
         * See: https://qemu-project.gitlab.io/qemu/system/gdb.html
         */
        if (qemu == null)
            this.qemurun = new QEMU(app);
        else
            this.qemurun = qemu;
    }

    /*
     * Newinstance
     */
    private static GDBRun newInstance(Application app,
            ResourceProvider gdbtmpl, boolean amInteractiveRun) {
        var qemurun = new QEMU(app);
        return new GDBRun(app,
                GDBRun.getGDBScript(gdbtmpl, app, qemurun.getPort()),
                qemurun, amInteractiveRun);
    }

    /*
     * Newinstance
     */
    public static GDBRun newInstanceFreeRun(Application app) {
        return GDBRun.newInstance(app, app.get(Application.GDBTMPLNONINTER), false);
    }

    /*
     * Newinstance
     */
    public static GDBRun newInstanceInteractive(Application app) {
        return GDBRun.newInstance(app, app.get(Application.GDBTMPLINTER), true);
    }

    /*
     * 
     */
    private static File getGDBScript(ResourceProvider gdbtmpl, Application app, int qemuport) {

        var elfpath = app.getElffile().getAbsolutePath();
        var gdbScript = new Replacer(gdbtmpl);
        gdbScript.replace("<ELFNAME>", elfpath);
        gdbScript.replace("<PORT>", qemuport);

        if (IS_WINDOWS)
            gdbScript.replace("<KILL>", "");
        else
            gdbScript.replace("<KILL>", "kill");

        var appname = app.getELFProvider().getELFName().replace(".elf", "");
        var cpuname = app.get(Application.CPUNAME);
        var fd = new File("tmpscript_" + cpuname + "_" + appname + "port" + qemuport + ".gdb");
        SpecsIo.write(fd, gdbScript.toString());
        fd.deleteOnExit();
        return fd;
    }

    @Override
    public Process start() {
        this.qemurun.start();
        while (!this.qemurun.isAlive())
            ;

        // testing a wait fo aarch64
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        var proc = super.start();
        if (this.amInteractiveRun) {
            // this.getGDBResponse(5000);
            // this.consumeAllGDBResponse();
            this.exitAddr = this.getExitAddr();
            this.targetOpen = this.hasTarget();
        }
        // first line might take a long time to launch QEMU...
        // consume garbage lines produced from the start of gdb

        // Note:
        // when launching QEMU under GDB, the "target remote" command
        // takes longer than 10ms to complete, hence the 5s timeout for
        // all reads from stdout of the process
        // This shouldn't introduce delays in any other circumstances
        // since poll returns immediately
        return proc;
    }

    @Override
    protected void attachStdOut() {
        if (this.amInteractiveRun)
            Executors.newSingleThreadExecutor()
                    .execute(() -> StdioThreads.stdoutThreadInteractive(this));
        else
            super.attachStdOut();
    }

    /*
     * 
     */
    public void basicSetup() {
        this.sendGDBCommand("set confirm off");
        this.sendGDBCommand("undisplay");
        this.sendGDBCommand("set print address off");
        this.sendGDBCommand("set height 0");

    }

    /*
     * check this before issuing commands that imply continuation of execution
     */
    public boolean isExitReached() {
        return exitReached;
    }

    /*
     * 
     */
    public int getExitAddr() {
        this.sendGDBCommand("x _exit");
        var line = this.readPairedResponse();
        var splits = line.split(":");
        return Integer.parseUnsignedInt(splits[0], 16);
    }

    /*
     * 
     */
    public String loadFile(Application app) {
        var fd = app.getElffile();
        var elfpath = fd.getAbsolutePath();
        if (IS_WINDOWS)
            elfpath = elfpath.replace("\\", "/");
        this.sendGDBCommand("file " + elfpath);
        return this.consumeAllGDBResponse();
    }

    /*
     * 
     */
    public String launchTarget(String remoteCommand) {
        this.sendGDBCommand("target remote | " + remoteCommand);
        this.targetOpen = true; // TODO: no way to make sure if true?
        return this.getGDBResponse(3000);
        // 3 second timeout to give time for the remote to launch
        // return this.consumeAllGDBResponse();
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
    }

    /*
     *
     */
    public void runToEnd() {
        if (this.targetOpen) {
            this.sendGDBCommand("while $pc != _exit");
            this.sendGDBCommand("stepi 1");
            this.discardAllGDBResponse(); // stepi might produce strings such as
            // /usr/src/debug/libmblebspmfpd-libgloss/3.1.0-r0/newlib-3.1.0/libgloss/microblaze/crt0.S: No such file or
            // directory.
            this.sendGDBCommand("x/x $pc");
            this.sendGDBCommand("end");
        }
    }

    /*
     * 
     */
    public void runUntil(String namedtarget) {
        if (this.targetOpen) {
            this.sendGDBCommand("break " + namedtarget);
            this.consumeAllGDBResponse(); // consume ack
            this.sendGDBCommand("c"); // continue
            while (!this.waitForGDB().contains("Breakpoint"))
                ;
        }
    }

    public void runUntil(Long hexaddr) {
        var val = BigInteger.valueOf(hexaddr);
        this.runUntil("*0x" + val.toString(16));
    }

    public void runUntil(Number hexaddr) {
        this.runUntil(hexaddr.longValue());
    }

    /*
     * 
     */
    public void stepi() {
        this.sendGDBCommand("stepi 1");
        this.discardAllGDBResponse();
        // System.out.println(this.consumeAllGDBResponse());
    }

    /*
     * (gdb) help x
     *  Examine memory: x/FMT ADDRESS.
     *  ADDRESS is an expression for the memory address to examine.
     *  FMT is a repeat count followed by a format letter and a size letter.
     *  Format letters are o(octal), x(hex), d(decimal), u(unsigned decimal),
     *    t(binary), f(float), a(address), i(instruction), c(char) and s(string),
     *    T(OSType), A(floating point values in hex).
     *  Size letters are b(byte), h(halfword), w(word), g(giant, 8 bytes).
     *  The specified number of objects of the specified size are printed
     *  according to the format.
     */
    private String readMemory(String fmt) {
        this.sendGDBCommand("x/" + fmt);
        return this.consumeAllGDBResponse();
    }

    public String readByte(long startaddr) {
        return this.readByte(startaddr, 1);
    }

    public String readByte(long startaddr, int count) {
        return this.readMemory(count + "xb " + startaddr);
    }

    public String readHalfWord(long startaddr) {
        return this.readHalfWord(startaddr, 1);
    }

    public String readHalfWord(long startaddr, int count) {
        return this.readMemory(count + "xh " + startaddr);
    }

    public String readWord(long startaddr) {
        return this.readWord(startaddr, 1);
    }

    public String readWord(long startaddr, int count) {
        return this.readMemory(count + "xw " + startaddr);
    }

    /*
     * info target
     */
    private boolean hasTarget() {
        this.sendGDBCommand("info target");
        var allresponse = this.consumeAllGDBResponse(100);
        return allresponse.contains("serial");
    }

    /*
     *
     */
    private static final Pattern INSTPATTERN = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");

    public String getAddrAndInstruction() {
        this.sendGDBCommand("x/x $pc");
        return this.readPairedResponse();
    }

    /*
     * Called by getAddrAndInstruction and getExitAddr
     */
    private String readPairedResponse() {

        // find matching line
        String line = null;
        while ((line = this.getGDBResponse(1000)) != null && (!SpecsStrings.matches(line, INSTPATTERN)))
            ;

        if (line != null) {
            var addrandinst = SpecsStrings.getRegex(line, INSTPATTERN);
            var addr = Integer.parseUnsignedInt(addrandinst.get(0), 16);
            if (addr == this.exitAddr)
                this.exitReached = true; // prevents sending of additional commands
            return addrandinst.get(0) + ":" + addrandinst.get(1);
        } else
            return line;
    }

    /*
     * 
     */
    // private static final Pattern REGPATTERN = Pattern.compile("([0-9a-z]+)\\s*0x([0-9a-f]+)\\s.*");

    // TODO: change this return to HashMap<String, Number> ??

    public String getRegisters() {
        this.sendGDBCommand("info registers");
        return this.consumeAllGDBResponse(100);
    }

    /*
     * 
     */
    private static final Pattern VARPATTERN = Pattern.compile("0x([0-9a-f]+)\\s*(.*)");

    public HashMap<Number, String> getVariableList() {

        // send command
        this.sendGDBCommand("info variables");

        // get list
        var map = new HashMap<Number, String>();
        String line = null;
        while ((line = this.getGDBResponse()) != null) {

            if (!SpecsStrings.matches(line, VARPATTERN))
                continue;

            var addrAndVar = SpecsStrings.getRegex(line, VARPATTERN);
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
        super.send(cmd);
    }

    /*
     * 
     */
    private String consumeAllGDBResponse() {
        return this.consumeAllGDBResponse(200);
    }

    /*
     * Get all lines available from GDB process
     */
    private String consumeAllGDBResponse(int mseconds) {
        var output = new StringBuilder();
        String line = null;
        while ((line = super.receive(mseconds)) != null) {
            output.append(line + "\n");
        }
        return output.toString().stripTrailing();
    }

    /*
     * Get and discard
     */
    private void discardAllGDBResponse() {
        while (super.receive() != null)
            ;
    }

    /* 
     * Wait for continue run
     */
    private String waitForGDB() {
        var ret = super.receive(-1);
        // System.out.println(ret);
        return ret;
        // return super.receive(-1);
    }

    /*
     * Get single output line from GDB process
     */
    private String getGDBResponse() {
        return super.receive();
    }

    /*
     * 
     */
    private String getGDBResponse(int mseconds) {
        return super.receive(mseconds);
    }

    /*
     * 
     */
    @Override
    public void close() {
        this.quit();
        this.qemurun.close();
        super.close();
    }
}
