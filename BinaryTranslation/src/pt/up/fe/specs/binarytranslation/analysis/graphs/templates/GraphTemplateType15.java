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

public class GraphTemplateType15 extends AGraphTemplate {

    protected GraphTemplateType15(String type) {
        super(type);
        var r4 = new BtfVertex("r4", BtfVertexType.REGISTER);
        var r8 = new BtfVertex("r8", BtfVertexType.REGISTER);
        var r10 = new BtfVertex("r10", BtfVertexType.REGISTER);
        var imm1 = new BtfVertex("4", BtfVertexType.IMMEDIATE);
        var add0 = new BtfVertex("+", BtfVertexType.OPERATION);
        var add1 = new BtfVertex("+", BtfVertexType.OPERATION);
        var mul = new BtfVertex("*", BtfVertexType.OPERATION);
        
        graph.addVertex(r4);
        graph.addVertex(r8);
        graph.addVertex(r10);
        graph.addVertex(imm1);
        graph.addVertex(add0);
        graph.addVertex(add1);
        graph.addVertex(mul);
        graph.addEdge(r8, add0);
        graph.addEdge(r4, add0);
        graph.addEdge(add0, mul);
        graph.addEdge(imm1, mul);
        graph.addEdge(mul, add1);
        graph.addEdge(r10, add1);
    }

    protected GraphTemplateType15() {
        this("Type 15");
    }
}
