package pt.up.specs.cgra.dataypes;

import pt.up.specs.cgra.structure.pes.ProcessingElement;

public final class PEControlALU extends PEControl {
	
	public static enum PEDirection
	{
		N,
		NE,
		E,
		SE,
		S,
		SW,
		W,
		NW,
		ZERO//constante 0
	}
	
	
	public static enum ALU_OP
	{
		ADD,
		SUB,
		MUL,
		DIV,
		LSHIFT,
		RSHIFT,
		MOD,
		AND,
		OR,
		XOR,
		PASSL,
		PASSR,
		PASSNULL
	}
	
	protected ALU_OP operation; //ta
	protected int memoffset;//ta - memory access offset, basic implementation of virtual memory, 
//does not replace or offer any memory access protection, basically X/N, X = size of memory, N=number of inputs
	protected PEDirection inputone;
	protected PEDirection inputtwo;

	
	public PEControlALU(ProcessingElement parent, PEMemoryAccess access, ALU_OP operation, PEDirection inputone, PEDirection inputtwo) 
	{
		this.PE = parent;
		this.petype = PEType.ALU;
		this.memaccesstype = access;
		this.operation = operation;
		this.memoffset = 0;
		this.inputone = inputone;
		this.inputtwo = inputtwo;
	}
	
	public PEControlALU(ProcessingElement parent, PEMemoryAccess access, ALU_OP operation) 
	{
		this.PE = parent;
		this.petype = PEType.ALU;
		this.memaccesstype = access;
		this.operation = operation;
		this.memoffset = 0;
		this.inputone = PEDirection.ZERO;
		this.inputtwo = PEDirection.ZERO;
	}
	
	public PEControlALU()
	{
		this.PE = null;
		this.petype = PEType.ALU;
		this.memaccesstype = PEMemoryAccess.NONE;
		this.operation = ALU_OP.PASSNULL;
		this.memoffset = 0;
		this.inputone = PEDirection.ZERO;
		this.inputtwo = PEDirection.ZERO;
	}
	
	

	
	public ALU_OP getOperation() {
		return operation;
	}
	
	public void setOperation(ALU_OP operation) {
		this.operation = operation;
	}

	public int getMemoffset() {
		return memoffset;
	}

	public void setMemoffset(int memoffset) {
		this.memoffset = memoffset;
	}
	
	public PEDirection getInputone() {
		return inputone;
	}

	public void setInputone(PEDirection inputone) {
		this.inputone = inputone;
	}

	public PEDirection getInputtwo() {
		return inputtwo;
	}

	public void setInputtwo(PEDirection inputtwo) {
		this.inputtwo = inputtwo;
	}

}
