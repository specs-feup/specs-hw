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
 
package pt.up.fe.specs.binarytranslation.detection.detectors;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.BinaryTranslationOutput;
import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.detection.detectors.DetectorConfiguration.DetectorConfigurationBuilder;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.binarytranslation.stream.InstructionStreamType;

/**
 * Segment detectors should return a single object of this type. A bundle contains the list of segments, and data about
 * the instruction stream that generated them, including nr instructions, nr cycles, architecture type, etc
 * 
 * @author nuno
 *
 */
public class SegmentBundle implements BinaryTranslationOutput {

    @Expose
    private final Date date;

    @Expose // the originating instruction stream
    private InstructionStream istream;

    private final List<BinarySegment> segments;

    private final DetectorConfiguration config;

    public SegmentBundle(List<BinarySegment> segments, InstructionStream istream) {
        this(segments, istream, DetectorConfigurationBuilder.defaultConfig());
    }

    public SegmentBundle(List<BinarySegment> segments,
            InstructionStream istream, DetectorConfiguration config) {

        this.istream = istream;
        this.segments = segments;
        this.date = new Date(System.currentTimeMillis());
        this.config = config;

        /*
         * For all segments, compute their coverage
         */
        for (var seg : this.segments) {

            seg.setStaticCoverage((float) (seg.getLatency()
                    * seg.getContexts().size()) / this.istream.getCycles());

            if (this.istream.getType() == InstructionStreamType.TRACE)
                seg.setDynamicCoverage((float) seg.getExecutionCycles() / this.istream.getCycles());
        }
    }

    /*
     * Returns basic info string for this bundle
     */
    public String getSummary() {
        return "Bundle Summary: \n" +
                "Application: " + this.getApplication().getAppName() + "\n" +
                "Architecture: " + this.getApplication().get(Application.CPUNAME) + "\n" +
                "Stream type: " + this.istream.getType() + "\n" +
                "Segment Type:" + this.segments.get(0).getSegmentType() + "\n" +
                "Num segments: " + this.segments.size() + "\n";
    }

    /*
     * Returns all segments in this bundle
     */
    public List<BinarySegment> getSegments() {
        return segments;
    }

    /*
     * Returns segments based on any given predicate applied over a single BinarySegment
     */
    public List<BinarySegment> getSegments(Predicate<BinarySegment> predicate) {

        var list = new ArrayList<BinarySegment>();
        for (var seg : this.segments) {
            if (predicate.test(seg))
                list.add(seg);
        }
        return list;
    }

    public Application getApplication() {
        return this.istream.getApp();
    }

    public Date getDate() {
        return date;
    }

    public InstructionStream getIstream() {
        return istream;
    }

    public DetectorConfiguration getConfig() {
        return config;
    }

    @Override
    public void toJSON(File outputfolder) {

        // first do self
        BinaryTranslationOutput.super.toJSON(outputfolder);

        // then list of segments
        var segfolder = new File(outputfolder, "segments");
        for (var s : this.getSegments()) {
            s.toJSON(new File(segfolder, s.getOutputFolderName()));
        }
    }

    public void printBundle() {
        for (var bs : this.getSegments()) {
            bs.printSegment();
            System.out.print("\n");
        }
    }

    public void printBundle(Predicate<BinarySegment> predicate) {
        for (var bs : this.getSegments(predicate)) {
            bs.printSegment();
            System.out.print("\n");
        }
    }

    // TODO methods that can compute the coverage and/or acceleration only for a filtered set of this bundle
    // look up what kind of java trickery can be used to do this

    /*
     * For static: Return the percentage of the ELF code the segments detected represent
     * For trace: Return the percentage of the executed instructions the segments detected represent
     * NOTE: only considers segments of size "segmentSize", to avoid overlap
     */
    /*public float getCoverage(int segmentSize) {
    
        Integer detectedportion = 0;
        for (BinarySegment seg : this.segments) {
            if (seg.getSegmentLength() == segmentSize)
                detectedportion += seg.getExecutionCycles();
        }
        return (float) detectedportion / this.totalCycles;
    }*/
}
