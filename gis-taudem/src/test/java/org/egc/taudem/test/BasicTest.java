package org.egc.taudem.test;

import org.egc.commons.command.ExecResult;
import org.egc.gis.taudem.TauDEMAnalysis;
import org.egc.gis.taudem.params.D8FlowDirectionsParams;
import org.egc.gis.taudem.params.PitRemoveParams;
import org.junit.Before;
import org.junit.Test;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/10/22 14:11
 */
public class BasicTest {

    String dem = "H:/xcDEM.tif";
    String outdir = "H:/gisdemo/out/taudem/1126/";
    TauDEMAnalysis analysis;

    @Before
    public void init() {
        analysis = TauDEMAnalysis.getInstance();
        analysis.init(outdir);
    }

    @Test
    public void testPitRemove() {
        PitRemoveParams pitRemoveParams = new PitRemoveParams.Builder(dem).build();
        ExecResult flag = analysis.pitRemove(pitRemoveParams);
        System.out.println(flag.getOut());
        PitRemoveParams params = (PitRemoveParams) flag.getParams();
        System.out.println(pitRemoveParams.getPitFilledElevation());
        System.out.println(params.getPitFilledElevation());
    }

    @Test
    public void testFlowDir() {
        D8FlowDirectionsParams params = new D8FlowDirectionsParams.Builder(
                outdir + "xcDEM_pitFilledElevation.tif").build();
        ExecResult flag = analysis.d8FlowDirections(params);
        System.out.println(params.getD8Slope());
        System.out.println(flag.getOut());
    }

    @Test
    public void test() {
        PitRemoveParams pitRemoveParams = new PitRemoveParams.Builder(dem).build();
        ExecResult flag = analysis.pitRemove(pitRemoveParams);
        D8FlowDirectionsParams params = new D8FlowDirectionsParams.Builder(pitRemoveParams.getPitFilledElevation()).build();
        analysis.d8FlowDirections(params);
        System.out.println(params.getD8Slope());
    }
}
