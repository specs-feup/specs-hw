package pt.up.specs.cgra.dataypes;

import pt.up.specs.cgra.dataypes.PEControl.PEDirection;

public class PEControlNull extends PEControl {
	
	protected PEDirection inputone;
	protected PEDirection inputtwo;
	
	public PEControlNull()
	{
		this.memaccesstype = PEMemoryAccess.NONE;
		this.petype = PEType.NULL;
		this.inputone = PEDirection.ZERO;
		this.inputtwo = PEDirection.ZERO;

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
