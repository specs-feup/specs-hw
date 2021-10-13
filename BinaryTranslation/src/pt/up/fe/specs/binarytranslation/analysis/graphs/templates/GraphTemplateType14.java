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

public class GraphTemplateType14 extends AGraphTemplate {

    protected GraphTemplateType14(String type) {
        super(type);
        
        var r8 = new BtfVertex("r8", BtfVertexType.REGISTER);
        var imm1 = new BtfVertex("100000", BtfVertexType.IMMEDIATE);
        var imm2 = new BtfVertex("200000", BtfVertexType.IMMEDIATE);
        var add0 = new BtfVertex("+", BtfVertexType.OPERATION);
        var add1 = new BtfVertex("+", BtfVertexType.OPERATION);
        
        graph.addVertex(r8);
        graph.addVertex(imm1);
        graph.addVertex(imm2);
        graph.addVertex(add0);
        graph.addVertex(add1);
        
        graph.addEdge(r8, add0);
        graph.addEdge(imm1, add0);
        graph.addEdge(add0, add1);
        graph.addEdge(imm2, add1);
    }

    
    protected GraphTemplateType14() {
        this("Type 14");
    }
}
