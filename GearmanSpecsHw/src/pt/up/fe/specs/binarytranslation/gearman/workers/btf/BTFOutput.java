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

import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import pt.up.fe.specs.binarytranslation.graphs.BinarySegmentGraph;

public class BTFOutput implements ABTFOutput {

    private final List<BinarySegmentGraph> graphs;
     
    public BTFOutput(List<BinarySegmentGraph> graphs) {
        this.graphs = graphs;
    }
    
    @Override
    public byte[] getJSONBytes() {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        JsonArray output = new JsonArray();
        // remove "nodes" JsonElement from Segment Graphs
        for (BinarySegmentGraph g : this.graphs) {
            JsonObject t = (JsonObject) gson.toJsonTree(g);
            t.remove("nodes");
            output.add(t);
        }
        // create return JSON MAP
        var outputMap = new HashMap<>();
        outputMap.put("segments", output);
        return gson.toJson(outputMap).getBytes();
    }
    
    
}
