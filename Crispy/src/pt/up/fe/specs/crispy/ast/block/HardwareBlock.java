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

package pt.up.fe.specs.crispy.ast.block;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pt.up.fe.specs.crispy.ast.HardwareNode;
import pt.up.fe.specs.crispy.ast.HardwareNodeType;
import pt.up.fe.specs.crispy.ast.declaration.IdentifierDeclaration;
import pt.up.fe.specs.crispy.ast.declaration.WireDeclaration;
import pt.up.fe.specs.crispy.ast.expression.HardwareExpression;
import pt.up.fe.specs.crispy.ast.expression.operator.Immediate;
import pt.up.fe.specs.crispy.ast.expression.operator.VariableOperator;
import pt.up.fe.specs.crispy.ast.statement.DelayStatement;
import pt.up.fe.specs.crispy.ast.statement.HardwareStatement;
import pt.up.fe.specs.crispy.ast.statement.IfElseStatement;
import pt.up.fe.specs.crispy.ast.statement.IfStatement;
import pt.up.fe.specs.crispy.ast.statement.ProceduralBlockingStatement;
import pt.up.fe.specs.crispy.ast.statement.ProceduralNonBlockingStatement;

public abstract class HardwareBlock extends HardwareNode {// implements HardwareBlockInterface {

    protected HardwareBlock(HardwareNodeType type) {
        super(type);
    }

    /*
     * 
     */
    protected abstract HardwareBlock getBody();

    /*
     * Auxiliary methods for statement creation
     */
    protected HardwareModule getAncestor() {

        var moduleParent = getBody().getAncestorTry(HardwareModule.class).orElse(null);
        if (moduleParent == null) {
            throw new RuntimeException("HardwareBlockInterface: cannot perform assign via"
                    + " targetName on a HardwareBlock without a HardwareModule ancestor!");
        }
        return moduleParent;
    }

    // based on which block type (i.e., alwaysff, a new result variable may have
    // to be a register!)
    protected IdentifierDeclaration resolveNewDeclaration(String newName) {
        return new WireDeclaration(newName, 32);
    }

    protected VariableOperator resolveIdentifier(String targetName) {
        var moduleParent = getAncestor();
        return (VariableOperator) moduleParent.getDeclaration(targetName);
    }

    protected VariableOperator resolveSink(String targetName) {

        var sink = resolveIdentifier(targetName);
        if (sink != null)
            return sink;
        else {
            return getAncestor().addDeclaration(resolveNewDeclaration(targetName));
        }
    }

    protected VariableOperator createAssigment(String targetName, String sourceName,
            BiFunction<VariableOperator, HardwareExpression, HardwareStatement> supplier) {
        var source = resolveIdentifier(sourceName);
        if (source == null) {
            throw new RuntimeException("HardwareBlockInterface: undeclared source name!");
        }
        return createAssigment(targetName, source, supplier);
    }

    protected VariableOperator createAssigment(VariableOperator target, HardwareExpression expr,
            BiFunction<VariableOperator, HardwareExpression, HardwareStatement> supplier) {
        return createAssigment(target.getName(), expr, supplier);
    }

    protected VariableOperator createAssigment(String targetName, HardwareExpression expr,
            BiFunction<VariableOperator, HardwareExpression, HardwareStatement> supplier) {
        var sink = resolveSink(targetName);
        getBody().addChild(supplier.apply(sink, expr));
        return sink;
    }

    protected void sanityCheck(HardwareNode newNode) {
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
     
    /*
     * All block types are supposed to have statements inside;
     * only @ModuleBlock may have other @HardwareBlock s
     */
    public HardwareBlock _do(HardwareStatement statement) {
        sanityCheck(statement);
        getBody().addChild(statement);
        return getBody();
    }

    /*
     * if-else
     */
    public IfStatement _if(HardwareExpression condition) {
        return (IfStatement) getBody().addChild(new IfStatement(condition));
    }

    public IfElseStatement _ifelse(HardwareExpression condition) {
        return (IfElseStatement) getBody().addChild(new IfElseStatement(condition));
    }

    // implemented if the block is a IfElse only, otherwise exception is thrown
    // this method is meant to be used as sugar when im in the BeginEndBlock
    // which corresponds to the If clause, and want to switch to the else
    // after im done adding statements with _do, but dont want to
    // get the reference to the parent explicitly
    public BeginEndBlock orElse() {

        var parent = getBody().getParent();
        if (parent.getType() != HardwareNodeType.IfElseStatement)
            throw new RuntimeException("HardwareBlockInterface: cannot fetch \"then\" "
                    + "clause of a block which is not an IfElse block!");

        else {
            return ((IfElseStatement) parent).orElse();
        }
    }

    /*
     * *****************************************************************************
     * get statements list
     */
    public List<HardwareStatement> getStatements() {
        return getBody().getChildren(HardwareStatement.class);
    }

    /*
     * get statement by index
     */
    public HardwareStatement getStatement(int idx) {
        return getStatements().get(idx);
    }

    /*
     * get statement via predicate
     */
    public List<HardwareStatement> getStatements(Predicate<HardwareStatement> predicate) {
        return getStatements().stream().filter(predicate).collect(Collectors.toList());
    }

    /*
     * nonBlocking
     */
    public VariableOperator nonBlocking(HardwareExpression expr) {
        return createAssigment(expr.getName(), expr, (t, u) -> new ProceduralNonBlockingStatement(t, u));
    }

    public VariableOperator nonBlocking(String targetName, String sourceName) {
        return createAssigment(targetName, sourceName, (t, u) -> new ProceduralNonBlockingStatement(t, u));
    }

    public VariableOperator nonBlocking(String targetName, HardwareExpression expr) {
        return createAssigment(targetName, expr, (t, u) -> new ProceduralNonBlockingStatement(t, u));
    }

    public VariableOperator nonBlocking(VariableOperator target, int literalConstant) {
        var imm = new Immediate(literalConstant, target.getWidth());
        return createAssigment(target, imm, (t, u) -> new ProceduralNonBlockingStatement(t, u));
    }

    public VariableOperator nonBlocking(VariableOperator target, HardwareExpression expr) {
        return createAssigment(target, expr, (t, u) -> new ProceduralNonBlockingStatement(t, u));
    }

    // TODO: add checks if assignemnts are attempted on input ports!!

    /*
     * blocking
     */
    public VariableOperator blocking(HardwareExpression expr) {
        return createAssigment(expr.getName(), expr, (t, u) -> new ProceduralBlockingStatement(t, u));
    }

    public VariableOperator blocking(String targetName, String sourceName) {
        return createAssigment(targetName, sourceName, (t, u) -> new ProceduralBlockingStatement(t, u));
    }

    public VariableOperator blocking(String targetName, HardwareExpression expr) {
        return createAssigment(targetName, expr, (t, u) -> new ProceduralBlockingStatement(t, u));
    }

    public VariableOperator blocking(VariableOperator target, int literalConstant) {
        var imm = new Immediate(literalConstant, target.getWidth());
        return createAssigment(target, imm, (t, u) -> new ProceduralBlockingStatement(t, u));
    }

    public VariableOperator blocking(VariableOperator target, HardwareExpression expr) {
        return createAssigment(target, expr, (t, u) -> new ProceduralBlockingStatement(t, u));
    }

    /*
     * delay
     */
    public DelayStatement delay(double delay) {
        return (DelayStatement) getBody().addChild(new DelayStatement(delay));
    }
}
