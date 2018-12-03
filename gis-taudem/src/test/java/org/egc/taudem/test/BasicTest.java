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
        ExecResult flag = analysis.PitRemove(pitRemoveParams);
        System.out.println(flag.getOut());
        PitRemoveParams params = (PitRemoveParams) flag.getParams();
        System.out.println(pitRemoveParams.getOutput_Pit_Removed_Elevation_Grid());
        System.out.println(params.getOutput_Pit_Removed_Elevation_Grid());
    }

    @Test
    public void testFlowDir() {
        D8FlowDirectionsParams params = new D8FlowDirectionsParams.Builder(
                outdir + "xcDEM_Pit_Removed_Elevation_Grid.tif").build();
        ExecResult flag = analysis.D8FlowDirections(params);
        System.out.println(params.getOutput_D8_Slope_Grid());
        System.out.println(flag.getOut());
    }

    @Test
    public void test() {
        PitRemoveParams pitRemoveParams = new PitRemoveParams.Builder(dem).build();
        ExecResult flag = analysis.PitRemove(pitRemoveParams);
        D8FlowDirectionsParams params = new D8FlowDirectionsParams.Builder(pitRemoveParams.getOutput_Pit_Removed_Elevation_Grid()).build();
        analysis.D8FlowDirections(params);
        System.out.println(params.getOutput_D8_Slope_Grid());
    }

}
