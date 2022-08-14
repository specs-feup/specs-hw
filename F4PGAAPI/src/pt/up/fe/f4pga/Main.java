package pt.up.fe.f4pga;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.Replacer;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
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
		
		
		
		F4PGA a = new F4PGA();
		a.init("basis3");
		
		
		
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
