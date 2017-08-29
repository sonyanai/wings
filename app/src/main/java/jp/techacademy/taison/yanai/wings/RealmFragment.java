package jp.techacademy.taison.yanai.wings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by taiso on 2017/08/28.
 */

public class RealmFragment extends Fragment {

    private TextView mTextView;

    //Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        //Fragment_receive.xmlレイアウトをここでViewとして作成する
        return inflater.inflate(R.layout.fragment_realm,container,false);
    }

    //Viewが生成し終わった時に呼ばれるメソッド
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        // Buttonのクリックした時の処理を書きます
        /*view.findViewById(R.id.genreP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText(mTextView.getText() + "!");
            }
        });*/
    }
}