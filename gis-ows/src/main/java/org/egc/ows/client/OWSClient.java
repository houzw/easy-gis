package org.egc.ows.client;

/**
 * @author houzhiwei
 * @date 2020/10/15 14:27
 */
public abstract class OWSClient {

    public static WCSClient createWCSClient(String serviceRoot) {
        return new WCSClient(serviceRoot);
    }
    public static WMSClient createWMSClient(String serviceRoot) {
        return new WMSClient(serviceRoot);
    }
    public static WFSClient createWFSClient(String serviceRoot) {
        return new WFSClient(serviceRoot);
    }
}
