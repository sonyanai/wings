package jp.techacademy.taison.yanai.wings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by taiso on 2017/09/26.
 */

class FolderViewHolder {
    ImageView imageView;
    TextView CostTextView;
    TextView CountTextView;

}

public class FolderListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int layoutId;
    private ArrayList<FolderData> folderList = new ArrayList<FolderData>();

    public FolderListAdapter(Context context,  int layoutId) {
        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //ファイル名->画像にする！



        //String mFilepath = folderList.get(position).getFileName();
        String mCount = folderList.get(position).getCount();
        String mCost = folderList.get(position).getCost();

        FolderViewHolder holder;
        if (convertView == null) {
            // main.xml の <GridView .../> に grid_items.xml を inflate して convertView とする
            convertView = inflater.inflate(layoutId, parent, false);
            // FolderViewHolder を生成
            holder = new FolderViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
            holder.CostTextView = (TextView) convertView.findViewById(R.id.CostTextView);
            holder.CountTextView = (TextView) convertView.findViewById(R.id.CountTextView);
            convertView.setTag(holder);
        }
        else {
            holder = (FolderViewHolder) convertView.getTag();
        }

        ///Bitmap bmp = BitmapFactory.decodeFile(mFilepath);
        //holder.imageView.setImageBitmap(bmp);
        //costととcountをholderにセット
        if(mCount!=null){
            holder.CountTextView.setText(mCount);
        }
        if(mCost!=null){
            holder.CostTextView.setText(mCost);
        }


        return convertView;
    }

    @Override
    public int getCount() {
        // List<String> imgList の全要素数を返す
        return folderList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void setFolderDataArrayList(ArrayList<FolderData> folderList){
        this.folderList = folderList;
    }
}
