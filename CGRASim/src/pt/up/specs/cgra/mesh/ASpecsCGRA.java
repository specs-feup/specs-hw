package pt.up.specs.cgra.mesh;

import java.util.ArrayList;
import java.util.List;

import pt.up.specs.cgra.pes.ProcessingElement;

public abstract class ASpecsCGRA implements SpecsCGRA {

    private List<List<ProcessingElement>> mesh;

    public ASpecsCGRA(int xDim, int yDim) {
        this.mesh = new ArrayList<List<ProcessingElement>>(xDim);
        this.mesh.forEach((list) -> {
            list = new ArrayList<ProcessingElement>(yDim);
        });
    }

    @Override
    public ProcessingElement getProcessingElement(int x, int y) {
        return this.mesh.get(x).get(y);
    }

    @Override
    public boolean setConnection(ProcessingElement from, ProcessingElement to) {
        // TODO Auto-generated method stub
        return false;
    }

}
