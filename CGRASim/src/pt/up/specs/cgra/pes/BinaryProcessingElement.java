package pt.up.specs.cgra.pes;

import pt.up.specs.cgra.dataypes.ProcessingElementDataType;

public interface BinaryProcessingElement extends ProcessingElement {

    public ProcessingElementDataType execute(ProcessingElementDataType operandA, ProcessingElementDataType operandB);
}
