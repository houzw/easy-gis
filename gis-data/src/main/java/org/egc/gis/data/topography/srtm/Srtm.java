package org.egc.gis.data.topography.srtm;

/**
 * @author houzhiwei
 * @date 2020/5/4 22:36
 */
public class Srtm {
    private static final int SRTM_NaN = -32768;
    private SrtmData srtmData;

    /**
     * @param directory Cache directory
     */
    public Srtm(String directory) {
        srtmData = new SrtmData(directory);
    }

    /**
     * Get SRTM elevation for geographic coordinate (WGS-84)
     * <p>
     * Stores a cache of uncompressed SRTM data files at the default directory.
     * It need a Internet connection to fetch SRTM files if they are not in the
     * disk
     *
     * @return Above Sea Level (ASL) altitude in meters
     */
    public int getData(double longitude, double latitude) {
        try {
            return srtmData.load(longitude, latitude);
        } catch (Exception e) {
            e.printStackTrace();
            return SRTM_NaN;
        }
    }
}
