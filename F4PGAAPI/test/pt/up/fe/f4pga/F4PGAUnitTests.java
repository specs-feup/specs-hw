package pt.up.fe.f4pga;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.utilities.Replacer;

public class F4PGAUnitTests {

    @Test
    public void test() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetEnvCommands(String installDir) {
        List<String> a = F4PGA.getEnvCommmands(installDir);
        String str1 = "";
        assertEquals(a, str1);
    }

    @Test
    public void testGetBuildCommmands(String installDir) {
        List<String> a = F4PGA.getBuildCommmands(installDir);
        String str1 = "";
        assertEquals(a, str1);
    }

    @Test
    public void testNewFlow(String _hdlSourceDir, String _topName) {
        String a = FPGA_Flow.NewFlow(_hdlSourceDir, _topName);
        String str1 = "";
        assertEquals(a, str1);
    }

    @Test
    public void testRunBashShellScript(File fileScript) {
        File a = F4PGA_Shell.runBashShellScript(fileScript);
        File exampleFile = "";
        assertEquals(a, exampleFile);
    }

    @Test
    public void testReplacer() {

        F4PGA basis3F4PGA = new F4PGA();
        basis3F4PGA.init("basis3");

        System.out.println("aqui");
        String i = SpecsIo.getResource("pt/up/fe/f4pga/makefile");
        // System.out.println(i);
        // Replacer r = new Replacer(() -> "pt/up/fe/f4pga/makefile");
        Replacer r = new Replacer(i);
        r.replace("basys3", "Antonio");
        r.replace("<put the name of your top module here>", "MODIFICADO");
        // System.out.println("Replaced:\n"+r.toString());
        // var processOutput = SpecsSystem.runProcess(Arrays.asList("dir"), true, false);
        // System.out.println(processOutput.getOutput());
        // String i = SpecsIo.getResource("resourceExample");

    }

}
