/**
 *  Copyright 2020 SPeCS.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package pt.up.fe.specs.binarytranslation.gearman.workers.btf;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graphs.GraphBundle;

/**
 * 
 * Class that stores information about a successful response to a request from
 * the Binary Translation Tool Web Application.
 *  
 * @author marantesss
 *
 */
public class BTFOutput implements IBTFOutput {

    /**
     * List of Binary Segment Graphs
     */
    private final List<BinarySegmentGraph> graphs;
    
    /**
     * Total number of instructions
     */
    private long totalInstructions;
    
    /**
     * GSON
     */
    private final Gson gson;
     
    /**
     * Default Constructor
     */
    public BTFOutput() {
        this.gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        this.graphs = new ArrayList<>();
    }
    
    /**
     * Constructor given an already set list of graphs
     * 
     * @param graphs List of Binary Segment Graphs
     */
    public BTFOutput(GraphBundle bundle) {
        this.gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        this.graphs = bundle.getGraphs();
    }
    
    /**
     * Adds Binary Segment Graphs provided in a list to the graph list
     * 
     * @param graphs New Binary Segment Graphs to add to the graph list
     */
    public void addGraphs(List<BinarySegmentGraph> graphs) {
        this.graphs.addAll(graphs);
    }
    
    /**
     * Sort Binary Segment Graphs based on smallest address
     */
    public void sortAddresses() {
        this.graphs.sort((g1, g2) ->
            g1.getSegment().getContexts().get(0).getStartaddresses() - g2.getSegment().getContexts().get(0).getStartaddresses());
    }
    
    
    /**
     * TODO Figure out if this is useful or not
     * @param s
     * @return
     */
    private static String splitCamelCase(String s) {
        return s.replaceAll(
           String.format("%s|%s|%s",
              "(?<=[A-Z])(?=[A-Z][a-z])",
              "(?<=[^A-Z])(?=[A-Z])",
              "(?<=[A-Za-z])(?=[^A-Za-z])"
           ),
           " "
        );
     }
    
    @Override
    public byte[] getJSONBytes() {
        var outputJson = new JsonObject();
        
        outputJson.add("stamps", this.getJSONStamps());
        outputJson.add("segments", this.getJSONSegments());
                
        return gson.toJson(outputJson).getBytes();
    }
    
    /**
     * 
     * @return
     */
    private JsonObject getJSONStamps() {
        JsonObject output = new JsonObject();
        
        output.addProperty("total", this.totalInstructions);
        output.addProperty("optimized", this.getOptimizedInstructions());
        output.addProperty("sequences", this.graphs.size());
        
        return output;
    }
    
    /**
     * TODO Does not work
     * @return
     */
    private long getOptimizedInstructions() {
        List<Number> instructionsAddress = new ArrayList<>();
        
        for (var graph : this.graphs) {
            var segment = graph.getSegment();
            for (var instruction : segment.getInstructions()) {
                var address = instruction.getAddress();
                if (!instructionsAddress.contains(address))
                    instructionsAddress.add(address);
            }
        }
                
        return instructionsAddress.size();
    }

    /**
     * 
     * @return
     */
    private JsonArray getJSONSegments() {
        JsonArray output = new JsonArray();

        for (var g : this.graphs) {
            // remove "nodes" JsonElement from Segment Graphs
            JsonObject seg = (JsonObject) gson.toJsonTree(g);
            seg.remove("nodes");
            // add segment type
            seg.addProperty("segmentType", splitCamelCase(g.getSegment().getClass().getSimpleName()));
            // add number of instructions
            var instructions = g.getSegment().getInstructions();
            seg.addProperty("instructions", instructions.size());
            // add code
            var instList = g.getSegment().getInstructions();
            var code = "";
            for (var inst : instList) {
                code += inst.getRepresentation() + "\n";
            }
            seg.addProperty("code", code);
            // Graph
            var bitmapfilename = g.getOutputFolderName() + ".png";
            seg.addProperty("graph", bitmapfilename);
            // add start address
            var startAddress = g.getSegment().getContexts().get(0).getStartaddresses();
            seg.addProperty("startAddress", String.format("0x%s", startAddress));
            // add coverage
            seg.addProperty("staticCoverage", this.getRoudedNumber(g.getSegment().getStaticCoverage() * 100));
            seg.addProperty("dynamicCoverage", this.getRoudedNumber(g.getSegment().getDynamicCoverage() * 100));
            // TODO add occurences
            //seg.addProperty("occurences", g.getSegment());
            // add Unique ID
            seg.addProperty("id", g.hashCode());
            output.add(seg);
        }
        
        return output;
    }
    
    private float getRoudedNumber(float number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Float.parseFloat(df.format(number));
    }

    public long getTotalInstructions() {
        return totalInstructions;
    }

    public void setTotalInstructions(long totalInstructions) {
        this.totalInstructions = totalInstructions;
    }
    
    
}
