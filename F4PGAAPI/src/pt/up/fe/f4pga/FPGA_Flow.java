package pt.up.fe.f4pga;

import java.util.Arrays;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.Replacer;

public class FPGA_Flow {
	
	
	//get resoruce -> replace -> criar ficheiro .json
	//flow.generate(source_path, top_name)
	//topName == main method, first method to be called
	
	
	public void fpgaFlowGenerate(String sourcePath, String topName) {
		
		String i = SpecsIo.getResource(sourcePath);

		// Creating a replacer
		Replacer r = new Replacer(i);

		r.replace("counter.v", fpgaFamily);
		r.replace("arty.xdc", fpgaFamily);
		r.replace("synth.log", fpgaFamily);
		r.replace("pack.log", fpgaFamily);
		r.replace("top", fpgaFamily);
		r.replace("<default_target>", fpgaFamily);
		r.replace("<build_dir>", fpgaFamily);

		System.out.println(i);

		var processOutput = SpecsSystem.runProcess(Arrays.asList("export F4PGA_INSTALL_DIR=~/opt/f4pga"), true, false);
		
	}
	
	
	/*{
    	"default_part": "XC7A35TCSG324-1",
    	 
    	//we have default_platform defined, we can skip the --part argument. 
    	//We can also skip the --target argument because we have a default_target defined for the chosen platform.
    	 *
    	"dependencies": {
        	"sources": ["counter.v"], // use replacer here with a list of the source files
        	"xdc": ["arty.xdc"],
        	"synth_log": "synth.log",
        	"pack_log": "pack.log",
    	},
    	"values": {
        	"top": "top" 
    	},
    	"XC7A35TCSG324-1": {
        	"default_target": "<default_target>",
        	"dependencies": {
            	"build_dir": "<build_dir>"
        	}
    	}
	  }*/
	
	
	
	
	

}
