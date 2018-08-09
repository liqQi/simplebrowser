package com.honeybilly.cleanbrowser.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by liqi on 14:40.
 */
@Entity
public class FaviconFile {
    @Id(autoincrement = true)
    private Long _id;

    private String domain;

    private String filePath;

    private long time;

    @Generated(hash = 1212091008)
    public FaviconFile(Long _id, String domain, String filePath, long time) {
        this._id = _id;
        this.domain = domain;
        this.filePath = filePath;
        this.time = time;
    }

    @Generated(hash = 1042618098)
    public FaviconFile() {
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
