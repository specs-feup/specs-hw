package pt.up.fe.specs.binarytranslation.instruction.ast.nodes;

public enum InstructionASTNodeType {

    PseudoInstructionNode,
    StatementNode,
    BinaryExpressionNode,
    UnaryExpressionNode,

    OperandNode,
    LiveInNode,
    LiveOutNode,
    VariableNode,
    LiteralNode,

    OperatorNode,
    NumberNode,
    Generic
}
