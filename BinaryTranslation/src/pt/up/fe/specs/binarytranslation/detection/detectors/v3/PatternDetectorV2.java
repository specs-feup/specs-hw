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

import pt.up.fe.specs.binarytranslation.utils.SlidingWindow;

public class PatternDetectorV2 {

    public enum PatternState {
        PATTERN_STOPPED,
        PATTERN_STARTED,
        PATTERN_CHANGED_SIZES,
        PATTERN_UNCHANGED,
        NO_PATTERN
    }

    private PatternState state;
    private int currentPatternSize = 0;
    private int maxPatternSize;
    private List<SlidingWindow<Boolean>> fifos;
    private List<Boolean> matchBits;
    private SlidingWindow<Integer> elementHash;

    public PatternDetectorV2(int maxsize) {
        this.state = PatternState.NO_PATTERN;
        this.maxPatternSize = maxsize;

        this.fifos = new ArrayList<SlidingWindow<Boolean>>(maxsize - 1);
        for (int i = 0; i < maxsize - 1; i++) {
            fifos.add(new SlidingWindow<Boolean>(i + 1));
            fifos.get(i).reset(false);
        }

        this.matchBits = new ArrayList<Boolean>(maxsize);
        for (int i = 0; i < maxsize; i++)
            matchBits.add(false);

        this.elementHash = new SlidingWindow<Integer>(maxsize);
        this.elementHash.add(0);
    }

    public void reset() {
        this.state = PatternState.NO_PATTERN;
        this.currentPatternSize = 0;
        this.matchBits.clear();
        for (var fifo : this.fifos)
            fifo.reset(false);
        this.elementHash.clear();
        this.elementHash.add(0);
    }

    public int getCurrentPatternSize() {
        return currentPatternSize;
    }

    public int getMaxPatternSize() {
        return maxPatternSize;
    }

    private void setState(int patsize) {
        if (currentPatternSize == patsize)
            state = (patsize > 0) ? PatternState.PATTERN_UNCHANGED : PatternState.NO_PATTERN;

        else {
            if (currentPatternSize == 0) {
                currentPatternSize = patsize;
                state = PatternState.PATTERN_STARTED;
            }

            else if (patsize == 0) {
                currentPatternSize = 0;
                state = PatternState.PATTERN_STOPPED;
            }

            /*
             * Only consider a change in size if patterns gets bigger
             */
            else if (patsize > currentPatternSize) {
                currentPatternSize = patsize;
                state = PatternState.PATTERN_CHANGED_SIZES;
            }
        }
    }

    /*
     * Architecture according to file 
     * "/BinaryTranslation/src/pt/up/fe/specs/binarytranslation/"
     * "detection/detectors/v3/Diagram-for-a-hardware-implementation"
     * "-of-the-Squares-Detector-showing-modules-for-pattern_W640.jpg"
     * 
     * Found in this package
     */
    public PatternState step(int hashCode) {

        // compare current to last one
        matchBits.set(0, hashCode == elementHash.get(0));

        // compare current too "ith" element ago
        for (int i = 1; i < elementHash.getCurrentSize(); i++) {
            var ithPrevUnit = elementHash.get(i);
            var currentCompare = hashCode == ithPrevUnit;

            // "i" consecutive comparisons for a pattern of size "i" have to be
            // true for a pattern of that size to have been found
            matchBits.set(i, currentCompare && fifos.get(i - 1).getLast());
            if (!currentCompare)
                fifos.get(i - 1).reset(false);
            else
                fifos.get(i - 1).addHead(currentCompare);
        }

        // look for smallest pattern match (TODO: how to change priority)
        int idx = matchBits.indexOf(true) + 1;
        this.setState(idx);

        // add unit to window
        elementHash.addHead(hashCode);

        return this.state;
    }
}
