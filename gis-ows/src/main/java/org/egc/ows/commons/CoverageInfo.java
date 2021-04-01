package org.egc.ows.commons;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author houzhiwei
 * @date 2020/10/18 16:44
 */
public class CoverageInfo implements Serializable {
    @Override
    public String toString() {
        return "CoverageInfo{" +
                "keywords=" + keywords +
                ", coverageId='" + coverageId + '\'' +
                ", title='" + title + '\'' +
                ", srsName='" + srsName + '\'' +
                ", srsNameShort='" + srsNameShort + '\'' +
                ", wgs84LowerCorner=" + Arrays.toString(wgs84LowerCorner) +
                ", lowerCorner=" + Arrays.toString(lowerCorner) +
                ", upperCorner=" + Arrays.toString(upperCorner) +
                ", wgs84UpperCorner=" + Arrays.toString(wgs84UpperCorner) +
                ", timePositions=" + timePositions +
                '}';
    }

    private List<String> keywords;

    public String getCoverageId() {
        return coverageId;
    }

    public void setCoverageId(String coverageId) {
        this.coverageId = coverageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSrsName() {
        return srsName;
    }

    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }

    public String getSrsNameShort() {
        if (this.srsName != null) {
            if (this.srsName.startsWith("urn:")) {
                String[] split = this.srsName.split(":");
                this.srsNameShort = split[split.length - 1];
            } else {
                this.srsNameShort = srsName;
            }
        }
        return srsNameShort;
    }

    public double[] getLowerCorner() {
        return lowerCorner;
    }

    public void setLowerCorner(double minx, double miny) {
        this.lowerCorner = new double[]{minx, miny};
    }

    public double[] getUpperCorner() {
        return upperCorner;
    }

    public void setUpperCorner(double maxx, double maxy) {
        this.upperCorner = new double[]{maxx, maxy};
    }

    public List<String> getTimePositions() {
        return timePositions;
    }

    public void setTimePositions(List<String> timePositions) {
        this.timePositions = timePositions;
    }

    private String coverageId;
    private String title;
    private String srsName;
    private String srsNameShort;

    public double[] getWgs84LowerCorner() {
        return wgs84LowerCorner;
    }

    public void setWgs84LowerCorner(double minx, double miny) {
        this.wgs84LowerCorner = new double[]{minx, miny};
    }

    public double[] getWgs84UpperCorner() {
        return wgs84UpperCorner;
    }

    public void setWgs84UpperCorner(double maxx, double maxy) {
        this.wgs84UpperCorner = new double[]{maxx, maxy};
    }

    private double[] wgs84LowerCorner;
    private double[] lowerCorner;
    private double[] upperCorner;
    private double[] wgs84UpperCorner;
    private List<String> timePositions;

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
