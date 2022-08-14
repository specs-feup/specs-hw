package pt.up.fe.f4pga;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.Replacer;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		F4PGA basis3F4PGA = new F4PGA();
		basis3F4PGA.init("basis3");
		
		
		
		FPGA object = new FPGA("","");

		
		
		System.out.println("aqui");
		String i = SpecsIo.getResource("pt/up/fe/f4pga/makefile");
		//System.out.println(i);
		//Replacer r = new Replacer(() -> "pt/up/fe/f4pga/makefile");
		Replacer r = new Replacer(i);
		r.replace("basys3", "Antonio");
		r.replace("<put the name of your top module here>", "MODIFICADO");
		//System.out.println("Replaced:\n"+r.toString());
		//var processOutput = SpecsSystem.runProcess(Arrays.asList("dir"), true, false);
		//System.out.println(processOutput.getOutput());
		//String i = SpecsIo.getResource("resourceExample");
		
	}
	
	

}
