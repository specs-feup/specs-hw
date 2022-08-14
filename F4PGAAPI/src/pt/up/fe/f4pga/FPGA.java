package pt.up.fe.f4pga;


public class FPGA {
	
	String fpgaFamily;
	
	String hwType;
	
	public FPGA(String hwType , String fpgaFamily) {
		this.fpgaFamily=fpgaFamily;
		this.hwType = hwType;
	}
	
	
	public String getFpgaFamily() {
		return fpgaFamily;
	}
	
	public String getHwType() {
		return hwType;
	}
	
	
	
	
	

}
