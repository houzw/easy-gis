package org.egc.gis.data.formats;

import lombok.extern.slf4j.Slf4j;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFiles;

import java.io.IOException;

/**
 * description:
 * https://docs.unidata.ucar.edu/netcdf-java/current/userguide/reading_cdm.html
 * @author houzhiwei
 * @date 2022/8/9/0009 11:26
 */
@Slf4j
public class NetcdfUtils {

    public static void readNetCdf(String filename) {
        try (NetcdfFile ncfile = NetcdfFiles.open(filename)) {
            // Do cool stuff here
            ncfile.getDetailInfo();
            ncfile.getVariables();
        } catch (IOException ioe) {
            // Handle less-cool exceptions here
            log.error("Try to open NetCdfFile failed ", ioe);
        }
    }

    /**
     * 从全球nc数据中裁剪指定区域的数据
     * https://blog.csdn.net/w779050550/article/details/81090286
     *
     * @param src      nc文件所在位置路径
     * @param startLon 指定区域开始的经度
     * @param startLat 指定区域开始的纬度
     * @param latCount 纬度要读取多少个点
     * @param lonCount 经度要读取多少个点
     * @param begin    从时间纬度的第几层开始读取
     * @param end      第几层结束
     * @return 指定区域的数据
     */
    /*public static short[][] readNCfile(String src, int latCount, int lonCount,
                                       float startLon, float startLat,
                                       int begin, int end) {
        try {
            NetcdfFile ncFile = NetcdfFiles.open(src);
            //读取qpf_ml的变量，
            Variable v = ncFile.findVariable("qpf_ml");
            //
            short[][] values = null;
            //本读取的qpf_ml是一个3维数据，时间、经度、维度，一下子把3维数据全部读出来会很大，时间维度是24层，所以通过遍历时间维度获取数据，i为时间维度的层数
            for (int i = begin; i < end; i++) {
                //origin 设置维度准备从哪个位置开始读取数据
                int[] origin = {i, (int) ((latNorth - startLat) * 100), (int) ((startLon - lonEast) * 100)};
                //shape 和origin对应，设置读取多少点后结束
                int[] shape = {1, latCount, lonCount};
                //去掉时间维度，变为二维
                short[][] temp = (short[][]) v.read(origin, shape).reduce(0).copyToNDJavaArray();

                if (values != null) {
                    for (int j = 0; j < latCount; j++) {
                        for (int k = 0; k < lonCount; k++) {
                            values[j][k] += temp[j][k];
                        }
                    }
                } else {
                    values = temp;
                }
            }
            ncFile.close();
            return values;
        } catch (IOException | InvalidRangeException | ucar.ma2.InvalidRangeException e) {
            e.printStackTrace();
        }
        return null;
    }
*/
}
