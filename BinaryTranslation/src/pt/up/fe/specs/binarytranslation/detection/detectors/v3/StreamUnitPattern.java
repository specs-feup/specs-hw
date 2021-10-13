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
 
package pt.up.fe.specs.binarytranslation.detection.detectors.v3;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.tracer.StreamUnit;

public class StreamUnitPattern {

    // TODO: starting context
    private final List<StreamUnit> list;
    private int occurenceCounter = 1;

    public StreamUnitPattern(List<StreamUnit> list) {
        this.list = new ArrayList<StreamUnit>();
        for (var el : list)
            // this.list.add(el.deepCopy());
            this.list.add(el);
    }

    public int getPatternSize() {
        return this.list.size();
    }

    public void incrementOccurenceCounter() {
        this.occurenceCounter++;
    }

    public int getOccurenceCounter() {
        return occurenceCounter;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (var unit : this.list) {
            sb.append(unit.toString());
        }
        return sb.toString();
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
