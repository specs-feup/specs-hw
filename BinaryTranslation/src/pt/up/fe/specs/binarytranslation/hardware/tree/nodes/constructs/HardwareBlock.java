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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes.constructs;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNodeType;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.HardwareStatement;

public abstract class HardwareBlock extends HardwareNode {

    protected Map<Integer, HardwareStatement> statementMap;

    public HardwareBlock(HardwareNodeType type) {
        super(type);
    }

    @Override
    public String getAsString() {
        return nestContent(super.getAsString());
    }

    /*
     * All block types are supposed to have statements inside;
     * only @ModuleBlock may have other @HardwareBlock s
     */
    public final HardwareStatement addStatement(HardwareStatement statement) {

        var id = statement.getIdentifier();
        if (this.statementMap.containsKey(id))
            throw new RuntimeException("Statement with same " +
                    " ID + (" + id + ") already added to Block!");

        this.addChild(statement);
        return statement;
    }

    /*
     * *****************************************************************************
     * get statements list
     */
    public final List<HardwareStatement> getStatements() {
        return getChildren(HardwareStatement.class);
    }

    /*
     * get statement by index
     */
    public final HardwareStatement getStatement(int idx) {
        return getStatements().get(idx);
    }

    /*
     * get statement via predicate
     */
    public final List<HardwareStatement> getStatements(Predicate<HardwareStatement> predicate) {
        return getStatements().stream().filter(predicate).collect(Collectors.toList());
    }
}
