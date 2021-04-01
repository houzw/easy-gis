package org.egc.gis.data.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.exception.BusinessException;
import org.egc.ows.client.OWSClient;
import org.egc.ows.client.WCSClient;
import org.egc.ows.commons.CoverageInfo;
import org.egc.ows.commons.WCSUtils;
import org.egc.ows.request.wcs.GetCoverageRequest20;

import java.io.File;
import java.util.List;

/**
 * @author houzhiwei
 * @date 2020/11/9 20:04
 */
@Slf4j
public class CoverageService {
    private String serviceEndpoint;
    private String fileTempDir;
    WCSClient client;
    private WCSVersionEnum wcsVersionEnum = WCSVersionEnum.Version_201;

    public CoverageService(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
        fileTempDir = FileUtils.getTempDirectoryPath();
        this.client = OWSClient.createWCSClient(this.serviceEndpoint);
    }

    /**
     * Gets coverage.
     *
     * @param coverageId    the coverage id
     * @param minMaxLatLong the min max lat and long: [minLat,  minLong,  maxLat,  maxLong]
     * @param subsetEpsg    the subset epsg
     * @param outputEpsg    the output epsg
     * @param filepath      the filepath
     * @return the coverage
     */
    public String getCoverage(String coverageId, double[] minMaxLatLong, int subsetEpsg, int outputEpsg, String filepath) {
        List<String> coverages = availableCoverages();
        if (!coverages.contains(coverageId)) {
            throw new BusinessException("Coverage with id [ " + coverageId + " ] not exists! Please check!");
        }
        return getCoverage(coverageId, minMaxLatLong[0], minMaxLatLong[1], minMaxLatLong[2], minMaxLatLong[3], subsetEpsg, outputEpsg, filepath);
    }

    /**
     * Gets coverage.
     *
     * @param coverageId the coverage id
     * @param minLat     the min lat
     * @param minLong    the min long
     * @param maxLat     the max lat
     * @param maxLong    the max long
     * @param subsetEpsg the subset epsg, 上述四至坐标的 epsg，若不想设置则赋值为 0
     * @param outputEpsg the output epsg，输出数据的 epsg，若不想设置则赋值为 0
     * @return the coverage file name
     */
    public String getCoverage(String coverageId, double minLat, double minLong, double maxLat, double maxLong, int subsetEpsg, int outputEpsg, String filepath) {
        List<String> coverages = availableCoverages();
        if (!coverages.contains(coverageId)) {
            throw new BusinessException("Coverage with id [ " + coverageId + " ] not exists! Please check!");
        }
        GetCoverageRequest20.Builder builder = new GetCoverageRequest20.Builder(coverageId);
        //1024-32767
        if (subsetEpsg > 1023 && subsetEpsg < 32768) {
            builder.subsetLatLong(minLat, minLong, maxLat, maxLong, subsetEpsg);
        } else {
            builder.subsetLatLong(minLat, minLong, maxLat, maxLong);
        }
        if (outputEpsg > 1023 && outputEpsg < 32768) {
            builder.outputEpsg(outputEpsg);
        }
        GetCoverageRequest20 getCoverageRequest20 = builder.build();
        log.debug("build getCoverageRequest20");
        return client.getCoverage20(getCoverageRequest20, getFilepath(filepath));
    }

    public String getCoverage(String coverageId, double minLat, double minLong, double maxLat, double maxLong, String filepath) {
        List<String> coverages = availableCoverages();
        if (!coverages.contains(coverageId)) {
            throw new BusinessException("Coverage with id [ " + coverageId + " ] not exists! Please check!");
        }
        GetCoverageRequest20.Builder builder = new GetCoverageRequest20.Builder(coverageId);
        builder.subsetLatLong(minLat, minLong, maxLat, maxLong);
        GetCoverageRequest20 getCoverageRequest20 = builder.build();
        return client.getCoverage20(getCoverageRequest20, getFilepath(filepath));
    }

    private String getFilepath(String filepath) {
        if (StringUtils.isBlank(filepath)) {
            filepath = this.fileTempDir;
        } else if (StringUtils.isBlank(FilenameUtils.getFullPath(filepath))) {
            filepath = FilenameUtils.normalize(this.fileTempDir + File.separator + filepath);
        }
        return filepath;
    }


    public List<String> availableCoverages() {
        return client.availableConverageIds20(client.getCapabilities20());
    }

    public List<CoverageInfo> availableCoverageInfo() {
        return WCSUtils.getCoveragesInfo20(client.getCapabilities20());
    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }


    public WCSVersionEnum getWcsVersionEnum() {
        return wcsVersionEnum;
    }

    public void setWcsVersionEnum(WCSVersionEnum wcsVersionEnum) {
        this.wcsVersionEnum = wcsVersionEnum;
    }

    public String getFileTempDir() {
        return fileTempDir;
    }

    public void setFileTempDir(String fileTempDir) {
        this.fileTempDir = fileTempDir;
    }


}
