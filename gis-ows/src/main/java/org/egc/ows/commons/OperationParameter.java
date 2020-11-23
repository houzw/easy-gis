package org.egc.ows.commons;

/**
 * @author houzhiwei
 * @date 2020/11/8 11:15
 */
public class OperationParameter {
    private String identifier;
    private boolean optional = false;

    public OperationParameter(String identifier) {
        this.identifier = identifier;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "OperationParameter{" +
                "identifier='" + identifier + '\'' +
                ", optional=" + optional +
                '}';
    }
}
