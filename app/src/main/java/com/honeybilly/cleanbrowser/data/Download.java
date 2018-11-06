package com.honeybilly.cleanbrowser.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by liqi on 15:07.
 */
@Entity
public class Download {
    @Id(autoincrement = true)
    private Long _id;

    private String name;

    private String url;

    private String path;

    private long date;

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Generated(hash = 1833866996)
    public Download(Long _id, String name, String url, String path, long date,
            int status) {
        this._id = _id;
        this.name = name;
        this.url = url;
        this.path = path;
        this.date = date;
        this.status = status;
    }

    @Generated(hash = 1462805409)
    public Download() {
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
