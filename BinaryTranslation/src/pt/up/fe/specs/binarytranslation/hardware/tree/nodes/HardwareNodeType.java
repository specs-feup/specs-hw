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

package pt.up.fe.specs.binarytranslation.hardware.tree.nodes;

public enum HardwareNodeType {

    // constructs
    AlwaysComb,
    AlwaysFF, // equivalent to Always @
    Always,
    Initial,

    // declarations
    ModuleDeclaration, // this is the definition of a module body
    ParameterDeclaration,
    PortDeclaration,
    RegisterDeclaration,
    WireDeclaration,
    IntegerDeclaration,
    ArrayDeclaration,
    TimeScaleDeclaration,
    DelayDeclaration,

    // expressions,
    UnimplementedExpression,
    AdditionExpression,
    SubtractionExpression,
    MultiplicationExpression,
    LeftShiftExpression,
    RightLogicalShiftExpression,
    RightArithmeticShiftExpression,
    DivisionExpression, // only for testing cannot be used in real hardware !!!

    BitWiseAndExpression,
    BitWiseNotExpression,
    BitWiseXorExpression,
    BitWiseOrExpression,

    LogicalAndExpression,
    LogicalOrExpression,
    LogicalNotExpression,

    EqualsToExpression,
    NotEqualsToExpression,
    GreaterThanExpression,
    LessThanExpression,
    GreaterThanOrEqualsToExpression,
    LessThanOrEqualsToExpression,

    ComparsionExpression,
    ImmediateReference,
    VariableReference,
    ParenthesisExpression,
    RangeSelection,
    IndexSelection,

    // meta
    DeclarationBlock,
    FileHeader,
    Anchor,
    Comment,
    ErrorMsg,
    Root,

    // statement
    ModuleInstantiation,
    ContinuousAssignment,
    ProceduralBlocking,
    ProceduralNonBlocking,
    ForLoop,
    IfStatement,
    IfElseStatement;
}
