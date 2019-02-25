package com.hangyjx.mode;

import java.io.Serializable;

/**
 * @author liuyuejie
 * @create 2018-12-26 20:52
 */
public class QueryFieldIndexMode implements Serializable{
    private String aid;
    private String queryfieldname;
    private String queryanalyzer;
    private String querybool;
    private String standid;
    private String createdate;

    public QueryFieldIndexMode() {
    }

    public QueryFieldIndexMode(String aid, String queryfieldname, String queryanalyzer, String querybool, String standid, String createdate) {
        this.aid = aid;
        this.queryfieldname = queryfieldname;
        this.queryanalyzer = queryanalyzer;
        this.querybool = querybool;
        this.standid = standid;
        this.createdate = createdate;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getQueryfieldname() {
        return queryfieldname;
    }

    public void setQueryfieldname(String queryfieldname) {
        this.queryfieldname = queryfieldname;
    }

    public String getQueryanalyzer() {
        return queryanalyzer;
    }

    public void setQueryanalyzer(String queryanalyzer) {
        this.queryanalyzer = queryanalyzer;
    }

    public String getQuerybool() {
        return querybool;
    }

    public void setQuerybool(String querybool) {
        this.querybool = querybool;
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
        return "QueryFieldIndexMode{" +
                "aid='" + aid + '\'' +
                ", queryfieldname='" + queryfieldname + '\'' +
                ", queryanalyzer='" + queryanalyzer + '\'' +
                ", querybool='" + querybool + '\'' +
                ", standid='" + standid + '\'' +
                ", createdate='" + createdate + '\'' +
                '}';
    }
}
