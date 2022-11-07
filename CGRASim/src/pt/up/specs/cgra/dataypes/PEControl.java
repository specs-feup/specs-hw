/**
 * 
 */
package pt.up.specs.cgra.dataypes;

import pt.up.specs.cgra.dataypes.PEControl.PEType;

/**
 * Abstract PEControl type class
 * @author gonsk
 *
 */
public abstract class PEControl {
	
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
	
	protected PEMemoryAccess memaccesstype;
	protected PEType petype;

}
