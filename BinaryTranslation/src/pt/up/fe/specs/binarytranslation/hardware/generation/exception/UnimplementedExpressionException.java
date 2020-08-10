package pt.up.fe.specs.binarytranslation.hardware.generation.exception;

public class UnimplementedExpressionException extends HardwareGenerationException {

    /**
     * 
     */
    private static final long serialVersionUID = 2875821032237934349L;

    public UnimplementedExpressionException(String error) {
        super(error);
    }
}
