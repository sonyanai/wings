package jp.techacademy.taison.yanai.wings;

import android.graphics.Bitmap;

/**
 * Created by taiso on 2017/09/12.
 */

public class ImageData {

    //mFile以外いらない

    private String mUid;
    private String mDate;
    private String mFileName;
    private String mName;
    private String mCount;

    public String getUid(){
        return mUid;
    }

    public String getDate(){
        return mDate;
    }

    public String getFileName(){
        return mFileName;
    }

    public String getmName(){
        return mName;
    }
    public String getCount(){
        return mCount;
    }

    public ImageData(String uid, String date, String fileName, String name,String count ) {
        mUid = uid;
        mDate = date;
        mFileName = fileName;
        mName = name;
        mCount = count;
    }

}
