package org.egc.ows.enums;

/**
 * @author houzhiwei
 * @date 2020/8/25 20:41
 */
public enum VersionEnum {
    Version_100("1.0.0"),
    Version_110("1.1.0"),
    Version_111("1.1.1"),
    Version_130("1.3.0"),
    Version_200("2.0.0"),
    Version_20("2.0"),
    Version_201("2.0.1"),
    Version_30("3.0");

    private String version;

    VersionEnum(String version) {
        this.version = version;
    }
}
