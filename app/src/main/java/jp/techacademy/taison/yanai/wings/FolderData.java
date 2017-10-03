package jp.techacademy.taison.yanai.wings;

/**
 * Created by taiso on 2017/09/26.
 */

public class FolderData {

    //何が必要？


    private String mUid;
    private String mDate;
    private String mName;
    private String mCount;
    private String mCost;
    private String mFolderName;
    private String mImage;


        public String getUid(){
            return mUid;
        }

        public String getDate(){
            return mDate;
        }

        public String getmName(){
            return mName;
        }

        public String getCount(){
            return mCount;
        }

        public String getCost(){
            return mCost;
        }

        public String getFileName(){
            return mFolderName;
        }

        public String getImageBytes(){
            return mImage;
        }

        public FolderData(String uid, String date, String name, String count, String cost, String folderName, String image ) {
            mUid = uid;
            mDate = date;
            mName = name;
            mCount = count;
            mCost = cost;
            mFolderName = folderName;
            mImage = image;
        }
}
