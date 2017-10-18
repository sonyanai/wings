package jp.techacademy.taison.yanai.wings;

/**
 * Created by taiso on 2017/09/26.
 */

public class KeyFolderData {

    private String mUid;
    private String mDate;
    private String mName;
    private String mCount;
    private String mCost;
    private String mFolderName;
    private String mImage;
    private String mKey;


    public String getUid(){
        return mUid;
    }

    public String getDate(){
        return mDate;
    }

    public String getName(){
        return mName;
    }

    public String getCount(){
        return mCount;
    }

    public String getCost(){
        return mCost;
    }

    public String getFolderName(){
        return mFolderName;
    }

    public String getImageBytes(){
        return mImage;
    }
    public String getKey(){
        return mKey;
    }

    public KeyFolderData(String uid, String date, String name, String count, String cost, String folderName, String image, String key ) {
        mUid = uid;
        mDate = date;
        mName = name;
        mCount = count;
        mCost = cost;
        mFolderName = folderName;
        mImage = image;
        mKey = key;
    }
}
