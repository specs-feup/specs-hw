package pt.up.specs.cgra.pes;

import pt.up.specs.cgra.dataypes.ProcessingElementDataType;

public class AdderElement extends BinaryProcessingElement {

    // TODO: datatypes for T? bitwidths?

    // private T prevOperandA, prevOperandB;
    // private T lastResult;
    // TODO: implement these vars as members of a future parent class ProcessingElementWithMemory ?
    // define hierarchy better

    public AdderElement(int latency, int memorySize) {
        super(latency, memorySize);
    }

    @Override
    protected ProcessingElementDataType _execute() {
        return this.getOperand(0).add(this.getOperand(1));
    }
}
