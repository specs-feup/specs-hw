package pt.up.specs.cgra.pes.integer;

import pt.up.specs.cgra.dataypes.ProcessingElementDataType;
import pt.up.specs.cgra.pes.AbstractProcessingElement;
import pt.up.specs.cgra.pes.BinaryProcessingElement;

public class AdderElement extends AbstractProcessingElement implements BinaryProcessingElement {

    // TODO: datatypes for T? bitwidths?

    // private T prevOperandA, prevOperandB;
    // private T lastResult;
    // TODO: implement these vars as members of a future parent class ProcessingElementWithMemory ?
    // define hierarchy better

    public AdderElement() {

    }

    public ProcessingElementDataType execute(ProcessingElementDataType operandA, ProcessingElementDataType operandB) {
        return operandA.add(operandB);
    }
}
