package pt.up.specs.cgra.structure.pes.unary;

import java.util.Arrays;

import pt.up.specs.cgra.structure.pes.AProcessingElement;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort;
import pt.up.specs.cgra.structure.pes.ProcessingElementPort.PEPortDirection;

public abstract class UnaryProcessingElement extends AProcessingElement {

    public UnaryProcessingElement(int latency, int memorySize) {
        super(latency, memorySize);
        this.ports = Arrays.asList(
                new ProcessingElementPort(this, PEPortDirection.input, null),
                new ProcessingElementPort(this, PEPortDirection.output, null));
    }
}
