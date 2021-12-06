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

package pt.up.fe.specs.binarytranslation.hardware.factory;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.aritmetic.AdditionExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.VariableOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralNonBlockingStatement;

/**
 * Factory methods for verilog AST nodes
 * 
 * @author nuno
 *
 */
public class Verilog {

    // public static

    // public static HardwareTestbench newTestBench(HardwareModule dut, TestbenchSetup setup);

    public static AdditionExpression add(HardwareExpression refA, HardwareExpression refB) {
        return new AdditionExpression(refA, refB);
    }

    public static ProceduralNonBlockingStatement nonBlocking(VariableOperator target, HardwareExpression expression) {
        return new ProceduralNonBlockingStatement(target, expression);
    }
}
