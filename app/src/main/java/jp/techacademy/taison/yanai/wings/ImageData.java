package jp.techacademy.taison.yanai.wings;


/**
 * Created by taiso on 2017/09/12.
 */

public class ImageData {

    private String mFileName;
    private byte[] mBitmapArray;


    public String getFileName(){
        return mFileName;
    }

    public byte[] getImageBytes() {
        return mBitmapArray;
    }

    public ImageData( String fileName , byte[] bytes) {
        mFileName = fileName;
        mBitmapArray = bytes.clone();
    }
}
