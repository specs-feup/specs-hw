package pt.up.fe.f4pga;

import java.util.ArrayList;

public class FPGA {

	String fpgaFamily;
	
	String hwType;
	

	final FPGA a1 = new FPGA("artix7", "basis3");
	final FPGA a2 = new FPGA("artix7", "arty 35t");
	final FPGA a3 = new FPGA("artix7", "arty 100t");
	final FPGA a4 = new FPGA("artix7", "nexys4 ddr");
	final FPGA a5 = new FPGA("artix7", "nexys video");
	final FPGA a6 = new FPGA("artix7", "zylo-z7");

	public FPGA(String fpgaFamily, String hwType) {
		this.fpgaFamily = fpgaFamily;
		this.hwType = hwType;
	}

	public String getFpgaFamily() {
		return fpgaFamily;
	}

	public String getHwType() {
		return hwType;
	}


	public ArrayList<FPGA> getListValidFPGA() {
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
