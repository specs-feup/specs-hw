package pt.up.fe.specs.binarytranslation.processes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.utils.BinaryTranslationUtils;
import pt.up.fe.specs.util.utilities.Replacer;

/**
 * Starts a QEMU process, so that the BTF can have a direct handle to it, in order to KILL IT PROPERLY!
 * 
 * @author nuno
 *
 */
public class QEMU implements AutoCloseable {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");
    private static int portInUse = 1234;

    private int port = 0;
    private Process proc;
    private final List<String> args;

    public QEMU(Application app) {
        this.port = QEMU.portInUse++;
        this.args = QEMU.getArgsList(app, this.port);
    }

    public void start() {
        var newArgs = new ArrayList<String>();
        newArgs.add("bash");
        newArgs.add("-l");
        newArgs.add("-c");
        newArgs.add(args.stream().collect(Collectors.joining(" ")));
        this.proc = BinaryTranslationUtils.newProcess(new ProcessBuilder(newArgs));
    }

    public int getPort() {
        return port;
    }

    /*
     * 
     */
    @Override
    public void close() {
        try {
            if (!this.proc.waitFor(200, TimeUnit.MILLISECONDS))
                proc.destroy();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * 
     */
    private static List<String> getArgsList(Application app, int port) {

        // template as string
        var qemutmpl = app.get(Application.QEMU_ARGS_TEMPLATE).read();

        var elfpath = app.getElffile().getAbsolutePath();
        var qemuexe = app.get(Application.QEMUEXE);
        if (IS_WINDOWS) {
            qemuexe += ".exe";
            elfpath = elfpath.replace("\\", "/");
        }

        var qemuArgs = new Replacer(qemutmpl);
        qemuArgs.replace("<ELFNAME>", elfpath);
        qemuArgs.replace("<QEMUBIN>", qemuexe);
        qemuArgs.replace("<PORT>", port);

        var dtbResourceProvider = app.get(Application.BAREMETAL_DTB);
        if (dtbResourceProvider != null) {
            var dtbfileOnDisk = dtbResourceProvider.write();
            dtbfileOnDisk.deleteOnExit();
            var dtbpath = dtbfileOnDisk.getAbsolutePath();
            if (IS_WINDOWS)
                dtbpath = dtbpath.replace("\\", "/");
            qemuArgs.replace("<DTBFILE>", dtbpath);
        }

        if (IS_WINDOWS)
            qemuArgs.replace("<KILL>", "");
        else
            qemuArgs.replace("<KILL>", "kill");

        return Arrays.asList(qemuArgs.toString().split(" "));
    }

    public boolean isAlive() {
        return this.proc.isAlive();
    }
}
