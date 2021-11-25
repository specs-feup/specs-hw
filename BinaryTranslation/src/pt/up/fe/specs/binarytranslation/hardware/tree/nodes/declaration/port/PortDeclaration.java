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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.port;

import pt.up.fe.specs.binarytranslation.graph.edge.GraphEdge;
import pt.up.fe.specs.binarytranslation.graph.edge.GraphEdgeType;
import pt.up.fe.specs.binarytranslation.graph.edge.GraphInput;
import pt.up.fe.specs.binarytranslation.graph.edge.GraphOutput;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.ModulePortDirection;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.VariableDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.reference.VariableReference;
import pt.up.fe.specs.binarytranslation.instruction.operand.Operand;

public class PortDeclaration extends VariableDeclaration {

    //private final int portWidth;
   // private final String portname;
    private final ModulePortDirection direction;

    /*
     * Plain constructor
     */
  /*  public PortDeclaration(String portName, int portWidth, ModulePortDirection direction) {
        super();
        this.portname = portName;
        this.portWidth = portWidth;
        this.direction = direction;
        this.type = HardwareNodeType.PortDeclaration;
    }*/
    
    public PortDeclaration(VariableDeclaration port, ModulePortDirection direction) {
      this.addChild(port);
      this.direction = direction;
    }
    
    /*private PortDeclaration(GraphEdge edge, ModulePortDirection direction) {
        super();
        this.portWidth = edge.getWidth();
        this.direction = direction;
        this.portname = edge.getRepresentation().replace("<", "").replace(">", "").replace("[", "").replace("]", "");
        // TODO: VERY CLUMSY!!
        this.type = HardwareNodeType.PortDeclaration;
    }

    private PortDeclaration(Operand op, ModulePortDirection direction) {
        super();
        this.portWidth = op.getProperties().getWidth();
        this.direction = direction;
        this.portname = op.getRepresentation().replace("<", "").replace(">", "").replace("[", "").replace("]", "");
        // TODO: VERY CLUMSY!!
        this.type = HardwareNodeType.PortDeclaration;
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
    /*
    public static PortDeclaration newPort(Operand op) {
        var dir = (op.isRead()) ? ModulePortDirection.input : ModulePortDirection.output;
        // TODO: THIS IS STILL UGLY!!
        return new PortDeclaration(op, dir);
    }
*/
    @Override
    public String getVariableName() {
       //return this.portname;
       return ((VariableDeclaration)this.getChild(0)).getVariableName();
    }

    @Override
    public String getAsString() {
        /*if (this.portWidth > 1)
            return this.direction.toString() + " "
                    + "[" + (this.portWidth - 1) + " : 0] " + this.portname + ";";
        else
            return this.direction.toString() + " " + this.portname + ";";
            */
        return this.direction.toString() + " " + ((VariableDeclaration)this.getChild(0)).getAsString();
    }

    @Override
    protected HardwareNode copyPrivate() {
        //return new PortDeclaration(this.portname, this.portWidth, this.direction);
        return new PortDeclaration((VariableDeclaration) this.getChild(0), this.direction);
    }
}
