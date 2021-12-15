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
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.VariableOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ContinuousStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.HardwareStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralNonBlockingStatement;

public interface HardwareBlockInterface {

    public abstract HardwareBlock getBody();

    // public abstract void updateTree(HardwareBlock myself); // handles structure updates when sugar methods are used
    // (?)

    public default void sanityCheck(HardwareNode newNode) {
        var addedNodes = getBody().getChildrenOf(newNode.getClass());
        for (var node : addedNodes) {
            if (node.getID().equals(newNode.getID()))
                throw new RuntimeException(newNode.getClass().getSimpleName() +
                        " with same ID (" + newNode.getID()
                        + ") already added to " + getBody().getClass().getSimpleName());
        }
    }

    /*
     * *****************************************************************************
     * shorthandles for statements
     
    public default ProceduralNonBlockingStatement nonBlocking(VariableOperator target, HardwareExpression expression) {
        var statement = new ProceduralNonBlockingStatement(target, expression);
        return (ProceduralNonBlockingStatement) addStatement(statement);
    }*/

    // TODO: use static classes to have syntax like "nonBlocking.add(...)"

    // TODO: methods like nonBlocking shouldbe part of VariableOperator
    // that way I can have a reference to a var and do:
    // ref1.nonBlocking(...), which returns a new ProceduralNonBlockingStatement

    /*
     * All block types are supposed to have statements inside;
     * only @ModuleBlock may have other @HardwareBlock s
     */
    public default HardwareStatement addStatement(HardwareStatement statement) {
        sanityCheck(statement);
        getBody().addChild(statement);
        return statement;
    }

    /*
     * *****************************************************************************
     * get statements list
     */
    public default List<HardwareStatement> getStatements() {
        return getBody().getChildren(HardwareStatement.class);
    }

    /*
     * get statement by index
     */
    public default HardwareStatement getStatement(int idx) {
        return getStatements().get(idx);
    }

    /*
     * get statement via predicate
     */
    public default List<HardwareStatement> getStatements(Predicate<HardwareStatement> predicate) {
        return getStatements().stream().filter(predicate).collect(Collectors.toList());
    }

    /*
     * *****************************************************************************
     * sugar methods
     */
    /*public default ContinuousStatement assign(String targetName, HardwareExpression expr) {
        
    }*/

    public default ContinuousStatement assign(VariableOperator target, HardwareExpression expr) {
        var stat = new ContinuousStatement(target, expr);
        getBody().addChild(stat); // TODO: will this handle adding the variable operator to the declaration block? no
        return stat;
    }

    public default ProceduralNonBlockingStatement nonBlocking(VariableOperator target, HardwareExpression expr) {
        var stat = new ProceduralNonBlockingStatement(target, expr);
        getBody().addChild(stat);
        return stat;
    }

    public default ProceduralBlockingStatement blocking(VariableOperator target, HardwareExpression expr) {
        var stat = new ProceduralBlockingStatement(target, expr);
        getBody().addChild(stat);
        return stat;
    }
}
