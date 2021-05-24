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

public class GraphTemplateType7 extends AGraphTemplate {

    protected GraphTemplateType7(GraphTemplateType type) {
        super(type);
        var r7 = new AddressVertex("r7", AddressVertexType.REGISTER);
        var row = new AddressVertex("Row", AddressVertexType.IMMEDIATE);
        var r5 = new AddressVertex("r5", AddressVertexType.REGISTER);
        var r8 = new AddressVertex("r8", AddressVertexType.REGISTER);
        var r9 = new AddressVertex("r9", AddressVertexType.REGISTER);
        var mult1 = new AddressVertex("*", AddressVertexType.OPERATION);
        var mult2 = new AddressVertex("*", AddressVertexType.OPERATION);
        var add1 = new AddressVertex("+", AddressVertexType.OPERATION);
        var imm4 = new AddressVertex("4", AddressVertexType.IMMEDIATE);
        var add2 = new AddressVertex("+", AddressVertexType.OPERATION);
        var add3 = new AddressVertex("+", AddressVertexType.OPERATION);
        
        graph.addVertex(r7);
        graph.addVertex(row);
        graph.addVertex(r5);
        graph.addVertex(r8);
        graph.addVertex(mult1);
        graph.addVertex(mult2);
        graph.addVertex(add1);
        graph.addVertex(imm4);
        graph.addVertex(add2);
        graph.addVertex(add3);
        graph.addVertex(r9);
        
        graph.addEdge(r7, mult1);
        graph.addEdge(row, mult1);
        graph.addEdge(mult1, add1);
        graph.addEdge(r8, add1);
        graph.addEdge(add1, add2);
        graph.addEdge(add2, mult2);
        graph.addEdge(mult2, add3);
        graph.addEdge(r9, add2);
        graph.addEdge(imm4, mult2);
        graph.addEdge(r5, add3);
    }

}
