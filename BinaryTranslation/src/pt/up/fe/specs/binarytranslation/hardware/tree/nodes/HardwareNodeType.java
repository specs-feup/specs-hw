package pt.up.fe.specs.binarytranslation.hardware.tree.nodes;

public enum HardwareNodeType {

    // constructs
    AlwaysComb,
    AlwaysFF,

    // declarations
    ModuleDeclaration,
    ParameterDeclaration,
    PortDeclaration,
    RegisterDeclaration,
    WireDeclaration,

    // expressions,
    UnimplementedExpression,
    AdditionExpression,
    SubtractionExpression,
    MultiplicationExpression,
    ImmediateReference,
    VariableReference,
    ParenthesisExpression,
    RangeSelection,

    // meta
    DeclarationBlock,
    FileHeader,
    Anchor,
    Comment,
    ErrorMsg,
    Root,

    // statement
    ContinuousAssignment,
    ProceduralBlocking,
    ProceduralNonBlocking,
    IfStatement;
}
