package com.hangyjx.mode;

import java.io.Serializable;

/**
 * @author liuyuejie
 * @create 2018-12-26 14:42
 */

public class IndexMode implements Serializable {
    private static final long serialVersionUID = 7140733682479717063L;
    private String standid;
    private String indexname;
    private String shardsnum;
    private String replicasnum;
    private String indexremark;
    private String createdate;
    private String iscreat;
    private String indexstatus;
    public IndexMode(){

    }

    public IndexMode(String standid, String indexname, String shardsnum, String replicasnum, String indexremark, String createdate, String iscreat, String indexstatus) {
        this.standid = standid;
        this.indexname = indexname;
        this.shardsnum = shardsnum;
        this.replicasnum = replicasnum;
        this.indexremark = indexremark;
        this.createdate = createdate;
        this.iscreat = iscreat;
        this.indexstatus = indexstatus;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStandid() {
        return standid;
    }

    public void setStandid(String standid) {
        this.standid = standid;
    }

    public String getIndexname() {
        return indexname;
    }

    public void setIndexname(String indexname) {
        this.indexname = indexname;
    }

    public String getShardsnum() {
        return shardsnum;
    }

    public void setShardsnum(String shardsnum) {
        this.shardsnum = shardsnum;
    }

    public String getReplicasnum() {
        return replicasnum;
    }

    public void setReplicasnum(String replicasnum) {
        this.replicasnum = replicasnum;
    }

    public String getIndexremark() {
        return indexremark;
    }

    public void setIndexremark(String indexremark) {
        this.indexremark = indexremark;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getIscreat() {
        return iscreat;
    }

    public void setIscreat(String iscreat) {
        this.iscreat = iscreat;
    }

    public String getIndexstatus() {
        return indexstatus;
    }

    public void setIndexstatus(String indexstatus) {
        this.indexstatus = indexstatus;
    }

    @Override
    public String toString() {
        return "IndexMode{" +
                "standid='" + standid + '\'' +
                ", indexname='" + indexname + '\'' +
                ", shardsnum='" + shardsnum + '\'' +
                ", replicasnum='" + replicasnum + '\'' +
                ", indexremark='" + indexremark + '\'' +
                ", createdate='" + createdate + '\'' +
                ", iscreat='" + iscreat + '\'' +
                ", indexstatus='" + indexstatus + '\'' +
                '}';
    }
}
