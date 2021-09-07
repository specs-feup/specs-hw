package pt.up.fe.specs.binarytranslation.detection.detectors.v3;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.tracer.StreamUnit;

public class StreamUnitPattern {

    // TODO: starting context
    private List<StreamUnit> list;
    private int occurenceCounter = 1;

    public StreamUnitPattern(List<StreamUnit> list) {
        this.list = new ArrayList<StreamUnit>();
        for (var el : list)
            // this.list.add(el.deepCopy());
            this.list.add(el);
    }

    public void incrementOccurenceCounter() {
        this.occurenceCounter++;
    }

    public int getOccurenceCounter() {
        return occurenceCounter;
    }

    /*
     * 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof StreamUnitPattern)) {
            return false;
        }

        var pat = (StreamUnitPattern) obj;
        return this.hashCode() == pat.hashCode();
    }

    /*
     * Used during detection to keep a list of pattern occurence counts
     */
    @Override
    public int hashCode() {
        int hash = 0;
        for (var unit : this.list) {
            hash += unit.hashCode();
        }
        return hash;
    }
}
