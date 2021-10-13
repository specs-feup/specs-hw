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
 
package pt.up.fe.specs.binarytranslation.flow.flat;

/*
 * Class should hold all the desired steps, including multiple transformation steps on IR (?)
 */
public class BinaryTranslationFlow {
    /*
    // TODO: the detectors and stream profilers should receive a notification of a new incoming instruction and read it,
    // instead of causing the instruction stream to advance by themselves. this allows for multiple detectors and
    // profilers to run in paralell
    
    //
    private final String filename;
    private final BinarySegmentType segtype;
    
    // stream stage
    private final InstructionStream istream;
    
    // detect stage
    private final SegmentDetector detector;
    
    // TODO: transformation steps
    // private final List<GraphTransformation> gtransforms;
    
    // TODO: hardware gen steps
    // private final HardwareGenerator hwgen;
    
    // TODO ISSUE: the stream and detector cant be passed as already initialized...
    // I think I really need enums or a config list then call the constructors based on that step list??
    
    public BinaryTranslationFlow(String filename, Class<?> streamClass, Class<?> detectorClass) {
        this.filename = filename;
        this.segtype = segtype;
        this.istream = this.detector = SegmentDetectorBuilder.buildDetector(this.istream, segtype);
    }
    
    public BinaryTranslationFlow(InstructionStream istream, SegmentDetector detector) {
        this.istream = istream;
        this.detector = detector;
    }
    
    public InstructionStream getIstream() {
        return istream;
    }
    
    public SegmentDetector getDetector() {
        return detector;
    }*/
}
