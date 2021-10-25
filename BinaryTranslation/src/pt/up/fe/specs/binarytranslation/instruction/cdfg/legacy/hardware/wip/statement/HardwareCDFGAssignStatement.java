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

package pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.hardware.wip.statement;

import pt.up.fe.specs.binarytranslation.instruction.cdfg.legacy.generic.node.AGenericCDFGNode;

public class HardwareCDFGAssignStatement extends AHardwareCDFGStatement{

    private final static String statement = "assign ";
    
    public HardwareCDFGAssignStatement(AGenericCDFGNode operation_node) {
        super(statement);
        this.addVertex(new HardwareCDFGAssignmentStatement(operation_node));
    }
    
    
    
    @Override
    public String toString() {
        return this.getStatement() + this.vertexSet().toArray()[0].toString() + ";";
    }
}
