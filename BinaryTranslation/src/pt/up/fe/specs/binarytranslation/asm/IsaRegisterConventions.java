package pt.up.fe.specs.binarytranslation.asm;

import java.util.Arrays;
import java.util.List;

public abstract class IsaRegisterConventions {    
    protected List<String> parameters;
    protected List<String> temporaries;
    protected List<String> returnVals;
    protected String stackPointer;
    protected String zero;
    
    protected void setStackPointer(String reg) {
        this.stackPointer = reg;
    }
    
    protected void setZero(String reg) {
        zero = reg;
    }

    protected void setParameters(String... regs) {
        parameters = Arrays.asList(regs);
    }
    
    protected void setTemporaries(String... regs) {
        temporaries = Arrays.asList(regs);
    }
    
    protected void setReturnVals(String... regs) {
        returnVals = Arrays.asList(regs);
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
}

