package pt.up.specs.cgra.dataypes;

public final class PEControlALU extends PEControl {
	
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
	
	protected ALU_OP operation = ALU_OP.PASSNULL;
	protected int memoffset = 0;//memory access address space offset, basic implementation of virtual memory, 
	//does not replace or offer any memory access protection

	public PEControlALU(PEMemoryAccess access, ALU_OP operation) {
		this.petype = PEType.ALU;
		
		this.memaccesstype = access;
		this.operation = operation;
	}
	
	public PEControlALU()
	{
		this.petype = PEType.ALU;

		this.memaccesstype = PEMemoryAccess.NONE;
		this.operation = ALU_OP.PASSNULL;
	}

	
	public ALU_OP getOperation() {
		return operation;
	}
	
	public void setOperation(ALU_OP operation) {
		this.operation = operation;
	}
	

	public void setMemAccess(PEMemoryAccess memaccess) {
		this.memaccesstype = memaccess;
	}
	
	public PEMemoryAccess getMemAccess() {
		return memaccesstype;
	}
	
	public void setType(PEType type) {
		this.petype = type;
	}
	
	public PEType getType() {
		return petype;
	}

	public int getMemoffset() {
		return memoffset;
	}

	public void setMemoffset(int memoffset) {
		this.memoffset = memoffset;
	}



}
