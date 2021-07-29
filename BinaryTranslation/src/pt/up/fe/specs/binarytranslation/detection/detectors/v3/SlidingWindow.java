package pt.up.fe.specs.binarytranslation.detection.detectors.v3;

import java.util.ArrayList;
import java.util.List;

public class SlidingWindow<T> {

    private int windowSize;
    private List<T> ulist;

    public SlidingWindow(int windowSize) {
        this.windowSize = windowSize;
        this.ulist = new ArrayList<T>(this.windowSize);
    }

    public T add(T unit) {

        // slide
        if (this.isFull())
            this.ulist.remove(0);

        // TODO: inefficient due to data sliding
        // re-implement with something other than "List"
        // or keep the list but with a "header" pointer
        // to implement a circular insertion list

        this.ulist.add(unit);
        return unit;
    }

    /*
     * 
     */
    public boolean isFull() {
        return (this.getCurrentSize() == this.windowSize);
    }

    /*
     * Checks if "other" exists in this window
     * (equality can be overriden as normal)
     */
    public boolean exists(T other) {
        for (var unit : this.ulist)
            if (other.equals(unit))
                return true;
        return false;
    }

    public List<T> getWindow() {
        return ulist;
    }

    public int getCurrentSize() {
        return this.ulist.size();
    }

    public int getWindowSize() {
        return windowSize;
    }

    public T get(int index) {
        return this.ulist.get(index);
    }

    public T getFromLast(int index) {
        return this.ulist.get(this.ulist.size() - 1 - index);
    }

    public T getLast() {
        return this.ulist.get(this.ulist.size() - 1);
    }

    public List<T> getRange(T from, T to) {
        var sidx = this.ulist.indexOf(from);
        var eidx = this.ulist.indexOf(to);
        return this.ulist.subList(sidx, eidx + 1);
    }

    public List<T> getRange(int from, int to) {
        return this.ulist.subList(from, to + 1);
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
}
