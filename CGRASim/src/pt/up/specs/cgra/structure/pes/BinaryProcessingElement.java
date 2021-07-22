package pt.up.specs.cgra.structure.pes;

import java.util.Arrays;

import pt.up.specs.cgra.structure.pes.ProcessingElementPort.PEPortDirection;

public abstract class BinaryProcessingElement extends AbstractProcessingElement {

    public BinaryProcessingElement(int latency, int memorySize) {
        super(latency, memorySize);
        this.ports = Arrays.asList(
                new ProcessingElementPort(this, PEPortDirection.input, null),
                new ProcessingElementPort(this, PEPortDirection.input, null),
                new ProcessingElementPort(this, PEPortDirection.output, null));
    }
}
