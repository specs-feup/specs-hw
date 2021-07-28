package pt.up.specs.cgra.structure.memory;

import pt.up.specs.cgra.dataypes.PEData;

public interface Memory {

    /*
     * 
     */
    public PEData read(int addr);

    /*
     * 
     */
    public boolean write(int addr, PEData data);
}
