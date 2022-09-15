package org.egc.gis.mapserver.entities;

/**
 * @author houzhiwei
 * @date 2020/9/8 20:05
 */
public class OWSRequest {

    private int numberParams;

    public int getNumParams() {
        return this.numberParams;
    }

    public enum REQUEST_TYPE {
        Get, Post
    }

    private REQUEST_TYPE type;


    private String contentType;



    public String postrequest;



    public String Httpcookiedata;


    public OWSRequest() {

    }

   /* public int loadParams() {
        return loadParams();
    }

    public int loadParamsFromURL(String url) {

    }

    public void setParameter(String name, String value) {

    }

    public void addParameter(String name, String value) {

    }*/

    private String name;


    private String value;

    public String getValue(int index) {
        return this.value;
    }

   /* public String getValueByName(String name) {
    }*/
}
