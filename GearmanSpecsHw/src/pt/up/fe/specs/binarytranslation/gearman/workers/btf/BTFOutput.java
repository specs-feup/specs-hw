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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;

/**
 * 
 * Class that stores information about a successful response to a request from
 * the Binary Translation Tool Web Application.
 *  
 * @author marantesss
 *
 */
public class BTFOutput implements ABTFOutput {

    /**
     * List of Binary Segment Graphs
     */
    private final List<BinarySegmentGraph> graphs;
    
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
    public BTFOutput(List<BinarySegmentGraph> graphs) {
        this.gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        this.graphs = graphs;
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
    
    private JsonObject getJSONStamps() {
        JsonObject output = new JsonObject();
        
        output.addProperty("total", 1861);
        output.addProperty("optimized", 1861);
        output.addProperty("sequences", this.graphs.size());
        
        return output;
    }

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
            // add Unique ID
            seg.addProperty("id", g.hashCode());
            output.add(seg);
        }
        
        return output;
    }
    
    
}
