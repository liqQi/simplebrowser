package com.honeybilly.cleanbrowser.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by liqi on 9:33.
 */
@Entity
public class BookMark {
    @Id(autoincrement = true)
    private Long _id;

    private String title;

    private String url;

    private Long timeStamp;

    @Generated(hash = 624069782)
    public BookMark(Long _id, String title, String url, Long timeStamp) {
        this._id = _id;
        this.title = title;
        this.url = url;
        this.timeStamp = timeStamp;
    }

    @Generated(hash = 1704575762)
    public BookMark() {
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
