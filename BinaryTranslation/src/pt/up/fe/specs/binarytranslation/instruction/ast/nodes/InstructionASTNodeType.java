package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public enum InstructionASTNodeType {

    PseudoInstructionNode,

    // statements
    PlainStatementNode,
    IfStatementNode,
    IfElseStatementNode,

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
