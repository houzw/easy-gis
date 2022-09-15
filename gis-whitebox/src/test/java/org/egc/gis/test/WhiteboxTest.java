package org.egc.gis.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.egc.commons.command.ExecResult;
import org.egc.gis.whitebox.WhiteboxTools;
import org.egc.gis.whitebox.params.BreachDepressionsParams;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author houzhiwei
 * @date 2020/5/24 10:50
 */
@Slf4j
public class WhiteboxTest {
    WhiteboxTools analysis;
    String outdir = "H:/gisdemo/out/whitebox";
    String dem = "H:/xcDEM.tif";

    @Before
    public void init() {
        analysis = WhiteboxTools.getInstance();
        analysis.init(outdir);
        Configurator.setAllLevels("", Level.DEBUG);
    }

    @Test
    public void depressions() throws IOException {
        BreachDepressionsParams depressionsParams = new BreachDepressionsParams.Builder(dem).filledDem("filled2.tif").build();
        ExecResult flag = analysis.breachDepressions(depressionsParams);
        System.out.println(flag.getOut());
        BreachDepressionsParams params = (BreachDepressionsParams) flag.getParams();
        System.out.println(params.getFilledDem());
//        System.out.println(params.getPitFilledElevation());
    }

}
