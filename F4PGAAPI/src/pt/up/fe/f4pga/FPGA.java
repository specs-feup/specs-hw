package pt.up.fe.f4pga;

import java.util.ArrayList;

public class FPGA {
	
	String fpgaFamily;
	
	String hwType;
	
	final FPGA a1 = new FPGA("basis3","artix7");
	final FPGA a2 = new FPGA("arty 35t","artix7");
	final FPGA a3 = new FPGA("arty 100t","artix7");
	final FPGA a4 = new FPGA("nexys4 ddr","artix7");
	final FPGA a5 = new FPGA("nexys video","artix7");
	final FPGA a6 = new FPGA("zylo-z7","artix7");
	
	
	
	
	
	public FPGA(String fpgaFamily, String hwType) {
		this.fpgaFamily=fpgaFamily;
		this.hwType = hwType;
	}
	
	
	public String getFpgaFamily() {
		return fpgaFamily;
	}
	
	public String getHwType() {
		return hwType;
	}
	
	public ArrayList<FPGA> getListValidFPGA(){
		ArrayList<FPGA> list = new ArrayList<>();
		list.add(a1);
		list.add(a2);
		list.add(a3);
		list.add(a4);
		list.add(a5);
		list.add(a6);
		
		return list;
		
	}
	
	
	

}
