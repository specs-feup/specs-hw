package pt.up.fe.f4pga;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.utilities.Replacer;

public class FPGA_Flow {

    String flowString;
    String hdlSourceDir;
    String topName;
    List<String> targetList;
    String buildDir;
    XDC xilinixDC;

    public String newFlow(String _hdlSourceDir, String _topName) {

        flowString = SpecsIo.getResource("flow.json");

        var _targetList = new ArrayList<>();
        _targetList.add("bitstream");

        sourceFromList(_hdlSourceDir);
        changeTopName(_topName);
        changeTarget(_targetList);
        changeBuildDir("local_dir");
        // default values from creating a flow.json file

        return flowString;
    }

    public void sourceFromList(String _hdlSourceDir) {

        var replacer = new Replacer(flowString);
        var sourceListString = "TODO";

        replacer.replace("<source_list>", sourceListString);
        hdlSourceDir = _hdlSourceDir;
    }

    public void changeTopName(String _topName) {

        var replacer = new Replacer(flowString);

        replacer.replace("<source_list>", _topName);
        topName = _topName;
    }

    public void changeBuildDir(String _buildDir) {

        var replacer = new Replacer(flowString);

        replacer.replace("build_dir>", _buildDir);
        buildDir = _buildDir;
    }

    public void changeTarget(List<String> _targetList) {

        var replacer = new Replacer(flowString);
        String sourceListString = "TODO";

        replacer.replace("<source_list>", sourceListString);
        targetList = _targetList;
    }
}
