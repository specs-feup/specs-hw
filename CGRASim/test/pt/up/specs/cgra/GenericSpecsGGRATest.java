package pt.up.specs.cgra;

import org.junit.Test;

import pt.up.specs.cgra.structure.GenericSpecsCGRA;
import pt.up.specs.cgra.structure.pes.AdderElement;
import pt.up.specs.cgra.structure.pes.MultiplierElement;

public class GenericSpecsGGRATest {

    @Test
    public void testInstantantiateAndView() {
        var CGRAbld = new GenericSpecsCGRA.Builder(2, 2);
        CGRAbld.withHomogeneousPE(new AdderElement());
        CGRAbld.withProcessingElement(new MultiplierElement(), 0, 1);
        var cgra = CGRAbld.build();
        cgra.visualize();
    }
}
