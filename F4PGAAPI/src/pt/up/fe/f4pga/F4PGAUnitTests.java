package pt.up.fe.f4pga;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class F4PGAUnitTests {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
    public void testGetEnvCommands(String installDir) {
		List<String> a = F4PGA.getEnvCommmands(installDir);
        String str1="";
        assertEquals(a, str1);
    }
	
	public void testGetBuildCommmands(String installDir) {
		List<String> a = F4PGA.getBuildCommmands(installDir);
        String str1="";
        assertEquals(a, str1);
	}
	
	
	public void testNewFlow(String _hdlSourceDir, String _topName) {
		String a = FPGA_Flow.NewFlow(_hdlSourceDir, _topName);
		String str1="";
		assertEquals(a, str1);
	}
	
	public void testRunBashShellScript(File fileScript) {
		File a = F4PGA_Shell.runBashShellScript(fileScript);
		File exampleFile = "";
		assertEquals(a, exampleFile);
	}
	
	


}
