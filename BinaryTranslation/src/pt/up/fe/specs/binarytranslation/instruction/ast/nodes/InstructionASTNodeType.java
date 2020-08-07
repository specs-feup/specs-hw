package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public enum InstructionASTNodeType {

    PseudoInstructionNode,

    // statements
    StatementNode,
    AssignmentStatementNode,
    UnaryStatement,

    // expressions
    BinaryExpressionNode,
    UnaryExpressionNode,

    // abstract
    OperandNode,

    // using only info from pseudocode
    BareOperandNode,
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
