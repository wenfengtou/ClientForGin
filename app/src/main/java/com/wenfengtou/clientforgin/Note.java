package com.wenfengtou.clientforgin;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


@Entity
public class Note {

    @Id(autoincrement = true)
    private Long id;
    private String data;
    private String comment;
    @Generated(hash = 81631006)
    public Note(Long id, String data, String comment) {
        this.id = id;
        this.data = data;
        this.comment = comment;
    }
    @Generated(hash = 1272611929)
    public Note() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }


}
