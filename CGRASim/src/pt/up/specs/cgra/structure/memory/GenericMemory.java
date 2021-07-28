package pt.up.specs.cgra.structure.memory;

import java.util.HashMap;

import pt.up.specs.cgra.dataypes.PEData;
import pt.up.specs.cgra.dataypes.PEInteger;

public class GenericMemory implements Memory {

    private int memsize;
    private HashMap<Integer, PEData> mem;

    public GenericMemory(int memsize) {
        this.memsize = memsize;
        this.mem = new HashMap<Integer, PEData>();
        for (int i = 0; i < this.memsize; i++)
            this.mem.put(Integer.valueOf(i), new PEInteger(0));
    }

    @Override
    public PEData read(int addr) {
        return this.mem.get(Integer.valueOf(addr));
    }

    @Override
    public boolean write(int addr, PEData data) {
        if (addr > this.memsize)
            return false;
        this.mem.put(Integer.valueOf(addr), data);
        return true;
    }
}
