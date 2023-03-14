package pt.up.specs.cgra.instructions_decoder;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import pt.up.specs.cgra.control.PEControlSetting;
import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.structure.GenericSpecsCGRA;
import pt.up.specs.cgra.structure.pes.AbstractProcessingElement;
import pt.up.specs.cgra.structure.pes.ProcessingElement;
import pt.up.specs.cgra.structure.pes.alu.ALUControlSetting;
import pt.up.specs.cgra.structure.pes.alu.ALUControlSettingObject;
import pt.up.specs.cgra.structure.pes.alu.ALUElement;
import pt.up.specs.cgra.structure.pes.alu.ALUElement.ALUOperation;

public class InstructionDecoder {
	
	GenericSpecsCGRA parent;
	
	public InstructionDecoder(GenericSpecsCGRA parent)
	{
		this.parent = parent;
	}
	
	//TODO: tokenizar string, 
	public boolean InstructionParser(String st)
	{
		StringTokenizer tk = new StringTokenizer(st);
		while (tk.hasMoreElements())
		{
			String s = tk.nextToken();
			switch(s) {
			case "SET":
				Integer x = Integer.parseInt(tk.nextToken());
				Integer y = Integer.parseInt(tk.nextToken());
				
				Integer op = Integer.parseInt(tk.nextToken());
				
				ALUElement pe = new ALUElement();
				ALUControlSettingObject aux = new ALUControlSettingObject(op);
				
				
				SET(pe, aux);
			}
		}
		return true;
	}
	
	//TODO: definir limites dos bits
	public void InstructionParser(Integer x)
	{
		
	}
	
	private <T extends AbstractProcessingElement, Y extends PEControlSetting> boolean SET(T x, Y ctrl) {
		
		x.setOperation(ctrl);
		
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
