/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.binarytranslation.graphs;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import com.google.gson.annotations.Expose;

import pt.up.fe.specs.binarytranslation.BinaryTranslationOutput;
import pt.up.fe.specs.binarytranslation.asm.Application;
import pt.up.fe.specs.binarytranslation.detection.detectors.SegmentBundle;
import pt.up.fe.specs.binarytranslation.detection.segments.BinarySegment;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;

/**
 * This class represents a group of @BinarySegmentGraph objects, which have been generated by translating a list
 * of @BinarySegment (possibly contained within a @SegmentBundle
 * 
 * @author Nuno
 *
 */
public class GraphBundle implements BinaryTranslationOutput {

    @Expose
    private final Date date;

    @Expose // the originating instruction stream
    private InstructionStream istream;

    // list of graphs created from input segments
    private final List<BinarySegmentGraph> graphs;

    // originating segments (ordered according to graph list) // or at least should be....
    private final List<BinarySegment> segments;

    private GraphBundle(SegmentBundle segbundle, List<BinarySegmentGraph> graphs) {
        this.date = segbundle.getDate();
        this.istream = segbundle.getIstream();
        this.graphs = graphs;
        this.segments = segbundle.getSegments();
    }

    /*
     * Returns average ideal instructions per clock cycle
     */
    public float getAverageIPC() {
        return getAverageIPC(null);
    }

    /*
     * Returns average ideal instructions per clock cycle, with predication
     */
    public float getAverageIPC(Predicate<BinarySegmentGraph> predicate) {
        float avg = 0;
        if (predicate != null)
            for (BinarySegmentGraph seg : this.graphs) {
                if (predicate.test(seg))
                    avg += seg.getEstimatedIPC();
            }
        else
            for (BinarySegmentGraph seg : this.graphs) {
                avg += seg.getEstimatedIPC();
            }
        return avg /= this.graphs.size();
    }

    public Application getApplicationInformation() {
        return this.istream.getApplicationInformation();
    }

    public Date getDate() {
        return date;
    }

    public InstructionStream getIstream() {
        return istream;
    }

    public List<BinarySegmentGraph> getGraphs() {
        return graphs;
    }

    public List<BinarySegment> getSegments() {
        return segments;
    }

    /*
     * Returns graphs based on any given predicate applied over a single BinarySegmentGraph
     */
    public List<BinarySegmentGraph> getGraphs(Predicate<BinarySegmentGraph> predicate) {

        var list = new ArrayList<BinarySegmentGraph>();
        for (var seg : this.graphs) {
            if (predicate.test(seg))
                list.add(seg);
        }
        return list;
    }

    /*
     * Generate output for a set (or all) graphs
     */
    @Override
    public void generateOutput() {
        generateOutput(this.getOutputFolder(), data -> true);
    }

    @Override
    public void generateOutput(File parentfolder) {
        generateOutput(parentfolder, data -> true);
    }

    public void generateOutput(Predicate<BinarySegmentGraph> predicate) {
        generateOutput(this.getOutputFolder(), predicate);
    }

    public void generateOutput(File parentfolder, Predicate<BinarySegmentGraph> predicate) {

        this.toJSON(parentfolder);

        var bsgfolder = new File(parentfolder, "bsg");
        bsgfolder.mkdir();
        for (BinarySegmentGraph g : this.graphs)
            if (predicate.test(g))
                g.generateOutput(new File(bsgfolder, g.getOutputFolderName()));
    }

    @Override
    public void toJSON(File outputfolder) {

        // first do self
        BinaryTranslationOutput.super.toJSON(outputfolder);

        // then list of graphs
        var bsgfolder = new File(outputfolder, "bsg");
        for (var g : this.getGraphs()) {
            g.toJSON(new File(bsgfolder, g.getOutputFolderName()));
        }
    }

    /*
     * Returns average ideal instructions per clock cycle, 
     * weighed by the number of occurrences of each segment
     */
    /*public int getWeigthedAverageIPC() {
    
    }*/

    /*
     * Static "constructor" creates a new GraphBundle from a SegmentBundle
     */
    public static GraphBundle newInstance(SegmentBundle bund) {
        var graphs = new ArrayList<BinarySegmentGraph>();
        for (BinarySegment seg : bund.getSegments()) {
            graphs.add(BinarySegmentGraph.newInstance(seg));
        }

        return new GraphBundle(bund, graphs);
    }

    /*
     * Static "constructor" creates a new GraphBundle from a single BinarySegment
     
    public static GraphBundle newInstance(BinarySegment seg) {
        var graphs = new ArrayList<BinarySegmentGraph>();
        graphs.add(BinarySegmentGraph.newInstance(seg));
        return new GraphBundle(graphs, seg.getAppinfo());
    }
    
    /*
     * Static "constructor" creates a new GraphBundle from a list of SegmentBundle
     
    public static GraphBundle newInstance(List<BinarySegment> segs) {
        var graphs = new ArrayList<BinarySegmentGraph>();
        for (BinarySegment seg : segs) {
            graphs.add(BinarySegmentGraph.newInstance(seg));
        }
    
        return new GraphBundle(graphs, segs.get(0).getAppinfo());
    }*/
}
