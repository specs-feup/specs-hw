package pt.up.specs.cgra.structure.pes.unary;

import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.structure.pes.ProcessingElement;
import pt.up.specs.cgra.structure.pes.binary.AdderElement;

public class UnaryBitwiseOr extends UnaryProcessingElement {

    public UnaryBitwiseOr(int latency, int memorySize) {
        super(latency, memorySize);
    }

    public UnaryBitwiseOr(int latency) {
        this(latency, 0);
    }

    public UnaryBitwiseOr() {
        this(1, 0);
    }

	@Override
	protected PEData _execute() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public ProcessingElement copy() {
        return new AdderElement(this.getLatency(), this.getMemorySize());
    }

    @Override
    public String toString() {
        return "UnaryBitwiseOr";
    }
}
