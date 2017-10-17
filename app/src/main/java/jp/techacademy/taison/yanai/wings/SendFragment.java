package jp.techacademy.taison.yanai.wings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
//fragmentのimportはこれ！！！
import android.util.Log;
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
        folderImageView.setImageResource(R.drawable.scenery);



        // Buttonのクリックした時の処理を書きます
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.totalSize = 0;
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
                    activity.onSelfCheck();
                }
            }
        });

        //画像の有無判断はMainActivityでやる
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity)getActivity();

                //サイズ制限6MBくらい？
                if(MainActivity.totalSize > 10){
                    if(MainActivity.totalSize < 6000000){
                        MainActivity.variable = "サイズ内ok";
                        activity.AlertDialog();
                        folderName = folderNameEditText.getText().toString();
                        cost = costEditText.getText().toString();
                        int costRange = Integer.parseInt(cost);

                        //folderNameとcostの桁数制限
                        if( folderName.length() == 8  && cost.length() != 0) {
                            if(100 <= costRange && costRange <= 5000){
                                activity.onSend();
                                Log.d("ssss","yes");
                            }else{
                                MainActivity.variable = "100<cost<5000";
                                activity.AlertDialog();
                                Log.d("ssss","cost");
                            }
                        }else{
                            MainActivity.variable = "input cost and password is 8 characters";
                            activity.AlertDialog();
                            Log.d("ssss","no");
                        }
                        folderNameEditText.getEditableText().clear();
                        costEditText.getEditableText().clear();
                    }else{
                        MainActivity.variable = "サイズオーバー";
                        activity.AlertDialog();
                    }
                }else{
                    MainActivity.variable = "select contents";
                    activity.AlertDialog();
                }
            }
        });
    }
}