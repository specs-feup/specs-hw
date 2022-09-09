package pt.up.fe.f4pga;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import pt.up.fe.specs.util.SpecsSystem;

public class F4PGA_Shell {

    /**
     * shell.init()
     * 
     * @throws InterruptedException
     */

    /**
     * Runs a bash shell script with variables depending on the FPGA
     * 
     * 
     * @param fileScript
     * @return
     * @throws IOException
     */
    public File runBashShellScript(File fileScript) throws IOException {

        // String i = SpecsIo.getResource("pt/up/fe/f4pga/f4pga_config.sh");

        var pb = new ProcessBuilder("bash", fileScript.toString());
        var streamWriter = new OutputStreamWriter(new FileOutputStream(fileScript));
        var printWriter = new PrintWriter(streamWriter);

        printWriter.println("#!/bin/bash");
        // printWriter.println("cd bin");
        // printWriter.println("ls");

        printWriter.close();

        SpecsSystem.runProcess(pb, true, true);

        return fileScript;
    }

}
