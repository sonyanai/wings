package jp.techacademy.taison.yanai.wings;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by taiso on 2017/08/27.
 */

//Fraggmentクラスを継承する
public class ReceiveFragment extends Fragment {

    private ArrayList<ImageData> gridList = new ArrayList<ImageData>();
    //private GridListAdapter mAdapter;
    public static String cord;
    Button searchButton;
    EditText cordEdit;
    GridView gridView;


   //Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_receive,container,false);
        MainActivity activity = (MainActivity)getActivity();
        //ReceiveActivityで引っ張ってくるUI
        searchButton = (Button)v.findViewById(R.id.searchButton);
        cordEdit = (EditText)v.findViewById(R.id.cordEdit);
        // GridViewのインスタンスを生成
        gridView = (GridView)v.findViewById(R.id.rankingGridView);

        return v;
    }

    //Viewが生成し終わった時に呼ばれるメソッド
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        MainActivity activity = (MainActivity)getActivity();
        activity.sAlertDialog();


        gridList.clear();
        GridListAdapter mAdapter = new GridListAdapter(this.getActivity(), R.layout.grid_items, gridList);
        //mAdapter.setListAdapter(gridList);
        // BaseAdapter を継承したGridAdapterのインスタンスを生成
        // 子要素のレイアウトファイル grid_items.xml を activity_main.xml に inflate するためにGridAdapterに引数として渡す
        //mAdapter = new GridListAdapter(this.getApplicationContext(), R.layout.grid_items, gridList);
        // gridViewにadapterをセット
        gridView.setAdapter(mAdapter);

        // /assets/image/以下に画像を入れています
        // それのパスを取り出す method
        //getImagePath();




        // Buttonのクリックした時の処理を書きます
        view.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cord = cordEdit.getText().toString();
                MainActivity activity = (MainActivity)getActivity();
                activity.download();


                //assetsに画像を保存する
                //assesフォルダに入っているaisha_3.jpgを表示させる
                try {
                    InputStream istream = getResources().getAssets().open("aisha_3.jpg");
                    Bitmap bitmap = BitmapFactory.decodeStream(istream);
                    //imageView.setImageBitmap(bitmap);//gridViewを使うはず
                } catch (IOException e) {
                    Log.d("Assets","Error");
                }

            }
        });
    }



    private void getImagePath() {
        AssetManager assetManager = getResources().getAssets();
        String[] fileList = null;
        InputStream input = null;
        FileOutputStream output = null;
        String destPath = null;

        try {
            // ファイルリストを作成する
            fileList = assetManager.list("image");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0; i< fileList.length ; i++){
            try{
                input = assetManager.open("image/" + fileList[i]);
                // 保存先のパス
                destPath = "/data/data/"+getActivity().getPackageName()+"/" + fileList[i];

                output=new FileOutputStream(destPath);

                // bufferの設定
                int DEFAULT_BUFFER_SIZE = 10240 * 4;
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                // assetsから読出して、内部メモリの/data/data/... に保存
                int n = 0;
                while (-1 != (n = input.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                output.close();
                input.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            //ImageData imageData = new ImageData(user.getUid());
            // List<String> imgList にはファイルのパスを入れる
            //imageDataを作ってから
            //gridList.add(destPath);

        }
    }

}
