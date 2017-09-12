package jp.techacademy.taison.yanai.wings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by taiso on 2017/09/12.
 */

public class WatchFragment extends Fragment {
    //Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_watch,container,false);
        MainActivity activity = (MainActivity)getActivity();
        //activity.watchGridView = (GridView)v.findViewById(R.id.watchGridView);
        //activity.imgView = (ImageView)v.findViewById(R.id.imgView);


        return v;
    }

    //Viewが生成し終わった時に呼ばれるメソッド
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
/*

        // GridViewのインスタンスを生成
        //GridView gridview = (GridView) v.findViewById(R.id.watchGridView);
        // BaseAdapter を継承したGridAdapterのインスタンスを生成
        // 子要素のレイアウトファイル grid_items.xml を activity_main.xml に inflate するためにGridAdapterに引数として渡す
        GridAdapter adapter = new GridAdapter(this.getApplicationContext(), R.layout.grid_items, imgList);
        // gridViewにadapterをセット
        gridview.setAdapter(adapter);

        // /assets/image/以下に画像を入れています
        // それのパスを取り出す method
        getImagePath();


        // Buttonのクリックした時の処理を書きます
        view.findViewById(R.id.selectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    MainActivity activity = (MainActivity)getActivity();
                    activity.onSelfCheck();
                }else{
                    MainActivity activity = (MainActivity)getActivity();
                    activity.intentLogin();
                }
            }
        });
        view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity)getActivity();
                activity.onSend();
            }
        });*/
    }
}

