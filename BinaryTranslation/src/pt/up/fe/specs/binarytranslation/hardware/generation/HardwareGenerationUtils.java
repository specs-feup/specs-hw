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

package pt.up.fe.specs.binarytranslation.hardware.generation;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.InstructionASTNode;
import pt.up.fe.specs.binarytranslation.instruction.ast.nodes.base.OperandASTNode;

public class HardwareGenerationUtils {

    /*
     * Public call method to get all OperandASTNode instances from a certain node downwards
     */
    public static List<OperandASTNode> getOperands(InstructionASTNode node) {
        var list = new ArrayList<OperandASTNode>();
        return HardwareGenerationUtils.getOperands(node, list);
    }

    /*
     * Private call method to help recurse on the tree (doesn't include creation of the list object to return)
     */
    private static List<OperandASTNode> getOperands(InstructionASTNode node, List<OperandASTNode> list) {
        for (var c : node.getChildren()) {
            if (c instanceof OperandASTNode)
                list.add((OperandASTNode) c);
            else
                getOperands(c, list);
        }
        return list;
    }
}
