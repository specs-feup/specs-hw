/**
 *  Copyright 2021 SPeCS.
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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.general.general;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jgrapht.Graph;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.BaseExporter;
import org.jgrapht.nio.ExportException;
import org.jgrapht.nio.GraphExporter;
import org.jgrapht.nio.IntegerIdProvider;

import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareFolderGenerator;
import pt.up.fe.specs.binarytranslation.hardware.testbench.VerilatorTestbenchGenerator;
import pt.up.fe.specs.util.SpecsLogs;

public class GeneralFlowGraphDOTExporter<V, E> extends BaseExporter<V, E> implements GraphExporter<V, E>{
    
    public String INDENT_BASE = "";
    protected String INDENT_INNER = "  ";
    protected final Map<V, Integer> validatedIds;
    protected int lastId;
    protected String graph_name;
    
    /** Keyword for representing strict graphs. */
    protected static final String DONT_ALLOW_MULTIPLE_EDGES_KEYWORD = "strict";
    /** Keyword for directed graphs. */
    protected static final String DIRECTED_GRAPH_KEYWORD = "digraph";
    /** Keyword for undirected graphs. */
    protected static final String UNDIRECTED_GRAPH_KEYWORD = "graph";
    /** Keyword for subgraphs*/
    protected static final String SUBGRAPH_KEYWORD = "subgraph";
    /** Edge operation for directed graphs. */
    protected static final String DIRECTED_GRAPH_EDGEOP = "->";
    /** Edge operation for undirected graphs. */
    protected static final String UNDIRECTED_GRAPH_EDGEOP = "--";
    
    // patterns for IDs
    protected static final Pattern ALPHA_DIG = Pattern.compile("[a-zA-Z_][\\w]*");
    protected static final Pattern DOUBLE_QUOTE = Pattern.compile("\".*\"");
    protected static final Pattern DOT_NUMBER = Pattern.compile("[-]?([.][0-9]+|[0-9]+([.][0-9]*)?)");
    protected static final Pattern HTML = Pattern.compile("<.*>");
    
    protected static boolean isValidID(String idCandidate) {
        return ALPHA_DIG.matcher(idCandidate).matches() || DOUBLE_QUOTE.matcher(idCandidate).matches() || DOT_NUMBER.matcher(idCandidate).matches()  || HTML.matcher(idCandidate).matches();
    }
        
    public GeneralFlowGraphDOTExporter() {
        super(new IntegerIdProvider<>());
        
        this.validatedIds = new HashMap<>();
        this.lastId = -1;
    }
    
    public GeneralFlowGraphDOTExporter(int offset) {
        super(new IntegerIdProvider<>());
        
        this.validatedIds = new HashMap<>();
        this.lastId = offset - 1;
    }
    
    protected int getLastId() {
        return this.lastId;
    }
    
    public static String generateGraphURL(String dotGraph) {
        try {
            URI uri = new URI("https://dreampuf.github.io/GraphvizOnline/#", dotGraph, null);
            return uri.toASCIIString().replace("#:", "#");
        } catch (URISyntaxException e) {
            SpecsLogs.warn("Error message:\n", e);
        }
        return "";
    }
    
    public static void generateGraphvizFile(String path, String name, String dotGraph) throws IOException {
        
        StringBuilder fileBuilder = new StringBuilder();
        File templateFile;
        
        try {
            templateFile = new File(VerilatorTestbenchGenerator.class.getClassLoader().getResource("pt/up/fe/specs/binarytranslation/cdfg/dot/graphvizRedirect.html").toURI());
        } catch (URISyntaxException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
            return;
        }
        
        Scanner templateScanner = new Scanner(templateFile);
        
        while(templateScanner.hasNextLine()) {
            fileBuilder.append(templateScanner.nextLine().replace("<INSERTURL>", generateGraphURL(dotGraph)));
        }
        
        templateScanner.close();
        
        HardwareFolderGenerator.newFile(path, name +"_dot", "html").write(fileBuilder.toString().getBytes());
    }
    
    public void emit(GeneralFlowGraph<V, E> g, String name,  Writer writer, OutputStream output_stream) {
        
        this.graph_name = name;
        
        try {
            Writer bw = new OutputStreamWriter(output_stream);
            bw.write(this.exportGraph(g, name));
            bw.flush();
            bw.close();

        } catch (IOException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        }

    }
    
    public void exportGraph(GeneralFlowGraph<V, E> g, String name,  Writer writer) {

        PrintWriter out = new PrintWriter(writer);
    
        this.graph_name = name;
        
        out.print(this.exportGraph(g, name));
        out.flush();
    }
    
    public String exportGraph(GeneralFlowGraph<V, E> g, String name) {
    
        StringBuilder graphBuilder = new StringBuilder();
        
        this.graph_name = name;
        
        graphBuilder.append(this.exportHeader(g, name));
        graphBuilder.append(this.exportGraphAttributes(g));        
        graphBuilder.append(this.exportVertexes(g));      
        graphBuilder.append(this.exportEdges(g));   
        graphBuilder.append(this.exportFooter());
        
        return graphBuilder.toString();
    }
    
    protected String exportGraphAttributes(GeneralFlowGraph<V, E> g) {
        
        StringBuilder graphAttributes = new StringBuilder();
        
        for (Entry<String, Attribute> attr : graphAttributeProvider.orElse(Collections::emptyMap).get().entrySet()){
                graphAttributes.append(INDENT_BASE + INDENT_INNER);
                graphAttributes.append(attr.getKey());
                graphAttributes.append('=');
                graphAttributes.append(attr.getValue());
                graphAttributes.append(";\n");
            }
        
        return graphAttributes.toString();
    }
   
    protected String exportVertexes(GeneralFlowGraph<V, E> g) {
        
        StringBuilder vertexesBuilder = new StringBuilder();
        
        for (V vertex : g.vertexSet()) {
         
                vertexesBuilder.append(INDENT_BASE + INDENT_INNER);
                vertexesBuilder.append(this.getVertexID(vertex));
        
                getVertexAttributes(vertex).ifPresent(m -> {vertexesBuilder.append(this.renderAttributes(m));});
        
                vertexesBuilder.append(";\n");
            }
        
       return vertexesBuilder.toString();
    }
    
    protected String exportEdges(GeneralFlowGraph<V, E> g) {
        
        String connector = this.computeConnector(g);
        StringBuilder edgeBuilder = new StringBuilder();
        
        for (E e : g.edgeSet()) {
            
            edgeBuilder.append(INDENT_BASE + INDENT_INNER);
            edgeBuilder.append(this.getVertexID(g.getEdgeSource(e)));
            edgeBuilder.append(connector);
            edgeBuilder.append(this.getVertexID(g.getEdgeTarget(e)));
    
            getEdgeAttributes(e).ifPresent(m -> { edgeBuilder.append(renderAttributes(m));});
    
            edgeBuilder.append(";\n");
        }
        
        return edgeBuilder.toString();
    }   
    
    protected String exportHeader(GeneralFlowGraph<V, E> g, String name) {
        StringBuilder headerBuilder = new StringBuilder();
        
        headerBuilder.append(INDENT_BASE);
        
        if (!g.getType().isAllowingMultipleEdges()) {
            headerBuilder.append(DONT_ALLOW_MULTIPLE_EDGES_KEYWORD).append(" ");
        }
        
        headerBuilder.append(g.getType().isDirected() ? DIRECTED_GRAPH_KEYWORD : UNDIRECTED_GRAPH_KEYWORD);
        headerBuilder.append(" ").append(this.computeGraphId(name)).append(" {\n");
        
        return headerBuilder.toString();
    }
    
    protected String exportFooter() {
        return INDENT_BASE + "}\n";
    }
    
    protected String computeConnector(GeneralFlowGraph<V, E> g) {
        StringBuilder connectorBuilder = new StringBuilder();
    
        connectorBuilder.append(" ").append(g.getType().isDirected() ? DIRECTED_GRAPH_EDGEOP : UNDIRECTED_GRAPH_EDGEOP).append(" ");
    
        return connectorBuilder.toString();
    }
    
    protected String computeGraphId(String g_name) {
        
        if (!isValidID(g_name)) {
            throw new ExportException( "Generated graph ID '" + g_name + "' is not valid with respect to the .dot language");
        }
        
        return g_name;
    }
    
    protected String getVertexID(V vertex) {

        validatedIds.putIfAbsent(vertex, ++this.lastId);

        return "v" + String.valueOf(validatedIds.get(vertex).intValue());
    }
    
    public Map<V, Integer> getVertexIDMap(){
        return this.validatedIds;
    }
    
    protected String renderAttributes(Map<String, Attribute> attributes) {
        StringBuilder attributesBuilder = new StringBuilder();
        
        
        attributesBuilder.append(" [ ");
        
        attributes.entrySet().forEach(entry -> attributesBuilder.append(renderAttribute(entry.getKey(), entry.getValue())));
             
        attributesBuilder.append("]");
        
        return attributesBuilder.toString();
    }
    
    protected String renderAttribute(String attrName, Attribute attribute) {
        StringBuilder attributeBuilder = new StringBuilder();
        
        attributeBuilder.append(attrName + "=");
        
        final String attrValue = attribute.getValue();
        if (AttributeType.HTML.equals(attribute.getType())) {
            attributeBuilder.append("<" + attrValue + ">");
        } else if (AttributeType.IDENTIFIER.equals(attribute.getType())) {
            attributeBuilder.append(attrValue);
        } else {
            attributeBuilder.append("\"" + this.escapeDoubleQuotes(attrValue) + "\"");
        }
        attributeBuilder.append(" ");
        
        return attributeBuilder.toString();
    }
    
    protected String escapeDoubleQuotes(String labelName) {
        return labelName.replaceAll("\"", Matcher.quoteReplacement("\\\""));
    }

    @Override
    @Deprecated
    public void exportGraph(Graph<V, E> g, Writer writer) {
        System.out.println("IGNORE THIS METHOD!!!");
    }
    
}
