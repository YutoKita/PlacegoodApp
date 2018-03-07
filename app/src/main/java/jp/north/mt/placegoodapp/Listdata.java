package jp.north.mt.placegoodapp;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Listdata extends RealmObject implements Serializable {
    private String mTitle; //タイトル
    private Date mDate; //日時
    private String mPlace; //場所
    private int mLatitude; //緯度
    private int mLongitude; //経度
    private String mComment; //コメント
    private byte[] mBitmapArray; //写真

    //引数なしのコンストラクタを追加(エラー「Class "Listdata" must declare a public constructor with no arguments if it contains custom constructors.」対応)
    public Listdata(){
        super();
    }

    //id をプライマリーキーとして設定
    @PrimaryKey
    private int id;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String place) {
        this.mPlace = place;
    }

    public int getLatitude() {
        return mLatitude;
    }

    public void setLatitude(int latitude) {
        this.mLatitude = latitude;
    }

    public int getLongitude() {
        return mLongitude;
    }

    public void setLongitude(int longitude) {
        this.mLongitude = longitude;
    }

    public String getContent() {
        return mComment;
    }

    public void setContent(String content) {
        this.mComment = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImageBytes() {
        return mBitmapArray;
    }

    public void setImageBytes(byte[] bitmapArray) {
        this.mBitmapArray = bitmapArray;
    }

    public Listdata(Date date, String title, String place, int latitude, int longitude, String content, byte[] bytes) {
        mTitle = title;
        mDate = date;
        mPlace = place;
        mLatitude = latitude;
        mLongitude = longitude;
        mComment = content;
        mBitmapArray = bytes.clone();
    }
}