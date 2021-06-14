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

package pt.up.fe.specs.binarytranslation.analysis.graphs.templates;

import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex;
import pt.up.fe.specs.binarytranslation.analysis.graphs.BtfVertex.BtfVertexType;

public class GraphTemplateType6 extends AGraphTemplate {

    protected GraphTemplateType6(GraphTemplateType type) {
        super(type);
        var r7 = new BtfVertex("r7", BtfVertexType.REGISTER);
        var row = new BtfVertex("Row", BtfVertexType.IMMEDIATE);
        var r5 = new BtfVertex("r5", BtfVertexType.REGISTER);
        var r8 = new BtfVertex("r8", BtfVertexType.REGISTER);
        var mult1 = new BtfVertex("*", BtfVertexType.OPERATION);
        var mult2 = new BtfVertex("*", BtfVertexType.OPERATION);
        var add1 = new BtfVertex("+", BtfVertexType.OPERATION);
        var imm4 = new BtfVertex("64", BtfVertexType.IMMEDIATE);
        var add2 = new BtfVertex("+", BtfVertexType.OPERATION);
        
        graph.addVertex(r7);
        graph.addVertex(row);
        graph.addVertex(r5);
        graph.addVertex(r8);
        graph.addVertex(mult1);
        graph.addVertex(mult2);
        graph.addVertex(add1);
        graph.addVertex(imm4);
        graph.addVertex(add2);
        
        graph.addEdge(r7, mult1);
        graph.addEdge(row, mult1);
        graph.addEdge(mult1, add1);
        graph.addEdge(r8, add1);
        graph.addEdge(add1, mult2);
        graph.addEdge(imm4, mult2);
        graph.addEdge(mult2, add2);
        graph.addEdge(r5, add2);
    }

}
