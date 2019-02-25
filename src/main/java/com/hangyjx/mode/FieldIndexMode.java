package com.hangyjx.mode;

import java.io.Serializable;

/**
 * @author liuyuejie
 * @create 2018-12-26 16:15
 */
public class FieldIndexMode implements Serializable {
    private String fid;
    private String fieldname;
    private String fieldtype;
    private String analyzer;
    private String boots;
    private String standid;
    private String createdate;

    public FieldIndexMode() {
    }

    public FieldIndexMode(String fid, String fieldname, String fieldtype, String analyzer, String boots, String standid, String createdate) {
        this.fid = fid;
        this.fieldname = fieldname;
        this.fieldtype = fieldtype;
        this.analyzer = analyzer;
        this.boots = boots;
        this.standid = standid;
        this.createdate = createdate;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getFieldtype() {
        return fieldtype;
    }

    public void setFieldtype(String fieldtype) {
        this.fieldtype = fieldtype;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }

    public String getBoots() {
        return boots;
    }

    public void setBoots(String boots) {
        this.boots = boots;
    }

    public String getStandid() {
        return standid;
    }

    public void setStandid(String standid) {
        this.standid = standid;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    @Override
    public String toString() {
        return "FieldIndexMode{" +
                "fid='" + fid + '\'' +
                ", fieldname='" + fieldname + '\'' +
                ", fieldtype='" + fieldtype + '\'' +
                ", analyzer='" + analyzer + '\'' +
                ", boots='" + boots + '\'' +
                ", standid='" + standid + '\'' +
                ", createdate='" + createdate + '\'' +
                '}';
    }
}
