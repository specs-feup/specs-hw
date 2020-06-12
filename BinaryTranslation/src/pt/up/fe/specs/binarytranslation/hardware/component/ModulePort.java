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

package pt.up.fe.specs.binarytranslation.hardware.component;

import java.io.OutputStream;

import pt.up.fe.specs.binarytranslation.graphs.edge.*;

public class ModulePort implements HardwareComponent {

    private final int portWidth;
    private final String portname;
    private final GraphEdge parentEdge;
    private final ModulePortDirection direction;

    private ModulePort(GraphEdge edge, ModulePortDirection direction) {
        this.parentEdge = edge;
        this.portWidth = edge.getWidth();
        this.direction = direction;
        this.portname = edge.getRepresentation().replace("<", "").replace(">", "");
        // TODO: VERY CLUMSY!!
    }

    public static ModulePort newInputPort(GraphInput edge) {
        return new ModulePort(edge, ModulePortDirection.input);
    }

    public static ModulePort newOutputPort(GraphOutput edge) {
        return new ModulePort(edge, ModulePortDirection.output);
    }

    public static ModulePort newPort(GraphEdge edge) {
        var dir = (edge.getType() == GraphEdgeType.livein)
                ? ModulePortDirection.input
                : ModulePortDirection.output;
        // TODO: THIS IS STILL UGLY!!

        return new ModulePort(edge, dir);
    }

    @Override
    public String getAsString() {
        return this.direction.toString() + " "
                + "[" + (this.portWidth - 1) + " : 0] " + this.portname + ";";
    }

    @Override
    public void emit(OutputStream os) {
        // TODO Auto-generated method stub

    }
}
