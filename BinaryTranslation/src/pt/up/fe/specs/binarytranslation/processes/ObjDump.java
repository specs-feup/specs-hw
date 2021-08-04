package pt.up.fe.specs.binarytranslation.processes;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.asm.Application;

public class ObjDump extends StringProcessRun {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    /*
     * 
     */
    private static List<String> getArgs(Application app) {
        var args = new ArrayList<String>();
        var elfname = app.getElffile().getAbsolutePath();
        var objdumpexe = app.get(Application.OBJDUMP);
        if (IS_WINDOWS)
            objdumpexe += ".exe";
        args.add(objdumpexe);
        args.add("-d");
        args.add(elfname);
        return args;
    }

    public ObjDump(Application app) {
        super(ObjDump.getArgs(app));
    }

    @Override
    protected void attachThreads() {
        this.attachStdOut();
    }

    public String getLine() {
        return super.receive();
    }
}
