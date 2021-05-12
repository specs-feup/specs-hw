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

package pt.up.fe.specs.binarytranslation.analysis.memory.transforms;

import java.util.ArrayList;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;

public class TransformRemoveOrphanOperations extends AGraphTransform {

    public TransformRemoveOrphanOperations(Graph<AddressVertex, DefaultEdge> graph) {
        super(graph);
    }

    @Override
    protected Graph<AddressVertex, DefaultEdge> applyTransform(Graph<AddressVertex, DefaultEdge> g) {
        var toRemove = new ArrayList<AddressVertex>();
        
        for (var v : g.vertexSet()) {
            if (v.getType() == AddressVertexType.OPERATION) {
                if (g.inDegreeOf(v) == 0)
                    toRemove.add(v);
                if (g.inDegreeOf(v) == 1 && g.outDegreeOf(v) == 0)
                    toRemove.add(v);
            }
        }
        g.removeAllVertices(toRemove);
        return g;
    }

}
