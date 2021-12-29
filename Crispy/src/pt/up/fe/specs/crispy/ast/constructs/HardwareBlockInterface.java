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

package pt.up.fe.specs.crispy.ast.constructs;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.declaration.IdentifierDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.WireDeclaration;
import pt.up.fe.specs.crispy.ast.definition.HardwareModule;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.Immediate;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;
import pt.up.fe.specs.crispy.ast.statement.DelayStatement;
import pt.up.fe.specs.crispy.ast.statement.HardwareStatement;
import pt.up.fe.specs.crispy.ast.statement.IfElseStatement;
import pt.up.fe.specs.crispy.ast.statement.IfStatement;
import pt.up.fe.specs.crispy.ast.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.crispy.ast.statement.ProceduralNonBlockingStatement;

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
    public default HardwareBlock _do(HardwareStatement statement) {
        sanityCheck(statement);
        getBody().addChild(statement);
        return getBody();
    }

    // implemented if the block is a IfElse only, otherwise exception is thrown
    // this method is meant to be used as sugar when im in the BeginEndBlock
    // which corresponds to the If clause, and want to switch to the else
    // after im done adding statements with _do, but dont want to
    // get the reference to the parent explicitly
    public default BeginEndBlock orElse() {

        var parent = getBody().getParent();
        if (parent.getType() != HardwareNodeType.IfElseStatement)
            throw new RuntimeException("HardwareBlockInterface: cannot fetch \"then\" "
                    + "clause of a block which is not an IfElse block!");

        else {
            return ((IfElseStatement) parent).orElse();
        }
    }

    /*
    public default HardwareStatement _doBefore(HardwareStatement statement, HardwareNode other) {
        sanityCheck(statement);
        getBody().addChildLeftOf(other, statement);
        return statement;
    }
    
    public default HardwareStatement _doAfter(HardwareStatement statement, HardwareNode other) {
        sanityCheck(statement);
        getBody().addChildRightOf(other, statement);
        return statement;
    }*/

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

    // based on which block type (i.e., alwaysff, a new result variable may have
    // to be a register!)
    public default IdentifierDeclaration resolveNewDeclaration(String newName) {
        return new WireDeclaration(newName, 32);
    }

    public default VariableOperator resolveIdentifier(String targetName) {
        var moduleParent = getAncestor();
        return (VariableOperator) moduleParent.getDeclaration(targetName);
    }

    public default VariableOperator resolveSink(String targetName) {

        var sink = resolveIdentifier(targetName);
        if (sink != null)
            return sink;
        else {
            return getAncestor().addDeclaration(resolveNewDeclaration(targetName));
        }
    }

    public default VariableOperator createAssigment(String targetName, String sourceName,
            BiFunction<VariableOperator, HardwareExpression, HardwareStatement> supplier) {
        var source = resolveIdentifier(sourceName);
        if (source == null) {
            throw new RuntimeException("HardwareBlockInterface: undeclared source name!");
        }
        return createAssigment(targetName, source, supplier);
    }

    public default VariableOperator createAssigment(VariableOperator target, HardwareExpression expr,
            BiFunction<VariableOperator, HardwareExpression, HardwareStatement> supplier) {
        return createAssigment(target.getResultName(), expr, supplier);
    }

    public default VariableOperator createAssigment(String targetName, HardwareExpression expr,
            BiFunction<VariableOperator, HardwareExpression, HardwareStatement> supplier) {
        var sink = resolveSink(targetName);
        getBody().addChild(supplier.apply(sink, expr));
        return sink;
    }

    /*
     * nonBlocking
     */
    public default VariableOperator nonBlocking(HardwareExpression expr) {
        return createAssigment(expr.getResultName(), expr, (t, u) -> new ProceduralNonBlockingStatement(t, u));
    }

    public default VariableOperator nonBlocking(String targetName, String sourceName) {
        return createAssigment(targetName, sourceName, (t, u) -> new ProceduralNonBlockingStatement(t, u));
    }

    public default VariableOperator nonBlocking(String targetName, HardwareExpression expr) {
        return createAssigment(targetName, expr, (t, u) -> new ProceduralNonBlockingStatement(t, u));
    }

    public default VariableOperator nonBlocking(VariableOperator target, int literalConstant) {
        var imm = new Immediate(literalConstant, target.getResultWidth());
        return createAssigment(target, imm, (t, u) -> new ProceduralNonBlockingStatement(t, u));
    }

    public default VariableOperator nonBlocking(VariableOperator target, HardwareExpression expr) {
        return createAssigment(target, expr, (t, u) -> new ProceduralNonBlockingStatement(t, u));
    }

    // TODO: add checks if assignemnts are attempted on input ports!!

    /*
     * blocking
     */
    public default VariableOperator blocking(HardwareExpression expr) {
        return createAssigment(expr.getResultName(), expr, (t, u) -> new ProceduralBlockingStatement(t, u));
    }

    public default VariableOperator blocking(String targetName, String sourceName) {
        return createAssigment(targetName, sourceName, (t, u) -> new ProceduralBlockingStatement(t, u));
    }

    public default VariableOperator blocking(String targetName, HardwareExpression expr) {
        return createAssigment(targetName, expr, (t, u) -> new ProceduralBlockingStatement(t, u));
    }

    public default VariableOperator blocking(VariableOperator target, int literalConstant) {
        var imm = new Immediate(literalConstant, target.getResultWidth());
        return createAssigment(target, imm, (t, u) -> new ProceduralBlockingStatement(t, u));
    }

    public default VariableOperator blocking(VariableOperator target, HardwareExpression expr) {
        return createAssigment(target, expr, (t, u) -> new ProceduralBlockingStatement(t, u));
    }

    /*
     * delay
     */
    public default DelayStatement delay(double delay) {
        return (DelayStatement) getBody().addChild(new DelayStatement(delay));
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
