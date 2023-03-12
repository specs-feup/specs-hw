package pt.up.specs.cgra.instructions_decoder;

import pt.up.specs.cgra.control.PEControlSetting;
import pt.up.specs.cgra.structure.GenericSpecsCGRA;
import pt.up.specs.cgra.structure.pes.AbstractProcessingElement;

public class InstructionDecoder {
	
	GenericSpecsCGRA parent;
	
	public InstructionDecoder(GenericSpecsCGRA x)
	{
		this.parent = x;
	}
	
	public void InstructionParser(String x)
	{
		
	}
	
	private <T extends AbstractProcessingElement, Y extends PEControlSetting> boolean SET(T x, Y ctrl) {
		return true;
		
	}
	
	private <T extends AbstractProcessingElement> boolean SET_IO() {
		return true;
		
	}
	
	private boolean GEN_INFO() {
		return true;//return cgra.toString?
		
	}
	
	private boolean PE_INFO() {
		return true;//return pe.toString?
		
	}
	
	private boolean MEM_INFO() {
		return true;
		
	}
	
	private boolean STORE_CTX() {
		return true;
		
	}
	
	private boolean SWITCH_CTX() {
		return true;
		
	}
	
	private boolean RUN() {
		return true;
		
	}
	
	private boolean PAUSE() {
		return true;
		
	}
	
	private boolean RESET() {
		return true;
		
	}

}
