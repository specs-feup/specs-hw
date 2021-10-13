/**
 * Copyright 2021 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
 
package pt.up.fe.specs.binarytranslation.graph.edge;

import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class GraphInput extends AGraphEdge {

    public GraphInput(Operand op) {
        super(op, GraphEdgeDirection.input, op.getRepresentation());

        if (op.isRegister()) {
            this.etype = GraphEdgeType.livein;
        }

        // if immediate
        else if (op.isImmediate()) {
            this.etype = GraphEdgeType.immediate;
        }
    }

    /*
     * An input is modified during the resolving of the graph, when a BinarySegmentGraph is instantiated
     */
    public void setInputAs(GraphEdgeType type, String value) {
        this.setEdgeAs(type, value);
    }

    /*
     * 
     */
    public String rawDotty() {
        String ret = "";
        if (this.isLivein())
            ret += "in_";
        ret += this.getRepresentation();
        return ret;
    }
}
