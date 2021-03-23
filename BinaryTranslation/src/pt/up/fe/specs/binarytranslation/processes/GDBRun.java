package pt.up.fe.specs.binarytranslation.processes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.producer.detailed.RegisterDump;
import pt.up.fe.specs.util.SpecsStrings;

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
    private boolean targetOpen = false;
    private Application app;

    /*
     * Constructor for a non-interactive scripting run
     
    public GDBRun(Application app, ResourceProvider gdbtmpl) {
        this.app = app;
        this.proc = BinaryTranslationUtils.newGDB(this.app);
    
    }*/

    protected Application getApp() {
        return app;
    }

    /*
     * 
     */
    private static List<String> getArgs(Application app, File scriptFile) {
        var args = new ArrayList<String>();
        args.add(app.getGdb().getResource());
        if (scriptFile != null) {
            args.add("-q"); // supresses intro and copywright text at start!
            args.add("-x");
            args.add(scriptFile.getAbsolutePath());
        }
        return args;
    }

    /*
     * 
     */
    public GDBRun(Application app) {
        this(app, null);
    }

    /*
     * 
     */
    public GDBRun(Application app, File scriptFile) {
        super(GDBRun.getArgs(app, scriptFile));
        super.attachThreads();
        this.getGDBResponse(1000); // consume a single garbage line produced from the start of gdb
        // Note:
        // when launching QEMU under GDB, the "target remote" command
        // takes longer than 10ms to complete, hence the 1s timeout for
        // all reads from stdout of the process
        // This shouldn't introduce delays in any other circumstances
        // since poll returns immediately

        // check if a target was opened
        this.targetOpen = this.hasTarget();
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
        // this.sendGDBCommand("monitor quit");
        // this.sendGDBCommand("disconnect");
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
    public void runUntil(long addr) {
        var hexaddr = Long.toHexString(addr);
        this.runUntil("0x" + hexaddr);
    }

    /*
     *
     */
    public void runToEnd() {
        if (this.targetOpen) {
            this.sendGDBCommand("while $pc != _exit");
            this.sendGDBCommand("stepi 1");
            this.sendGDBCommand("x/x $pc");
            this.sendGDBCommand("end");
        }
    }

    /*
     * 
     */
    public void runUntil(String hexaddr) {
        // this.sendGDBCommand("while $pc != " + hexaddr + "\nstepi 1\nend");
        this.sendGDBCommand("break *" + hexaddr);
        this.consumeAllGDBResponse(); // consume ack
        this.sendGDBCommand("c"); // continue
        this.getGDBResponse(); // consume "Continuing."
        this.getGDBResponse(); // consume ""
        this.waitForGDB();
    }

    /*
     * 
     */
    public void stepi() {
        this.sendGDBCommand("stepi 1");
        this.discardAllGDBResponse();
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
        var allresponse = this.consumeAllGDBResponse();
        return allresponse.contains("serial");
    }

    /*
     *
     */
    private static final Pattern INSTPATTERN = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");

    public String getAddrAndInstruction() {
        this.sendGDBCommand("x/x $pc");
        String line = null, ret = null;
        while ((line = this.getGDBResponse(1000)) != null) {
            if (!SpecsStrings.matches(line, INSTPATTERN))
                continue;

            var addrandinst = SpecsStrings.getRegex(line, INSTPATTERN);
            ret = addrandinst.get(0) + ":" + addrandinst.get(1);
            break;
        }
        // if (ret == null)
        // System.out.println("OPS");
        return ret;
    }

    /*
     * 
     */
    private static final Pattern REGPATTERN = Pattern.compile("([0-9a-z]+)\\s*0x([0-9a-f]+)\\s.*");

    public RegisterDump getRegisters() {
        this.sendGDBCommand("info registers");

        var dump = new RegisterDump();
        String line = null;
        while ((line = this.getGDBResponse()) != null) {
            if (!SpecsStrings.matches(line, REGPATTERN))
                continue;

            var regAndValue = SpecsStrings.getRegex(line, REGPATTERN);
            var reg = regAndValue.get(0).trim();
            var value = Long.valueOf(regAndValue.get(1).trim(), 16);
            dump.add(reg, value);
        }

        return dump;
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
     * Get all lines available from GDB process
     */
    private String consumeAllGDBResponse() {
        var output = new StringBuilder();
        String line = null;
        while ((line = super.receive()) != null) {
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
    private void waitForGDB() {
        super.receive(-1);
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
}
