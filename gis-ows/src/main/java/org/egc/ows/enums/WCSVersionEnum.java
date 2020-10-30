package org.egc.ows.enums;

/**
 * @author houzhiwei
 * @date 2020/8/25 20:41
 */
public enum WCSVersionEnum {
    Version_100("1.0.0"),
    Version_110("1.1.0"),
    Version_111("1.1.1"),
    Version_112("1.1.2"),
    Version_20("2.0");

    private String version;

    WCSVersionEnum(String version) {
        this.version = version;
    }
}
