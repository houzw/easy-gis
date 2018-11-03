package org.egc.taudem.test;

import org.egc.gis.taudem.BasicGridAnalysis;
import org.egc.gis.taudem.StreamNetworkAnalysis;
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
String outdir = "H:/gisdemo/out/taudem/1102/";

    @Test
    public void testPitRemove() {
//        boolean flag = BasicGridAnalysis.PitRemove(dem, "xcDEM_Pit_Removed_Elevation_27.tif", null, outdir);
        boolean flag = BasicGridAnalysis.PitRemove(dem, null, null, outdir);
        System.out.println(flag);
    }

    @Test
    public void testFlowDir() {

        boolean flag = BasicGridAnalysis.D8FlowDirections(outdir+"xcDEM_Pit_Removed_Elevation_Grid.tif",
                                                   "",
                                                   "",null,outdir);
        System.out.println(flag);
    }

    @Test
    public void testSCA() {

        BasicGridAnalysis.D8ContributingArea(outdir+"xcDEM_D8_Flow_Direction_Grid.tif",
                                             "",null,outdir);

    }

    @Test
    public void test() {

        StreamNetworkAnalysis.ConnectDown("H:/gisdemo/in/test/cubdemp.tif",
                                          "H:/gisdemo/in/test/cubdemad8.tif",
                                          "H:/gisdemo/in/test/cubdemw.tif");

    }


}
