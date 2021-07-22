package pt.up.specs.cgra.pes;

import pt.up.specs.cgra.dataypes.PEData;

public class AdderElement extends BinaryProcessingElement {

    // TODO: datatypes for T? bitwidths?

    public AdderElement(int latency, int memorySize) {
        super(latency, memorySize);
    }

    public AdderElement(int latency) {
        this(latency, 0);
    }

    public AdderElement() {
        this(1, 0);
    }

    @Override
    protected PEData _execute() {
        return this.getOperand(0).add(this.getOperand(1));
    }

    @Override
    public ProcessingElement copy() {
        return new AdderElement(this.latency, this.memorySize);
    }
}
