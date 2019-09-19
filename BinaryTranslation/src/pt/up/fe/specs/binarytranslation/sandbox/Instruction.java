package pt.up.fe.specs.binarytranslation.sandbox;

public class Instruction {

	/*
	 * Binary and string codes for the instruction
	 */
	private int binCode;
	private String asmCode;

	public Instruction(String asmCode) {
		this.asmCode = asmCode;
		// this.binCode = TODO do reassemble here?
	}

	public Instruction(int binCode) {
		this.binCode = binCode;
		// this.asmCode = how to call proper decoder??
	}

	// public int getCycles();

}
