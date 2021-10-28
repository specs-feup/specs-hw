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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;

public class StatementList extends HardwareStatement{

    public StatementList() {
        super();
    }
    
    private StatementList(List<HardwareStatement> statements) {
        super();
        statements.forEach(s -> this.addStatement(s));
    }
    
    public void addStatement(HardwareStatement statement) {
        this.addChild(statement);
    }
    
    public List<HardwareStatement> getStatements() {
        var list = this.getChildren();
        var nlist = new ArrayList<HardwareStatement>();
        for (var c : list)
            nlist.add((HardwareStatement) c);
        return nlist;
    }

    @Override
    protected HardwareNode copyPrivate() {
        return new StatementList(this.getStatements());
    }  
    
}