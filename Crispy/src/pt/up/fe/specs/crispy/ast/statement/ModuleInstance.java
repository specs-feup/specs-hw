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

package pt.up.fe.specs.crispy.ast.statement;

import java.util.Arrays;
import java.util.List;

import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.definition.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.operator.HardwareOperator;

public class ModuleInstance extends HardwareStatement {

    private final HardwareModule moduleDefinition;
    private final String instanceName;
    private final List<? extends HardwareOperator> portConnections;

    /*
     * TODO: Maybe use a map to pass the child port - parent module signal relation ?
     */

    public ModuleInstance(HardwareModule moduleDefinition,
            String instanceName, List<? extends HardwareOperator> connections)
            throws IllegalArgumentException {

        // "connections" must be single name operators,
        // i.e., @VariableOperator,
        // or @ImmediateOperator, for now
        // if we want an input that is a complex expression,
        // assign that expression to
        // a @HardwareOperator first

        super(HardwareNodeType.ModuleInstantiation);
        this.instanceName = instanceName;
        this.moduleDefinition = moduleDefinition;
        this.portConnections = connections;

        if (this.moduleDefinition.getPorts().size() != this.portConnections.size()) {
            throw new IllegalArgumentException();
        }

        /*
         * NOTE: dont use children here, since its a fixed 1 to 1 list
         * 
        for (var asNode : this.portConnections) {
            this.addChild(asNode.copy());
            // we must copy since the HardwareOperator is
            // part of another part of the module body
            // and a node cannot have 2 parents in a tree
        }*/
    }

    public ModuleInstance(HardwareModule moduleDefinition,
            String instanceName, HardwareOperator... connections)
            throws IllegalArgumentException {
        this(moduleDefinition, instanceName, Arrays.asList(connections));
    }

    @Override
    public Integer getID() {
        return this.instanceName.hashCode();
    }

    public HardwareModule getModuleDefinition() {
        return moduleDefinition;
    }

    @Override
    protected ModuleInstance copyPrivate() {
        return new ModuleInstance(this.moduleDefinition, this.instanceName, this.portConnections);
    }

    @Override
    public ModuleInstance copy() {
        return (ModuleInstance) super.copy();
    }

    /*
     * Supposed to print an instantiation of the module only, i.e.
     * 
     * "adder add1(.inA(wireA), .inB(wireB));"
     */
    @Override
    public String getAsString() {

        var builder = new StringBuilder();

        builder.append(this.moduleDefinition.getName() + " ");
        builder.append(this.instanceName + " (\n");

        var ports = this.moduleDefinition.getPorts();
        for (int i = 0; i < ports.size(); i++) {
            var connection = this.portConnections.get(i).getValue();
            builder.append("\t." + ports.get(i).getVariableName() + "(" + connection + ")");
            if (i != ports.size() - 1) {
                builder.append(",");
            }
            builder.append("\n");
        }
        builder.append(");\n");
        return builder.toString();
    }
}
