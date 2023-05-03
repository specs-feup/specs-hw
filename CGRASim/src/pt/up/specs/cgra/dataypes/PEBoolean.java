package pt.up.specs.cgra.dataypes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class PEBoolean implements PEData {

    boolean value;

    public PEBoolean(boolean x) {
        value = x;
    }

    public PEBoolean() {
        value = false;
    }

    @Override
    public Integer getValue() {
        return this.value ? 1 : 0; // a little hacky, but not critical
    }

    @Override
    public PEData copy() {
        return new PEBoolean(this.value);
    }

    @Override
    public PEData and(PEData operandB) {
        var intB = (PEBoolean) operandB;
        return new PEBoolean(this.value && intB.value);

    }

    @Override
    public PEData or(PEData operandB) {
        var intB = (PEBoolean) operandB;
        return new PEBoolean(this.value || intB.value);

    }

    @Override
    public PEData xor(PEData operandB) {
        var intB = (PEBoolean) operandB;
        return new PEBoolean(this.value ^ intB.value);
    }

    @Override
    public PEData passl(PEData operandB) {
        return new PEBoolean(this.value);
    }

    @Override
    public PEData passr(PEData operandB) {
        var intB = (PEBoolean) operandB;
        return new PEBoolean(intB.value);
    }

    // INVALID OPERATIONS:

    @Override
    public PEData add(PEData operandB) {
        throw new RuntimeException("Cannot perform add operation on boolean inputs");
    }

    @Override
    public PEData sub(PEData operandB) {
        throw new RuntimeException("Cannot perform sub operation on boolean inputs");
    }

    @Override
    public PEData mul(PEData operandB) {
        throw new RuntimeException("Cannot perform mul operation on boolean inputs");
    }

    @Override
    public PEData div(PEData operandB) {
        throw new RuntimeException("Cannot perform div operation on boolean inputs");
    }

    @Override
    public PEData lshift(PEData operandB) {
        throw new RuntimeException("Cannot perform lshift operation on boolean inputs");
    }

    @Override
    public PEData rshift(PEData operandB) {
        throw new RuntimeException("Cannot perform rshift operation on boolean inputs");
    }

    @Override
    public PEData slt(PEData operandB) {
        throw new RuntimeException("Cannot perform slt operation on boolean inputs");

    }

    @Override
    public PEData seq(PEData operandB) {
        throw new RuntimeException("Cannot perform seq operation on boolean inputs");
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31)
            .append(value)
            .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof PEBoolean))
            return false;
        if (obj == this)
            return true;

        PEBoolean pebool = (PEBoolean) obj;
        return new EqualsBuilder()
            // if deriving: appendSuper(super.equals(obj)).
            .append(pebool, pebool.value)
            .isEquals();
    }
}
