package org.egc.gis.gdal.raster;

/**
 * Description:
 * <pre>
 * Raster calculation (map algebra)
 *
 * Average two rasters:
 *
 * gdal_calc.py -A input1.tif -B input2.tif --outfile=output.tif --calc="(A+B)/2"
 *
 * Add two rasters:
 *
 * gdal_calc.py -A input1.tif -B input2.tif --outfile=output.tif --calc="A+B"
 *
 * etc.
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/11/2 11:54
 */
public interface MapAlgebra {
}
