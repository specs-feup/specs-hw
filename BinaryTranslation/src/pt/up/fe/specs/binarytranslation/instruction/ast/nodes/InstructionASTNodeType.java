package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public enum InstructionASTNodeType {

    PseudoInstructionNode,
    StatementNode,
    BinaryExpressionNode,
    UnaryExpressionNode,

    // abstract
    OperandNode,

    // using only info from pseudocode
    BareOperandNode,

    // types of operands with information
    // completed from executed instruction
    LiveInNode,
    LiveOutNode,
    VariableNode,
    LiteralNode,

    OperatorNode,
    NumberNode,
    Generic
}
