package pt.up.specs.cgra.structure.pes;

import pt.up.specs.cgra.dataypes.PEData;

/**
 * Used to occupy a mesh slot while nothing is placed there
 */
public class NullPE implements ProcessingElement {

    @Override
    public ProcessingElement copy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLatency() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean hasMemory() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isReady() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isExecuting() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getExecuteCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean setOperand(int opIndex, PEData op) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean setResultRegister(int regIndex) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public PEData execute() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMemorySize() {
        // TODO Auto-generated method stub
        return 0;
    }

}
