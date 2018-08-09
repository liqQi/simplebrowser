package com.honeybilly.cleanbrowser.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by liqi on 14:07.
 */
@Entity
public class WebHistory {

    @Id(autoincrement = true)
    private Long  _id;

    @Unique
    private String url;

    private String title;

    private String iconId;

    @Index
    private long dateTime;

    private String domain;

    @Generated(hash = 1224941040)
    public WebHistory(Long _id, String url, String title, String iconId,
            long dateTime, String domain) {
        this._id = _id;
        this.url = url;
        this.title = title;
        this.iconId = iconId;
        this.dateTime = dateTime;
        this.domain = domain;
    }

    @Generated(hash = 549319939)
    public WebHistory() {
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
