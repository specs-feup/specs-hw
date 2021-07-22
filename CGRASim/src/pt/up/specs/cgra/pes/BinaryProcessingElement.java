package pt.up.specs.cgra.pes;

import java.util.ArrayList;

import pt.up.specs.cgra.dataypes.PEData;

public abstract class BinaryProcessingElement extends AbstractProcessingElement {

    public BinaryProcessingElement(int latency, int memorySize) {
        super(latency, memorySize);
        this.operands = new ArrayList<PEData>(2);
    }
}
