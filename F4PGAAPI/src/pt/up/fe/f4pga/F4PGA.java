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

	public void init(String fpgaHwType) throws Exception {

		// Checking if FPGA is supported by F4PGA
		FPGA fpgaObject = new FPGA("", "");
		FPGA ValidFPGA = fpgaObject.getFPGA(fpgaHwType);

		String fpgaFamilyName = ValidFPGA.getFpgaFamily();

		// getting the resource (f4pga_config.sh)
		String config_resource = SpecsIo.getResource("pt/up/fe/f4pga/f4pga_config.sh");

		// Creating a replacer
		Replacer init_replacer = new Replacer(config_resource);

		init_replacer.replace("<FPGA_FAM>", fpgaFamilyName);

		System.out.println(config_resource);

		var processOutput = SpecsSystem.runProcess(Arrays.asList("export F4PGA_INSTALL_DIR=~/opt/f4pga"), true, false);

		// FPGA_FAM="xc7"
		// var processOutput = SpecsSystem.runProcess(Arrays.asList("FPGA_FAM=\"xc7\""),
		// true, false);
	}

}
