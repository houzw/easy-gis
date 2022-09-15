package org.egc.gis.wps;

/**
 * @author houzhiwei
 * @date 2020/12/14 16:11
 */
@Deprecated
public class WpsInputDTO {

    private String inputName;
    private String schema;
    private String mimeType;
    private String encoding;
    Object inputValue;


    public WpsInputDTO(Object inputValue) {
        this.inputValue = inputValue;
    }

    public WpsInputDTO(String inputName, Object inputValue) {
        this.inputValue = inputValue;
        this.inputName = inputName;
    }

    public WpsInputDTO(Object inputValue, String mimeType) {
        this.inputValue = inputValue;
        this.mimeType = mimeType;
    }

    public WpsInputDTO(Object inputValue, String mimeType, String encoding) {
        this.inputValue = inputValue;
        this.mimeType = mimeType;
        this.encoding = encoding;
    }

    public WpsInputDTO(String inputName, Object inputValue, String mimeType, String schema, String encoding) {
        this.inputValue = inputValue;
        this.inputName = inputName;
        this.mimeType = mimeType;
        this.schema = schema;
        this.encoding = encoding;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Object getInputValue() {
        return inputValue;
    }

    public void setInputValue(Object inputValue) {
        this.inputValue = inputValue;
    }

    @Override
    public String toString() {
        return "WpsInputDTO{" +
                "inputName='" + inputName + '\'' +
                ", schema='" + schema + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", encoding='" + encoding + '\'' +
                ", inputValue=" + inputValue +
                '}';
    }
}
