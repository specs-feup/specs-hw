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

public class GraphOutput extends AGraphEdge {

    public GraphOutput(Operand op) {
        super(op, GraphEdgeDirection.output, op.getRepresentation());

        // TODO how to determine is operand is a data or control output? would need instruction, or to add a field to

        // TODO actually, the branch oeprations of the microblaze have NO output register

        // OperandTypes
        // if(op)

        this.etype = GraphEdgeType.noderesult;
        // default to "data", change to liveout if the owner of
        // this output is the last one to write to this register (?)

        this.op = op;
    }

    public void setOutputAs(GraphEdgeType type, String value) {
        this.setEdgeAs(type, value);
    }

    /*
     * 
     */
    public String rawDotty() {
        return "out_" + this.getRepresentation();
    }
}
