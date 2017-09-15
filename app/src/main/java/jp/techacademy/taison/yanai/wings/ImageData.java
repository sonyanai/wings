package jp.techacademy.taison.yanai.wings;

import android.graphics.Bitmap;

/**
 * Created by taiso on 2017/09/12.
 */

public class ImageData {
    private String mUid;
    private String mDate;
    private String mFileName;
    //private Bitmap mImageView;
    private byte[] mBitmapArray;
    //private int mCount;

    public String getUid(){
        return mUid;
    }

    public String getDate(){
        return mDate;
    }

    public String getFileName(){
        return mFileName;
    }

    /*public Bitmap getImageView(){
        return mImageView;
    }*/

    public byte[] getImageBytes(){
        return mBitmapArray;
    }
    /*public int getCount(){
        return mCount;
    }*/

    public ImageData(String uid, String date, String fileName, byte[] imageBytes) {
        mUid = uid;
        mDate = date;
        mFileName = fileName;
        //mImageView = imageView;
        //mBitmapArray = imageBytes;
        //mCount = count;
    }

}
