package pt.up.specs.cgra.dataypes;

public interface ProcessingElementDataType {

    public ProcessingElementDataType add(ProcessingElementDataType operandB);

    public ProcessingElementDataType sub(ProcessingElementDataType operandB);

    public ProcessingElementDataType mul(ProcessingElementDataType operandB);

    public ProcessingElementDataType div(ProcessingElementDataType operandB);

    public ProcessingElementDataType lshift(ProcessingElementDataType operandB);

    public ProcessingElementDataType rshift(ProcessingElementDataType operandB);

    public ProcessingElementDataType partSelect(ProcessingElementDataType operandB);

}
