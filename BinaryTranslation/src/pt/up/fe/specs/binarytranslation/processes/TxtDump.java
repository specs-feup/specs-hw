package pt.up.fe.specs.binarytranslation.processes;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class TxtDump extends AProcessRun {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");
    private static final List<String> WARGS = Arrays.asList("cmd", "/c", "type");
    private static final List<String> UARGS = Arrays.asList("cat");

    private static List<String> getArgs(File txtfile) {
        var args = (IS_WINDOWS) ? WARGS : UARGS;
        args.add(txtfile.getAbsolutePath());
        return args;
    }

    public TxtDump(File txtfile) {
        super(TxtDump.getArgs(txtfile));
        StdioThreadUtils.attachOut(this);
    }
}
