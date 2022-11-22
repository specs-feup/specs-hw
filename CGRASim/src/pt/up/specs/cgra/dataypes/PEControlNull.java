package pt.up.specs.cgra.dataypes;

public class PEControlNull extends PEControl {
	
	public PEControlNull()
	{
		this.memaccesstype = PEMemoryAccess.NONE;
		this.petype = PEType.NULL;
	}

}
