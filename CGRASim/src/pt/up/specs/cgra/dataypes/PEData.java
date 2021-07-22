package pt.up.specs.cgra.dataypes;

/**
 * Required methods for any data type which is meant to be used as data to be processed by a @ProcessingElement Any
 * PEData should only be attributed a value ONCE
 * 
 * @author nuno
 *
 */
public interface PEData {

    public PEData add(PEData operandB);

    public PEData sub(PEData operandB);

    public PEData mul(PEData operandB);

    public PEData div(PEData operandB);

    public PEData lshift(PEData operandB);

    public PEData rshift(PEData operandB);

    public PEData copy();

    // public PEData partSelect(PEData operandB);
}
