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
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.HardwareNode;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.IdentifierDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.declaration.WireDeclaration;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.definition.HardwareModule;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.HardwareExpression;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.HardwareOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.ImmediateOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.expression.operator.VariableOperator;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.HardwareStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.IfElseStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.IfStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.binarytranslation.hardware.tree.nodes.statement.ProceduralNonBlockingStatement;

public interface HardwareBlockInterface { // extends HardwareOperatorMethods?

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
    public default HardwareModule getAncestor() {

        var moduleParent = getBody().getAncestorTry(HardwareModule.class).orElse(null);
        if (moduleParent == null) {
            throw new RuntimeException("HardwareBlockInterface: cannot perform assign via"
                    + " targetName on a HardwareBlock without a HardwareModule ancestor!");
        }
        return moduleParent;
    }

    public default HardwareOperator resolveIdentifier(String targetName) {
        var moduleParent = getAncestor();
        return moduleParent.getDeclaration(targetName);
    }

    // based on which block type (i.e., alwaysff, a new result variable may have
    // to be a register!)
    public default IdentifierDeclaration resolveNewDeclaration(String newName) {
        return new WireDeclaration(newName, 32);
    }

    public default VariableOperator createAssigment(String targetName, String sourceName,
            BiFunction<VariableOperator, HardwareExpression, VariableOperator> supplier) {

    }

    public default VariableOperator createAssigment(String targetName, String sourceName,
            BiFunction<VariableOperator, HardwareExpression, VariableOperator> supplier) {

        var sink = resolveIdentifier(targetName);
        var source = resolveIdentifier(sourceName);
        if (sink != null)
            return supplier.apply((VariableOperator) sink, source);
        else {
            var newTarget = getAncestor().addDeclaration(resolveNewDeclaration(targetName));
            return supplier.apply(newTarget, source);
        }
    }

    public default VariableOperator createAssigment(VariableOperator target, HardwareExpression expr,
            BiFunction<VariableOperator, HardwareExpression, VariableOperator> supplier) {
        return createAssigment(target.getResultName(), expr, supplier);
    }

    public default VariableOperator createAssigment(String targetName, HardwareExpression expr,
            BiFunction<VariableOperator, HardwareExpression, VariableOperator> supplier) {

        var sink = resolveIdentifier(targetName);
        if (sink != null)
            return supplier.apply((VariableOperator) sink, expr);
        else {
            var newTarget = getAncestor().addDeclaration(resolveNewDeclaration(targetName));
            return supplier.apply(newTarget, expr);
        }
    }

    /*
     * nonBlocking
     */
    public default VariableOperator nonBlocking(HardwareExpression expr) {
        return createAssigment(expr.getResultName(), expr, (t, u) -> nonBlocking(t, u));
    }

    public default VariableOperator nonBlocking(String targetName, String sourceName) {
        return createAssigment(targetName, sourceName, (t, u) -> nonBlocking(t, u));
    }

    public default VariableOperator nonBlocking(String targetName, HardwareExpression expr) {
        return createAssigment(targetName, expr, (t, u) -> nonBlocking(t, u));
    }

    public default VariableOperator nonBlocking(VariableOperator target, int literalConstant) {
        // return nonBlocking(target, new ImmediateOperator(literalConstant, target.getResultWidth()));
        return createAssigment(target, new ImmediateOperator(literalConstant, target.getResultWidth()));
    }

    public default VariableOperator nonBlocking(VariableOperator target, HardwareExpression expr) {
        getBody().addChild(new ProceduralNonBlockingStatement(target, expr));
        // TODO: will this handle adding the variable operator to the declaration block if it doesnt exist? NO
        return target;
    }

    // TODO: add checks if assignemnts are attempted on input ports!!

    /*
     * blocking
     */
    public default VariableOperator blocking(HardwareExpression expr) {
        return createAssigment(expr.getResultName(), expr, (t, u) -> blocking(t, u));
    }

    public default VariableOperator blocking(String targetName, String sourceName) {
        return createAssigment(targetName, sourceName, (t, u) -> blocking(t, u));
    }

    public default VariableOperator blocking(String targetName, HardwareExpression expr) {
        return createAssigment(targetName, expr, (t, u) -> blocking(t, u));
    }

    public default VariableOperator blocking(VariableOperator target, int literalConstant) {
        return blocking(target, new ImmediateOperator(literalConstant, target.getResultWidth()));
    }

    public default VariableOperator blocking(VariableOperator target, HardwareExpression expr) {
        getBody().addChild(new ProceduralBlockingStatement(target, expr));
        // TODO: will this handle adding the variable operator to the declaration block if it doesnt exist? NO
        return target;
    }

    /*
     * if-else
     */
    public default IfStatement _if(HardwareExpression condition) {
        return (IfStatement) getBody().addChild(new IfStatement(condition));
    }

    public default IfStatement _if(HardwareExpression condition, HardwareStatement... stats) {
        return (IfStatement) getBody().addChild(new IfStatement(condition, stats));
    }

    public default IfStatement _if(HardwareExpression condition, List<HardwareStatement> stats) {
        return (IfStatement) getBody().addChild(new IfStatement(condition, stats));
    }

    public default IfElseStatement _ifelse(HardwareExpression condition) {
        return (IfElseStatement) getBody().addChild(new IfElseStatement(condition));
    }

    public default IfElseStatement _ifelse(HardwareExpression condition, HardwareStatement... stats) {
        return (IfElseStatement) getBody().addChild(new IfElseStatement(condition, stats));
    }

    public default IfElseStatement _ifelse(HardwareExpression condition, List<HardwareStatement> stats) {
        return (IfElseStatement) getBody().addChild(new IfElseStatement(condition, stats));
    }
}
