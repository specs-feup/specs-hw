package pt.up.fe.f4pga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.Replacer;

public class F4PGA {

	FPGA validFPGA;

	ArrayList<FPGA> getFpgaList() {

		FPGA a1 = new FPGA("basis3","artix7");
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

		var processOutput = SpecsSystem.runProcess(Arrays.asList("export F4PGA_INSTALL_DIR=~/opt/f4pga"), true, false);

		// FPGA_FAM="xc7"
		// var processOutput = SpecsSystem.runProcess(Arrays.asList("FPGA_FAM=\"xc7\""),
		// true, false);
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
