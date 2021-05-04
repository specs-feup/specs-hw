package pt.up.fe.specs.binarytranslation.asm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AIsaRegisterConventions implements RegisterProperties {
    protected List<String> registers;
    protected List<String> parameters;
    protected List<String> temporaries;
    protected List<String> returnVals;
    protected String stackPointer;
    protected String zero;
    
    protected void setAllRegisters(String...regs) {
        registers = Collections.unmodifiableList(Arrays.asList(regs));
    }
    
    protected void setStackPointer(String reg) {
        stackPointer = reg;
    }
    
    protected void setZero(String reg) {
        zero = reg;
    }

    protected void setParameters(String... regs) {
        parameters = Collections.unmodifiableList(Arrays.asList(regs));
    }
    
    protected void setTemporaries(String... regs) {
        temporaries = Collections.unmodifiableList(Arrays.asList(regs));
    }
    
    protected void setReturnVals(String... regs) {
        returnVals = Collections.unmodifiableList(Arrays.asList(regs));
    }
    
    public List<String> getAllRegisters() {
        return registers;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public List<String> getTemporaries() {
        return temporaries;
    }

    public List<String> getReturnVals() {
        return returnVals;
    }

    public String getStackPointer() {
        return stackPointer;
    }
    
    public String getZero() {
        return zero;
    }
    
    public boolean isStackPointer(String reg) {
        return stackPointer.equals(reg);
    }
    
    public boolean isTemporary(String reg) {
        return temporaries.contains(reg);
    }
    
    public boolean isParameter(String reg) {
        return parameters.contains(reg);
    }
    
    public boolean isZero(String reg) {
        return zero.equals(reg);
    }
    
    public boolean isReturn(String reg) {
        return returnVals.equals(reg);
    }
}

