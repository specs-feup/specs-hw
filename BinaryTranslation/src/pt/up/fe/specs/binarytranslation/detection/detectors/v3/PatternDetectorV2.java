package pt.up.fe.specs.binarytranslation.detection.detectors.v3;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.utils.SlidingWindow;

public class PatternDetectorV2 {

    public enum PatternState {
        PATTERN_STOPED,
        PATTERN_STARTED,
        PATTERN_CHANGED_SIZES,
        PATTERN_UNCHANGED,
        NO_PATTERN
    }

    private PatternState state;
    private int currentPatternSize = 0;
    private int maxsize;
    private List<SlidingWindow<Boolean>> fifos;
    private List<Boolean> matchBits;
    private SlidingWindow<Integer> elementHash;

    public PatternDetectorV2(int maxsize) {
        this.state = PatternState.NO_PATTERN;
        this.maxsize = maxsize;
        this.fifos = new ArrayList<SlidingWindow<Boolean>>(maxsize - 1);
        this.matchBits = new ArrayList<Boolean>(maxsize);
        this.elementHash = new SlidingWindow<Integer>(maxsize);
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
                state = PatternState.PATTERN_STOPED;
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

    public PatternState step(int hashCode) {

        // compare to existing
        matchBits.set(0, hashCode == elementHash.get(0));
        for (int i = 1; i < elementHash.getCurrentSize(); i++) {
            var ithPrevUnit = elementHash.get(i);
            var currentCompare = hashCode == ithPrevUnit;
            matchBits.set(i, currentCompare && fifos.get(i - 1).getLast());
            fifos.get(i - 1).addHead(currentCompare);
        }

        // look for smallest pattern match
        for (int i = 0; i < maxsize; i++) {
            int idx = matchBits.indexOf(true) + 1;
            this.setState(idx);
        }

        // add unit to window
        elementHash.addHead(hashCode);

        return this.state;
    }
}
