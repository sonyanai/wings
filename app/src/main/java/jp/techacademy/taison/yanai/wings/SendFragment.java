package jp.techacademy.taison.yanai.wings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
//fragmentのimportはこれ！！！
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by taison on 2017/08/28.
 */

public class SendFragment extends Fragment {


    //Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_send,container,false);
        MainActivity activity = (MainActivity)getActivity();
        //activity.imgView = (ImageView)v.findViewById(R.id.imgView);


        return v;
    }

    //Viewが生成し終わった時に呼ばれるメソッド
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
        });
    }
}