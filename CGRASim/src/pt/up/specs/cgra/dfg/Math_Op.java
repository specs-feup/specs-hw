package pt.up.specs.cgra.dfg;

public enum Math_Op {
	
	ADD,
	SUB,
	MUL,
	DIV,
	SQR,
	INV,
	ABS;
	
	private String name;
	
	@Override
    public String toString() {
		name = name();
        return name;
    }
	

}


