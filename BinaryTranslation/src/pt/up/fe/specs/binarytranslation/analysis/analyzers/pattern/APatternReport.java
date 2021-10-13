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

package pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;

public abstract class APatternReport {
    protected List<Graph<BtfVertex, DefaultEdge>> graphs = new ArrayList<>();
    private List<String> basicBlockIDs = new ArrayList<>();
    protected static int lastID = 1;
    
    protected String name;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public static int getLastID() {
        return lastID;
    }
    
    public static void resetLastID() {
        lastID = 1;
    }

    public static void incrementLastID() {
        lastID++;
    }
    
    public List<Graph<BtfVertex, DefaultEdge>> getGraphs() {
        return graphs;
    }

    public List<String> getBasicBlockIDs() {
        return basicBlockIDs;
    }

    public void setBasicBlockIDs(List<String> basicBlockIDs) {
        this.basicBlockIDs = basicBlockIDs;
    }
}
