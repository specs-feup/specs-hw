package pt.up.fe.f4pga;


import java.io.File;
import java.util.Arrays;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.Replacer;

public class F4PGA {
	
	String familyName;
	
	//init API recebe tipo de familia
	//fazer build
	public void init(String fpgaFamilyName) throws Exception {
		
		boolean aux = testFamilyName(fpgaFamilyName);
		
		if(aux == false) {
			throw new Exception("Invalid FPGA Family Name");
		}
		
		familyName = fpgaFamilyName;
		
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
	
	
	private boolean testFamilyName(String familyName) {
		
		if(!familyName.equalsIgnoreCase("xc7") && !familyName.equalsIgnoreCase("eos-s3")) {
			return false;
		}
	
		return true;
		
	}
	

}
