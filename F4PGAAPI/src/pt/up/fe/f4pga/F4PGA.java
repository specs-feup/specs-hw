package pt.up.fe.f4pga;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.Replacer;

public class F4PGA {

	String DEFAULT_F4PGA_INSTALL_DIR="~/opt/f4pga";

	FPGA validFPGA;
	FPGA_Flow flow;

	private ArrayList<FPGA> getFpgaList() {

		FPGA basis3 = new FPGA("basis3","xc7");
		FPGA arty_35t = new FPGA("arty 35t","xc7");
		FPGA arty_100t = new FPGA("arty 100t","xc7");
		FPGA nexys4_ddr = new FPGA("nexys4 ddr","xc7");
		FPGA nexys_video = new FPGA("nexys video","xc7");
		FPGA zylo_z7 = new FPGA("zylo-z7","xc7");
		
		
		ArrayList<FPGA> listFPGA = new ArrayList<>();
		
		listFPGA.add(basis3);
		listFPGA.add(arty_35t);
		listFPGA.add(arty_100t);
		listFPGA.add(nexys4_ddr);
		listFPGA.add(nexys_video);
		listFPGA.add(zylo_z7);
		
		return listFPGA;
	}
	
	private List<String> getEnvCommmands(String installDir) {
		
		String famDir = installDir +'/' + validFPGA.getFpgaFamily();
		
		String exportF4pgaCommand = "export PATH=" + installDir + "/install/bin:$PATH";
		String sourceCondaCommand = "source " + famDir + "/conda/etc/profile.d/conda.sh";
		String activateCondaCommand = "conda activate " + validFPGA.getFpgaFamily();
		
		List<String> commandList = new List<>();
		
		commandList.add(exportF4pgaCommand);
		commandList.add(sourceCondaCommand);
		commandList.add(activateCondaCommand);
		
		return commandList;
	}
	
	private List<String> getBuildCommmands(String installDir) {
		
		// f4pga build --flow ./flow.json
		
		List<String> commandList = new List<>();
		
		return commandList;
	}
		
	private void getFPGA(String fpgaHwType) throws Exception {

		ArrayList<FPGA> list = getFpgaList();

		for (int index = 0; index < list.size(); index++) {
			
			FPGA currentFPGA = list.get(index);
			if (currentFPGA.getHwType().equalsIgnoreCase(fpgaHwType)) {
				
				validFPGA = currentFPGA;
				return;
			}
		}
		throw new Exception("Invalid FPGA");
	}
	
	public void init(String fpgaHwType) throws Exception {

		String F4pgaInstallDir = DEFAULT_F4PGA_INSTALL_DIR;
		// Checking if FPGA is supported by F4PGA
		getFPGA(fpgaHwType);
		List<String> commandList = getInitCommmands(F4pgaInstallDir);
		getResources();

		SpecsSystem.runProcess(commandList, false, true);
	}
	
	public void config(String hdlSourceDir, String topName){	
		flow.newFlow(hdlSourceDir, topName);
	}
	public void build(String buildDir) {
		//fpgaFlowGenerate(String sourcePath, String topName)
		String target = validFPGA.getHwType();
		

		String buildCmd = "TARGET=/" + target + " make -C " + buildDir;
		var processOutput = SpecsSystem.runProcess(Arrays.asList(buildCmd), true, false);

	}
	
	public void upload() {
	}
}
