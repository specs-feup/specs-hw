package pt.up.specs.cgra.structure.pes;

import java.util.List;

import pt.up.specs.cgra.dataypes.PEControl;
import pt.up.specs.cgra.dataypes.PEControlNull;

public class NullProcessingElement implements ProcessingElement {
	
	PEControl control;

	@Override
	public List<ProcessingElementPort> getPorts() {
		return null;
	}
	
	public NullProcessingElement()
	{
		this.control = new PEControlNull();
	}

}
