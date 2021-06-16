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

public class GraphTemplateType4 extends AGraphTemplate {

    protected GraphTemplateType4(String type) {
        super(type);
        var r9 = new BtfVertex("r9", BtfVertexType.REGISTER);
        var imm16 = new BtfVertex("16", BtfVertexType.IMMEDIATE);
        var r5 = new BtfVertex("r5", BtfVertexType.REGISTER);
        var mult = new BtfVertex("*", BtfVertexType.OPERATION);
        var add1 = new BtfVertex("+", BtfVertexType.OPERATION);
        var imm4 = new BtfVertex("4", BtfVertexType.IMMEDIATE);
        var add2 = new BtfVertex("+", BtfVertexType.OPERATION);
        
        graph.addVertex(r9);
        graph.addVertex(imm16);
        graph.addVertex(r5);
        graph.addVertex(mult);
        graph.addVertex(add1);
        graph.addVertex(imm4);
        graph.addVertex(add2);
        
        graph.addEdge(r9, add1);
        graph.addEdge(imm16, add1);
        graph.addEdge(add1, mult);
        graph.addEdge(imm4, mult);
        graph.addEdge(mult, add2);
        graph.addEdge(r5, add2);
    }
    
    protected GraphTemplateType4() {
        this("Type 4");
    }
}
