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

public class GraphTemplateType5 extends AGraphTemplate {

    protected GraphTemplateType5(GraphTemplateType type) {
        super(type);
        var r7 = new AddressVertex("r7", AddressVertexType.REGISTER);
        var imm0 = new AddressVertex("0", AddressVertexType.IMMEDIATE);
        var add = new AddressVertex("+", AddressVertexType.OPERATION);
        graph.addVertex(r7);
        graph.addVertex(imm0);
        graph.addVertex(add);
        
        graph.addEdge(r7, add);
        graph.addEdge(imm0, add);
    }

}
