/**
 * 
 */
package pt.up.specs.cgra.controlDeprecated;

import pt.up.specs.cgra.structure.pes.ProcessingElement;

/**
 * Abstract PEControl type class
 * @author gonsk
 *
 */
public abstract class PEControl {
	
	protected PEMemoryAccess memaccesstype;
	protected PEType petype;
	protected ProcessingElement PE;
	
	public static enum PEType
	{
		ALU,
		MUL,
		NULL
	}
	
	public static enum PEMemoryAccess
	{
		INITIAL,
		FINAL,
		NONE
	}
	
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

	public ProcessingElement getPE() {
		return PE;
	}

	public void setPE(ProcessingElement pe) {
		PE = pe;
	}

	public abstract PEDirection getInputone();
	public abstract PEDirection getInputtwo();


}
