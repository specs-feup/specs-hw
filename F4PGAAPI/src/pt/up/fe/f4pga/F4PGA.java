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

	final List<FPGA> list = new ArrayList<>();

	public void init(String fpgaHwType) throws Exception {

		// Gets FPGA Family according to FPGA Hardware type
		String fpgaFamily = getFpgaFamily(fpgaHwType);

		// Checking if FPGA is supported by F4PGA
		boolean aux = isFpgaValid(fpgaHwType, fpgaFamily);

		if (aux == false) {
			throw new Exception("Invalid FPGA");
		}

		// Saving FPGA in a class variable
		validFPGA = new FPGA(fpgaHwType, fpgaFamily);

		// getting the resource (f4pga_config.sh)
		String i = SpecsIo.getResource("pt/up/fe/f4pga/f4pga_config.sh");

		// Creating a replacer
		Replacer r = new Replacer(i);

		r.replace("<FPGA_FAM>", fpgaFamily);

		System.out.println(i);

		var processOutput = SpecsSystem.runProcess(Arrays.asList("export F4PGA_INSTALL_DIR=~/opt/f4pga"), true, false);

		// FPGA_FAM="xc7"
		// var processOutput = SpecsSystem.runProcess(Arrays.asList("FPGA_FAM=\"xc7\""),
		// true, false);
	}

	public boolean isFpgaValid(String fpgaFamilyName, String fpgaHwType) {

		boolean aux = false;

		for (int i = 0; i < list.size(); i++) {
			if ((list.get(i).getFpgaFamily().equalsIgnoreCase(fpgaFamilyName)) && (list.get(i).getHwType().equalsIgnoreCase(fpgaHwType))) {
				aux = true;
			}
		}
		return aux;

	}

	public String getFpgaFamily(String hwType) {

		String fpgaFamily = "";

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getFpgaFamily().equalsIgnoreCase(hwType)) {
				fpgaFamily = list.get(i).getFpgaFamily();
			}
		}

		return fpgaFamily;

	}

	public void build(String buildDir) {
		String target = validFPGA.getHwType();

		String buildCmd = "TARGET=/" + target + " make -C " + buildDir;
		var processOutput = SpecsSystem.runProcess(Arrays.asList(buildCmd), true, false);

	}
	
	
	
	

}
