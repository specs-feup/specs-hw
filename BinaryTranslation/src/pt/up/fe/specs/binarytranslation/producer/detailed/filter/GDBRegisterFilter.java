package pt.up.fe.specs.binarytranslation.producer.detailed.filter;

import pt.up.fe.specs.binarytranslation.instruction.register.RegisterDump;
import pt.up.fe.specs.util.utilities.LineStream;

public class GDBRegisterFilter extends GDBFilter {
    private RegisterDump dump = new RegisterDump();
    public GDBRegisterFilter(LineStream lines) {
        super(lines);
    }

    @Override
    public boolean filter() {
        String line = null;

        while (((line = lines.peekNextLine()) != null)) {
            // System.out.println("RegFilter processing line: " + line);
            if (line.startsWith("mucounteren") || line.startsWith("shr")) {
                lines.nextLine();
                break;
            } else {
                line = line.replaceAll("\\s+", " ");
                String[] elems = line.split(" ");
                // TODO: replace by a provided regex
                if (belongsToISA(elems[0])) {
                    // System.out.println("Found valid reg: " + line);
                    try {
                        if (elems.length > 1) {
                            long val = Long.decode(elems[1]);
                            dump.add(elems[0], val);
                        }
                    } catch (Exception e) {

                    }
                }
            }
            lines.nextLine();
        }
        return !dump.isEmpty();
    }

    private boolean belongsToISA(String string) {
        // return Arrays.asList(this.regs).contains(string);
        // return Pattern.matches(pattern, string);
        return true;
    }

    @Override
    public RegisterDump getResult() {
        return dump;
    }

}
