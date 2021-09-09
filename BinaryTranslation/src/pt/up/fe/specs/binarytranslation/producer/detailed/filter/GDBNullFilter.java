package pt.up.fe.specs.binarytranslation.producer.detailed.filter;

import pt.up.fe.specs.util.utilities.LineStream;

@Deprecated
public class GDBNullFilter extends GDBFilter {

    public GDBNullFilter(LineStream lines) {
        super(lines);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean filter() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object getResult() {
        // TODO Auto-generated method stub
        return null;
    }

}
