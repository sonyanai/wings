package jp.techacademy.taison.yanai.wings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * Created by taiso on 2017/08/27.
 */

//Fraggmentクラスを継承する
public class ReceiveFragment extends Fragment {

    private EditText codeEdit;
    private Button searchButton;
    public static String code;


   //Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_receive,container,false);
        MainActivity activity = (MainActivity)getActivity();
        //ReceiveActivityで引っ張ってくるUI
        Button searchButton = (Button)v.findViewById(R.id.searchButton);
        EditText codeEdit = (EditText)v.findViewById(R.id.codeEdit);

        return v;
    }

    //Viewが生成し終わった時に呼ばれるメソッド
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);




        // Buttonのクリックした時の処理を書きます
        view.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    MainActivity activity = (MainActivity)getActivity();
                    activity.onSelfCheck();
                }else{
                    MainActivity activity = (MainActivity)getActivity();
                    activity.intentLogin();
                }*/

                //code = searchButton.getText().toString();

                /*if (指定されたcodeがあったら){
                    fragmentで書き換えてgridview(WatchFragment)を表示
                    // Fragmentを作成・初期化します
                    fragmentReceive = new ReceiveFragment();
                    // コードからFragmentを追加
                    // Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
                    // 新しく追加を行うのでaddを使用します
                    // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
                    transaction.replace(R.id.container, fragmentReceive);
                    // 最後にcommitを使用することで変更を反映します
                    transaction.commit();
                }*/

            }
        });
    }
}
