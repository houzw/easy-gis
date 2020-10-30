package org.egc.ows.client;

/**
 * @author houzhiwei
 * @date 2020/8/25 19:40
 */
public class WMSClient  {
    private String serviceRoot;

    public WMSClient(String serviceRoot) {
        this.serviceRoot = serviceRoot;
    }

    public String getServiceRoot() {
        return this.serviceRoot;
    }
}
