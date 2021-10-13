/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
package pt.up.fe.specs.binarytranslation.utils;

import java.util.ArrayList;
import java.util.List;

public class SlidingWindow<T> {

    private int windowSize;
    private List<T> ulist;

    public SlidingWindow(int windowSize) {
        this.windowSize = windowSize;
        this.ulist = new ArrayList<T>(this.windowSize);
    }

    /*
     * 
     */
    public void clear() {
        this.ulist.clear();
    }

    /*
     * 
     */
    public void reset(T val) {
        this.clear();
        for (int i = 0; i < windowSize; i++)
            this.ulist.add(val);
    }

    /*
     * 
     */
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
     * Insert into head, like a push queue
     */
    public T addHead(T unit) {

        // slide
        if (this.isFull())
            this.ulist.remove(this.ulist.size() - 1);

        // TODO: inefficient due to data sliding
        // re-implement with something other than "List"
        // or keep the list but with a "header" pointer
        // to implement a circular insertion list

        this.ulist.add(0, unit);
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
