package pt.up.fe.specs.binarytranslation.processes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.util.SpecsStrings;

/**
 * Represents an interactive underlying GDB + QEMU process
 * 
 * @author nuno
 *
 */
public class GDBRun extends AProcessRun {

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
        // discard garbage
        this.consumeAllGDBResponse();
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
     *     AARCH64_TRACE_REGEX("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");
     *     MICROBLAZE_INSTRUCTION_TRACE_REGEX("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");
     *     RISC_TRACE_REGEX("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");
     *     
     *     ALL EQUAL! --> ("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)")
     */
    private static final Pattern INSTPATTERN = Pattern.compile("0x([0-9a-f]+)\\s<.*>:\\s0x([0-9a-f]+)");

    public String getAddrAndInstruction() {
        this.sendGDBCommand("x/x $pc");
        var response = this.getGDBResponse();
        var addrandinst = SpecsStrings.getRegex(response, INSTPATTERN);
        return addrandinst.get(0) + ":" + addrandinst.get(1);
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
    public String consumeAllGDBResponse() {
        var output = new StringBuilder();
        String line = null;
        while ((line = super.receive()) != null) {
            output.append(line + "\n");
        }
        return output.toString().stripTrailing();
    }

    /*
     * Get single output line from GDB process
     */
    public String getGDBResponse() {
        return super.receive();
    }
}
