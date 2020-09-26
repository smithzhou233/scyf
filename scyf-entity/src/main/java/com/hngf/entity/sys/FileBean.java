package com.hngf.entity.sys;

import java.io.Serializable;

public class FileBean implements Serializable {
    private static final long serialVersionUID = 8656597559014685635L;
    private String type;
    private long size;
    private String ext;
    private String path;
    private String oriFileName;
    private String saveFileName;
    private String mimeType;

    public FileBean() {
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getExt() {
        return this.ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOriFileName() {
        return this.oriFileName;
    }

    public void setOriFileName(String oriFileName) {
        this.oriFileName = oriFileName;
    }

    public String getSaveFileName() {
        return this.saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }
}
