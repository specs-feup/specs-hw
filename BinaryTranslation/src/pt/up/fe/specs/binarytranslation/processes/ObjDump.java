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
        var elfname = app.getElffile();
        var objdumpexe = app.getObjdump();
        var objdumppath = objdumpexe.getResource();
        if (IS_WINDOWS)
            objdumppath += ".exe";

        args.add(objdumppath);
        args.add("-d");
        args.add(elfname.getAbsolutePath());
        return args;
    }

    public ObjDump(Application app) {
        super(ObjDump.getArgs(app));
        super.attachStdOut();
    }

    public String getLine() {
        return super.receive();
    }
}
