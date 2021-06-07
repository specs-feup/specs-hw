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

import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex;
import pt.up.fe.specs.binarytranslation.analysis.memory.AddressVertex.AddressVertexType;

public class GraphTemplateType11 extends AGraphTemplate {

    protected GraphTemplateType11(GraphTemplateType type) {
        super(type);
        var r7 = new AddressVertex("r7", AddressVertexType.REGISTER);
        var imm64 = new AddressVertex("64", AddressVertexType.IMMEDIATE);
        var r5 = new AddressVertex("r5", AddressVertexType.REGISTER);
        var add0 = new AddressVertex("+", AddressVertexType.OPERATION);
        var add = new AddressVertex("+", AddressVertexType.OPERATION);
        
        graph.addVertex(r7);
        graph.addVertex(imm64);
        graph.addVertex(r5);
        graph.addVertex(add0);
        graph.addVertex(add);
        
        graph.addEdge(r7, add0);
        graph.addEdge(r5, add0);
        graph.addEdge(add0, add);
        graph.addEdge(imm64, add);
    }
    
}
