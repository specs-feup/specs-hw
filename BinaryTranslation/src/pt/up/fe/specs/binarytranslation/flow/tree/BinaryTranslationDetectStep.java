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
 
package pt.up.fe.specs.binarytranslation.flow.tree;

import java.lang.reflect.Constructor;

import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentDetector;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

public class BinaryTranslationDetectStep extends BinaryTranslationStep {

    private Class<?> detectorClass;
    private SegmentDetector detector;
    private SegmentBundle bundle;

    public BinaryTranslationDetectStep(Class<?> detectorClass) {
        this.detectorClass = detectorClass;
    }

    @Override
    protected BinaryTranslationStep copyPrivate() {
        return new BinaryTranslationDetectStep(this.detectorClass);
    }

    @Override
    public void execute() {

        /*
         * Get the istream from parent node (?)
         */
        InstructionStream istream = null;
        var parent = this.getParent();
        if (parent instanceof BinaryTranslationStreamOpen) {
            istream = ((BinaryTranslationStreamOpen) parent).getStream();

            // TODO fix! create new BinaryTranslationRunException (?)
        } else {
            throw new RuntimeException();
        }

        /*
         * Construct the detector
         */
        Constructor<?> consDetector;
        try {
            consDetector = detectorClass.getConstructor(InstructionStream.class);
            this.detector = (SegmentDetector) consDetector.newInstance();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /*
         * Execute it
         */
        this.bundle = detector.detectSegments(istream);
    }

    public SegmentBundle getBundle() {
        return this.bundle;
    }
}
