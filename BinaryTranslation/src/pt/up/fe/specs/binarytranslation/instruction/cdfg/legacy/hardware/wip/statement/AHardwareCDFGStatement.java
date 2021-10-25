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

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

public abstract class AHardwareCDFGStatement extends SimpleDirectedGraph<AHardwareCDFGSubStatement, DefaultEdge>{
    
    private String statement;
    
    protected AHardwareCDFGStatement(String statement) {
        super(DefaultEdge.class);
        
        this.statement = statement;
        
    }

    protected String getStatement() {
        return this.statement;
    }
    
    // adicionar novos statements
    // estes statements tem que roubar nodes aos outros
    // tem de se ter atencao se nao se estraga o control e o data flow
    
 
    
}
