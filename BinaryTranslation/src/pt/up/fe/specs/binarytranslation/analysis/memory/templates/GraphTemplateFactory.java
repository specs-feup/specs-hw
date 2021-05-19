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

package pt.up.fe.specs.binarytranslation.analysis.memory.templates;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;

public class GraphTemplateFactory {
    public static Graph<AddressVertex, DefaultEdge> getTemplate(GraphTemplateType type) {
        switch(type) {
        case TYPE_1:
            return new GraphTemplateType1().getGraph();
        case TYPE_2:
            return new GraphTemplateType2().getGraph();
        case TYPE_0:
            return null;
        default:
            return null;
        }
    }
}
