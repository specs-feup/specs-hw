package pt.up.fe.specs.binarytranslation.detection.detectors.v3;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.tracer.StreamUnit;

public class StreamUnitWindow {

    private int windowSize;
    private List<StreamUnit> ulist;

    public StreamUnitWindow(int windowSize) {
        this.windowSize = windowSize;
        this.ulist = new ArrayList<StreamUnit>(this.windowSize);
    }

    public StreamUnit add(StreamUnit unit) {

        // slide
        if (this.ulist.size() == this.windowSize)
            this.ulist.remove(0);

        this.ulist.add(unit);
        return unit;
    }

    /*
     * Checks if two StreamUnits are equal
     * (trace units and static units can define their own functions for equality)
     */
    public boolean exists(StreamUnit other) {
        for (var unit : this.ulist)
            if (other.equals(unit))
                return true;
        return false;
    }

    public List<StreamUnit> getWindow() {
        return ulist;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public StreamUnit get(int index) {
        return this.ulist.get(index);
    }

    public StreamUnit getFromLast(int index) {
        return this.ulist.get(this.ulist.size() - 1 - index);
    }

    public StreamUnit getLast() {
        return this.ulist.get(this.ulist.size() - 1);
    }

    public List<StreamUnit> getRange(StreamUnit from, StreamUnit to) {
        var sidx = this.ulist.indexOf(from);
        var eidx = this.ulist.indexOf(to);
        return this.ulist.subList(sidx, eidx + 1);
    }

    /*
     * 
     */
    public int getRangeHash(int from, int to) {
        int hash = 0;
        var sublist = this.ulist.subList(from, to);
        for (var unit : sublist) {
            hash += unit.hashCode();
        }
        return hash;
    }

    /*
     * Get latest occurring instance of an instruction (by addr) in window
    
    public StreamUnit getLatestByAddr(Long addr) {
        for (int i = this.ulist.size() - 1; i >= 0; i--) {
            var lastInstInUnit = this.ulist.get(i).getEnd();
            if (lastInstInUnit.getAddress().longValue() == addr.longValue())
                return this.ulist.get(i);
        }
        return null;
    }
    
    
     * Get earliest occurring instance of an instruction (by addr) in window
    
    public StreamUnit getEarliestByAddr(Long addr) {
        for (var inst : this.ulist)
            if (inst.getAddress().longValue() == addr.longValue())
                return inst;
        return null;
    }*/
}
