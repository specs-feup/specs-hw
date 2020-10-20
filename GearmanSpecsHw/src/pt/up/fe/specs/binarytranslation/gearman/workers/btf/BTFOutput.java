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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraph;
import pt.up.fe.specs.binarytranslation.graph.BinarySegmentGraphOutputUtils;
import pt.up.fe.specs.binarytranslation.graph.GraphBundle;
import pt.up.fe.specs.binarytranslation.graph.GraphNode;
import pt.up.fe.specs.binarytranslation.graph.edge.GraphInput;
import pt.up.fe.specs.binarytranslation.graph.edge.GraphOutput;
import pt.up.fe.specs.binarytranslation.stream.InstructionStream;
import pt.up.fe.specs.util.SpecsLogs;

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
     * Instruction Stream
     */
    private InstructionStream instructionStream;
    
    /**
     * Program Path
     */
    private String program;
    
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
        this.instructionStream = bundle.getIstream();
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
        return s.substring(1)
                .replaceAll(
           String.format("%s|%s|%s",
              "(?<=[A-Z])(?=[A-Z][a-z])",
              "(?<=[^A-Z])(?=[A-Z])",
              "(?<=[A-Za-z])(?=[^A-Za-z])"
           ),
           " "
        );
     }
    
    /**
     * 
     */
    @Override
    public byte[] getJSONBytes() {
        var outputJson = new JsonObject();
        
        outputJson.add("stamps", this.getJSONStamps());
        outputJson.add("segments", this.getJSONSegments());
        outputJson.addProperty("code", this.getCode());
                
        return gson.toJson(outputJson).getBytes();
    }
    
    /**
     * 
     * @return
     */
    private String getCode() {
        String output = new String();
        Path fileName = Path.of(this.program);
        try {
            output = Files.readString(fileName).substring(0, 3000);
        } catch (IOException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        }
         
        return output;
    }

    /**
     * 
     * @return
     */
    private JsonObject getJSONStamps() {
        JsonObject output = new JsonObject();
        
        output.addProperty("total", this.instructionStream.getNumInstructions());
        output.addProperty("optimized", this.getOptimizedInstructions());
        output.addProperty("sequences", this.graphs.size());
        
        return output;
    }
    
    /**
     * TODO Not sure if this works
     *
     * @return Number of unique instructions in all detected binary segments
     */
    private long getOptimizedInstructions() {
        List<Number> instructionsAddress = new ArrayList<>();
        
        for (var graph : this.graphs) {
            var segment = graph.getSegment();
            var startAddress = segment.getContexts().get(0).getStartaddresses();
            for (var instruction : segment.getInstructions()) {
                var address = instruction.getAddress().intValue() + startAddress;
                if (!instructionsAddress.contains(address))
                    instructionsAddress.add(address);
            }
        }
                
        return instructionsAddress.size();
    }

    /**
     * 
     * @return JsonArray with all segments
     */
    private JsonArray getJSONSegments() {
        JsonArray output = new JsonArray();

        for (var g : this.graphs) {
            // remove "nodes" JsonElement from Segment Graphs
            JsonObject seg = (JsonObject) gson.toJsonTree(g);
            seg.remove("nodes");
            // add segment type
            seg.addProperty("segmentType", splitCamelCase(g.getSegment().getClass().getSuperclass().getSimpleName()));
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
            // add occurrences
            seg.addProperty("occurrences", g.getSegment().getOccurences());
            // add Unique ID
            seg.addProperty("id", g.hashCode());
            // add dotty
            seg.addProperty("dotty", this.getDotty(g));
            output.add(seg);
        }
        
        return output;
    }
    
    /*
     * TODO DELETE THIS AND UPDATE THE METHOD ON BinarySegmentGraphOutputUtils
     * 
     * Write to a given output stream (file or stdio)
     */
    private String getDotty(BinarySegmentGraph graph) {
        
        StringBuilder sb = new StringBuilder();     
        sb.append("digraph G {\n\n");

        sb.append("graph [ dpi = 72, nodesep=\"0.1\" ];\n");
        sb.append("bgcolor=\"#ffffff00\";\n\n");

        // livein nodes
        sb.append("{ rank = source;\n");
        for (GraphInput in : graph.getLiveins()) {
            var s = in.getRepresentation();
            sb.append("\t\"in_" + s
                    + "\"[shape = box, fillcolor=\"#8080ff\", style=filled, label=\"" + s
                    + "\"];\n");
        }
        sb.append("}\n");

        sb.append("{ rank = sink;\n");
        // liveout nodes
        for (GraphOutput out : graph.getLiveouts()) {
            var s = out.getRepresentation();
            sb.append("\t\"out_" + s
                    + "\"[shape = box, fillcolor=\"#ff8080\", style=filled, label=\"" + s
                    + "\"];\n");
        }
        sb.append("}\n");

        sb.append("subgraph nodes {\n\tnode [style=filled, fillcolor=\"#ffffff\"];\n");
        for (GraphNode n : graph.getNodes()) {
            sb.append(n.rawDotty());
        }
        sb.append("}\n");

        for (int rank = 0; rank < graph.getCpl(); rank++) {
            sb.append("{ rank = same;\n");
            for (GraphNode n : graph.getNodes()) {
                if (n.getLevel() == rank)
                    sb.append("\t\"" + n.getRepresentation() + "\"\n");
            }
            sb.append("}\n");
        }
        sb.append("}\n");
        
        return sb.toString();
    }
    
    /**
     * 
     * @param number
     * @return numb with decimal places
     */
    private float getRoudedNumber(float number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Float.parseFloat(df.format(number));
    }

    /**
     * Getter for instructionStream
     * @return instructionStream
     */
    public InstructionStream getInstructionStream() {
        return this.instructionStream;
    }

    /**
     * Setter for instructionStream
     * @param instructionStream
     */
    public void setInstructionStream(InstructionStream instructionStream) {
        this.instructionStream = instructionStream;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }
    
    
}
