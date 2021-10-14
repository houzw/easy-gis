package org.egc.proj.test;

import org.egc.gis.proj.ProjUtilities;
import org.junit.Test;

/**
 * @author houzhiwei
 * @date 2020/11/26 20:55
 */
public class ProjTest {
    @Test
    public void testProjUtils() {
        String lf_1 = "119d17'16.89\"E";
        String lf_2 = "31d6'57.12\"N";
        String ur_1 = "119d30'25.94\"E";
        String ur_2 = "31d15' 6.22\"N";
        System.out.println(ProjUtilities.parseDms2dd(lf_1));
        System.out.println(ProjUtilities.parseDms2dd(lf_2));
        System.out.println(ProjUtilities.parseDms2dd(ur_1));
        System.out.println(ProjUtilities.parseDms2dd(ur_2));


        // 77° 30' 29.9988" S  ->  	-77.508333
        System.out.println(ProjUtilities.parseDms2dd("77° 30' 29.9988\" S"));
        System.out.println(ProjUtilities.parseDms2dd("0d0'30\""));
    }

    @Test
    public void testDis() {
        System.out.println(ProjUtilities.degree2meters(0.0037));
        System.out.println(ProjUtilities.degree2meters(0.0675));
        System.out.println(ProjUtilities.degree2meters(0.477464829275686));
    }

}
