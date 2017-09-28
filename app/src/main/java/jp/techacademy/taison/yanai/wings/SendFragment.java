package jp.techacademy.taison.yanai.wings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
//fragmentのimportはこれ！！！
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by taison on 2017/08/28.
 */

public class SendFragment extends Fragment {

    public static String folderName;
    public static String cost;
    EditText folderNameEditText;
    EditText costEditText;
    public static ImageView folderImageView;
    Button selectButton;
    Button sendButton;
    public static FirebaseUser user;


    //Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_send,container,false);
        MainActivity activity = (MainActivity)getActivity();

        folderNameEditText = (EditText)v.findViewById(R.id.folderName_EditText);
        costEditText = (EditText)v.findViewById(R.id.cost_EditText);
        folderImageView = (ImageView)v.findViewById(R.id.folderImageView);
        selectButton = (Button)v.findViewById(R.id.selectButton);
        sendButton = (Button)v.findViewById(R.id.sendButton);



        return v;
    }

    //Viewが生成し終わった時に呼ばれるメソッド
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Buttonのクリックした時の処理を書きます
        view.findViewById(R.id.selectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                /*if(user != null){
                    //mAuthのときはログイン
                    if(user != 匿名ユーザーmAuth){
                        MainActivity activity = (MainActivity)getActivity();
                        activity.intentLogin();
                    }else{
                        MainActivity activity = (MainActivity)getActivity();
                        activity.onSelfCheck();
                    }
                }else{
                    MainActivity activity = (MainActivity)getActivity();
                    activity.intentLogin();
                }*/
                if(user != null){
                    //mAuthのときはログイン
                    MainActivity activity = (MainActivity)getActivity();
                    //activity.intentLogin();
                    activity.onSelfCheck();
                }else {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.intentLogin();
                    //ひとまず9.28
                    //activity.onSelfCheck();
                }
            }
        });
        view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folderName = folderNameEditText.getText().toString();
                cost = costEditText.getText().toString();
                MainActivity activity = (MainActivity)getActivity();
                activity.onSend();
            }
        });
    }
}