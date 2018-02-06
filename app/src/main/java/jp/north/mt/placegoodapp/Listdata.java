package jp.north.mt.placegoodapp;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Listdata extends RealmObject implements Serializable {
    private Date date; //日時
    private String title; //タイトル
    private String place; //場所
    private int latitude; //緯度
    private int longitude; //経度
    private String comment; //コメント
    private byte[] BitmapArray; //写真

    //id をプライマリーキーとして設定
    @PrimaryKey
    private int id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public byte[] getImageBytes() {
        return BitmapArray;
    }

    public void setImageBytes(byte[] bitmapArray) {
        this.BitmapArray = bitmapArray;
    }


}