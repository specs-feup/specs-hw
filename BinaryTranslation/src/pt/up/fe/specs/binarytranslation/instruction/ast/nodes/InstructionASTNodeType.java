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
 
package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public enum InstructionASTNodeType {

    PseudoInstructionNode,

    // statements
    PlainStatementNode,
    IfStatementNode,
    IfElseStatementNode,
    StatementListNode,

    // expressions
    AssignmentExpressionNode,
    BinaryExpressionNode,
    UnaryExpressionNode,
    ScalarSubscriptASTNode,
    RangeSubscriptASTNode,
    FunctionExpressionASTNode,

    // abstract
    OperandNode,

    // using only info from pseudocode
    BareOperandNode,
    MetaFieldNode,
    LiteralOperandNode,

    // types of operands with information
    // completed from executed instruction
    LiveInNode,
    LiveOutNode,
    VariableNode,
    ImmediateNode,

    OperatorNode,
    NumberNode,
    Generic
}
