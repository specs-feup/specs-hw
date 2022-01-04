/**
 *  Copyright 2022 SPeCS.
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

package pt.up.fe.specs.binarytranslation.hardware.generation.visitors.wip;

import pt.up.fe.specs.binarytranslation.hardware.generation.HardwareNodeExpressionMap;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.node.AInstructionCDFGNode;
import pt.up.fe.specs.binarytranslation.instruction.cdfg.instruction.subgraph.AInstructionCDFGSubgraph;
import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.block.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.Wire;

public class InstructionCDFGConditionalSubgraphConverter extends InstructionCDFGSubgraphConverter{

    private HardwareExpression rootNode;
    
    public InstructionCDFGConditionalSubgraphConverter(AInstructionCDFGSubgraph subgraph, HardwareModule module) {
        super(subgraph, module);
    }
    
    public static HardwareExpression convert(AInstructionCDFGSubgraph subgraph, HardwareModule module) {
        InstructionCDFGConditionalSubgraphConverter converter = new InstructionCDFGConditionalSubgraphConverter(subgraph, module);
        
        converter.begin();
        
        return converter.rootNode;
    }
    
    @Override
    protected void visitOperationVertex(AInstructionCDFGNode vertex, HardwareNode childHardwareNode) {
        
        Wire operationSignal = this.module.addWire(vertex.getUID(), 32);
        
        HardwareExpression operationExpression = HardwareNodeExpressionMap.generate(vertex.getClass(), this.getOperationOperandsSignals(vertex));
        
        this.rootNode = operationExpression;
        
        super.visitOperationVertex(vertex, operationExpression);
    }
    
    public HardwareExpression getRootNode() {
        return this.rootNode;
    }
}