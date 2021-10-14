package org.egc.ows.client;

/**
 * TODO
 * @author houzhiwei
 * @date 2020/10/15 14:41
 */
public class WFSClient {
    public WFSClient(String serviceRoot) {
        this.serviceRoot = serviceRoot;
    }

    private String serviceRoot;

    public String getServiceRoot() {
        return this.serviceRoot;
    }
}
