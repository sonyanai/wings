package jp.techacademy.taison.yanai.wings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//fragmentのimportはこれ！！！
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by taiso on 2017/08/28.
 */

public class SendFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 100;


    //Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //Fragment_receive.xmlレイアウトをここでViewとして作成する
        return inflater.inflate(R.layout.fragment_send, container, false);
    }

    //Viewが生成し終わった時に呼ばれるメソッド
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Buttonのクリックした時の処理を書きます
        view.findViewById(R.id.selectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity)getActivity();
                activity.onSelfCheck();

            }
        });
    }
}