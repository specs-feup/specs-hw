package pt.up.fe.f4pga;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.Replacer;

public class F4PGA {
	
	FPGA validFPGA;
	
	final ArrayList<FPGA> list = new ArrayList<>();
	
	
	public void init(String fpgaFamilyName, String fpgaHwType) throws Exception {
		
		//Checking if FPGA is supported by F4PGA
		boolean aux = isFpgaValid(fpgaFamilyName, fpgaHwType);
		
		if(aux == false) {
			throw new Exception("Invalid FPGA Family Name");
		}
		
		//Saving FPGA in a class variable
		 validFPGA = new FPGA(fpgaFamilyName, fpgaHwType);
		
		//getting the resource (f4pga_config.sh)
		String i = SpecsIo.getResource("pt/up/fe/f4pga/f4pga_config.sh");
		
		//Creating a replacer
		Replacer r = new Replacer(i);
		
		r.replace("<FPGA_FAM>", fpgaFamilyName);
		
		System.out.println(i);		
		
		var processOutput = SpecsSystem.runProcess(Arrays.asList("export F4PGA_INSTALL_DIR=~/opt/f4pga"), true, false);
		
	//	FPGA_FAM="xc7"
		//		var processOutput = SpecsSystem.runProcess(Arrays.asList("FPGA_FAM=\"xc7\""), true, false);
	}
	
	
	public boolean isFpgaValid(String fpgaFamilyName, String fpgaHwType ) {
		
		boolean aux = false;
		
		for (int i = 0; i < list.size(); i++) {
			if((list.get(i).getFpgaFamily() == fpgaFamilyName) && (list.get(i).getHwType() == fpgaHwType)) {
				aux = true;
			}
		}
		return aux;
		
	}
	

}






