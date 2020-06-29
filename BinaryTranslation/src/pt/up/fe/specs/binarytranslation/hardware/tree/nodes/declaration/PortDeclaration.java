/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration;

import java.io.OutputStream;

import pt.up.fe.specs.binarytranslation.graphs.edge.GraphEdge;
import pt.up.fe.specs.binarytranslation.graphs.edge.GraphEdgeType;
import pt.up.fe.specs.binarytranslation.graphs.edge.GraphInput;
import pt.up.fe.specs.binarytranslation.graphs.edge.GraphOutput;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.AHardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.ModulePortDirection;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class PortDeclaration extends AHardwareNode implements HardwareDeclaration {

    private final int portWidth;
    private final String portname;
    private final ModulePortDirection direction;

    private PortDeclaration(GraphEdge edge, ModulePortDirection direction) {
        this.portWidth = edge.getWidth();
        this.direction = direction;
        this.portname = edge.getRepresentation().replace("<", "").replace(">", "");
        // TODO: VERY CLUMSY!!
    }

    private PortDeclaration(Operand op, ModulePortDirection direction) {
        this.portWidth = op.getProperties().getWidth();
        this.direction = direction;
        this.portname = op.getRepresentation().replace("<", "").replace(">", "");
        // TODO: VERY CLUMSY!!
    }

    public static PortDeclaration newInputPort(GraphInput edge) {
        return new PortDeclaration(edge, ModulePortDirection.input);
    }

    public static PortDeclaration newOutputPort(GraphOutput edge) {
        return new PortDeclaration(edge, ModulePortDirection.output);
    }

    public static PortDeclaration newPort(GraphEdge edge) {
        var dir = (edge.getType() == GraphEdgeType.livein)
                ? ModulePortDirection.input
                : ModulePortDirection.output;
        // TODO: THIS IS STILL UGLY!!

        return new PortDeclaration(edge, dir);
    }

    /*
     * Used to create a port directly from an operand
     */
    public static PortDeclaration newPort(Operand op) {
        var dir = (op.isRead()) ? ModulePortDirection.input : ModulePortDirection.output;
        // TODO: THIS IS STILL UGLY!!
        return new PortDeclaration(op, dir);
    }

    @Override
    public String getAsString() {
        return this.direction.toString() + " "
                + "[" + (this.portWidth - 1) + " : 0] " + this.portname + ";";
    }
}
