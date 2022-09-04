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

	String F4PGA_INSTALL_DIR="~/opt/f4pga";
	String F4PGA_FAM_DIR;

	FPGA validFPGA;
	


	ArrayList<FPGA> getFpgaList() {

		FPGA a1 = new FPGA("basis3","xc7");
		FPGA a2 = new FPGA("arty 35t","artix7");
		FPGA a3 = new FPGA("arty 100t","artix7");
		FPGA a4 = new FPGA("nexys4 ddr","artix7");
		FPGA a5 = new FPGA("nexys video","artix7");
		FPGA a6 = new FPGA("zylo-z7","artix7");
		
		ArrayList<FPGA> listFPGA = new ArrayList<>();
		listFPGA.add(a1);
		listFPGA.add(a2);
		listFPGA.add(a3);
		listFPGA.add(a4);
		listFPGA.add(a5);
		listFPGA.add(a6);
		
		return listFPGA;
	}


	public void init(String fpgaHwType) throws Exception {


		// Checking if FPGA is supported by F4PGA
		if (!isFpgaValid(fpgaHwType)) {
			throw new Exception("Invalid FPGA");
		}

		// getting the resource (f4pga_config.sh)
		String i = SpecsIo.getResource("pt/up/fe/f4pga/f4pga_config.sh");

		// Creating a replacer
		Replacer r = new Replacer(i);

		r.replace("<FPGA_FAM>", validFPGA.getFpgaFamily());

		System.out.println(i);

		F4PGA_FAM_DIR = F4PGA_INSTALL_DIR +'/' +validFPGA.getFpgaFamily();
		String command = "cd "+F4PGA_FAM_DIR +"/conda/etc/profile.d";
		System.out.println(command);
		
		
		SpecsSystem.runProcess(Arrays.asList(command), false, true);
		SpecsSystem.runProcess(Arrays.asList("cd ~/opt/f4pga/xc7/conda/etc/profile.d"), false, true);
		SpecsSystem.runProcess(Arrays.asList("pwd"), false, true);
		SpecsSystem.runProcess(Arrays.asList("cd .."), false, true);

	}

	public boolean isFpgaValid(String fpgaHwType) {
		
		ArrayList<FPGA> list = getFpgaList();

		for (int index = 0; index < list.size(); index++) {
			
			FPGA currentFPGA = list.get(index);
			if (currentFPGA.getHwType().equalsIgnoreCase(fpgaHwType)) {
				
				validFPGA = currentFPGA;
				return true;
			}
		}
		
		return false;
	}

	public void build(String buildDir) {
		String target = validFPGA.getHwType();

		String buildCmd = "TARGET=/" + target + " make -C " + buildDir;
		var processOutput = SpecsSystem.runProcess(Arrays.asList(buildCmd), true, false);

	}
	
	
	
	

}
