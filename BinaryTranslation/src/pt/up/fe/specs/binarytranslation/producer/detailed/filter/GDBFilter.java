package pt.up.fe.specs.binarytranslation.producer.detailed.filter;

import java.util.regex.Pattern;

import pt.up.fe.specs.util.utilities.LineStream;

public abstract class GDBFilter {
    protected LineStream lines;
    
    public GDBFilter(LineStream lines) {
        this.lines = lines;
    }
    
    public abstract boolean filter();
    
    public abstract Object getResult();
}
