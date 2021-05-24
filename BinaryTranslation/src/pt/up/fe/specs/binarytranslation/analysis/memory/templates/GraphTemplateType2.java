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

public class GraphTemplateType2 extends AGraphTemplate {

    protected GraphTemplateType2(GraphTemplateType type) {
        super(type);
        var r7 = new AddressVertex("r7", AddressVertexType.REGISTER);
        var imm4 = new AddressVertex("4", AddressVertexType.IMMEDIATE);
        var r5 = new AddressVertex("r5", AddressVertexType.REGISTER);
        var mult = new AddressVertex("*", AddressVertexType.OPERATION);
        var add1 = new AddressVertex("+", AddressVertexType.OPERATION);
        var imm16 = new AddressVertex("64", AddressVertexType.IMMEDIATE);
        var add2 = new AddressVertex("+", AddressVertexType.OPERATION);
        
        graph.addVertex(r7);
        graph.addVertex(imm4);
        graph.addVertex(r5);
        graph.addVertex(mult);
        graph.addVertex(add1);
        graph.addVertex(imm16);
        graph.addVertex(add2);
        
        graph.addEdge(r7, mult);
        graph.addEdge(imm4, mult);
        graph.addEdge(mult, add1);
        graph.addEdge(r5, add1);
        graph.addEdge(add1, add2);
        graph.addEdge(imm16, add2);
    }

}
