package org.egc.gis.data.topography.srtm;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author houzhiwei
 * @date 2020/5/4 22:34
 */
public class SrtmRegions {
    static final String[] REGIONS = {"Eurasia", "Africa", "Australia", "Islands", "North_America", "South_America"};
    private Map<String, Integer> regionMap = new HashMap<String, Integer>();
    private String path;

    public SrtmRegions(String path) {
        this.path = path;
    }

    /**
     * Returns region name for a file
     */
    public String findRegion(String fname) throws Exception {
        if (regionMap.isEmpty()) {
            fillRegionData();
        }
        String name = fname.replace(".hgt", "");
        if (regionMap.containsKey(name)) {
            return REGIONS[regionMap.get(name)];
        }
        throw new Exception("Null Region");
    }


    private void fillRegionData() throws Exception {
        String region;
        for (int i = 0; i < SrtmRegions.REGIONS.length; i++) {
            region = SrtmRegions.REGIONS[i];
            String indexPath = region;
            indexPath = SrtmDownloader.getIndexPath(path) + indexPath;
            File indexDir = new File(indexPath);
            if (!indexDir.exists()) {
                indexDir.mkdirs();
            }
            indexPath += ".index.html";
            File indexFile = new File(indexPath);
            if (!indexFile.exists()) {
                try {
                    new SrtmDownloader().downloadRegionIndex(i, path);
                } catch (IOException e) {
                    // download error, try again with the next attempt
                    regionMap.clear();
                    throw new Exception("Null Region");
                }
            }
            Scanner scanner = new Scanner(indexFile);
            while (scanner.hasNext()) {
                String line = scanner.next();
                if (line.contains("href=\"")) {
                    int index = line.indexOf(".hgt.zip") - 7;
                    if (index >= 0) {
                        String srtm = line.substring(index, index + 7);
                        regionMap.put(srtm, i);
                    } else {
                        index = line.indexOf("hgt.zip") - 7;
                        if (index >= 0) {
                            String srtm = line.substring(index, index + 7);
                            regionMap.put(srtm, i);
                        }
                    }
                }
            }
            scanner.close();
        }
    }
}
