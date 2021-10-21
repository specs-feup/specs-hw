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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.GraphMapping;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.analyzers.pattern.ArithmeticExpressionMatcher.ArithmeticTemplates;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;

public class ArithmeticPatternReport extends APatternReport {
    private List<Map<ArithmeticTemplates, Integer>> foundTypes = new ArrayList<>();
    private List<HashMap<ArithmeticTemplates, List<GraphMapping<BtfVertex, DefaultEdge>>>> foundGraphs = new ArrayList<>();
    private static int lastID = 1;

    @Override
    public int getLastID() {
        return lastID;
    }

    @Override
    public void resetLastID() {
        lastID = 1;
    }

    @Override
    public void incrementLastID() {
        lastID++;
    }

    @Override
    public String toCsv() {
        var sb = new StringBuilder();

        for (int i = 0; i < getBasicBlockIDs().size(); i++) {
            var blockTypes = foundTypes.get(i);
            var bbid = getBasicBlockIDs().get(i);
            sb.append(this.name + "," + bbid);
            for (var type : ArithmeticTemplates.values()) {
                sb.append(",").append(blockTypes.get(type));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public void addEntry(HashMap<ArithmeticTemplates, List<GraphMapping<BtfVertex, DefaultEdge>>> matches) {
        foundGraphs.add(matches);
        getBasicBlockIDs().add("BB" + lastID);
        
        var map = new HashMap<ArithmeticTemplates, Integer>();
        for (var type : ArithmeticTemplates.values()) {
            if (matches.get(type) == null)
                map.put(type, 0);
            else
                map.put(type, matches.get(type).size());
        }
        foundTypes.add(map);
        
        incrementLastID();
    }

    public List<Map<ArithmeticTemplates, Integer>> getFoundTypes() {
        return foundTypes;
    }

    public List<HashMap<ArithmeticTemplates, List<GraphMapping<BtfVertex, DefaultEdge>>>> getFoundGraphs() {
        return foundGraphs;
    }
}
