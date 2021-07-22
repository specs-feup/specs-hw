package pt.up.specs.cgra.dataypes;

public class PEInteger implements PEData {

    int value = 0;

    public PEInteger(int value) {
        this.value = value;
    }

    @Override
    public PEData copy() {
        return new PEInteger(this.value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public PEData add(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value + intB.getValue());
    }

    @Override
    public PEData sub(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value - intB.getValue());
    }

    @Override
    public PEData mul(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value * intB.getValue());
    }

    @Override
    public PEData div(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value / intB.getValue());
    }

    @Override
    public PEData lshift(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value << intB.getValue());
    }

    @Override
    public PEData rshift(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value >> intB.getValue());
    }

    /*
    @Override
    public PEData partSelect(PEData operandB) {
        var intB = (PEInteger) operandB;
        return new PEInteger(this.value >> intB.getValue());
    }*/
}
