package pt.up.fe.specs.binarytranslation.hardware.generation.exception;

public class UnimplementedStatementException extends HardwareGenerationException {

    /**
     * 
     */
    private static final long serialVersionUID = 6564295732380033621L;

    public UnimplementedStatementException(String error) {
        super(error);
    }
}
