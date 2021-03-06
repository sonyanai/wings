package jp.techacademy.taison.yanai.wings;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by taiso on 2017/08/28.
 */

public class SentFragment extends Fragment {
    public static final String TAG = "SentFragment";
    Button receivedFolderButton;
    private ArrayList<FolderData> folderList = new ArrayList<FolderData>();  //FolderDataList
    private FolderListAdapter mAdapter;
    DatabaseReference dataBaseReference;
    DatabaseReference folderPathRef;
    GridView gridView;
    FirebaseUser user;
    String Uid;


    //Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_sent,container,false);
        // GridViewのインスタンスを生成
        gridView = (GridView)v.findViewById(R.id.SentFolderGridView);
        receivedFolderButton = (Button)v.findViewById(R.id.receivedFolderButton);
        return v;
    }

    //Viewが生成し終わった時に呼ばれるメソッド
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        Uid = user.getUid();

        //firebaseを使えるようにする
        dataBaseReference = FirebaseDatabase.getInstance().getReference();
        //MainActivityのメソッドを使うときはactivity.methodName()でいける


        //realtimeDatabase
        folderPathRef = dataBaseReference.child(Const.FolderPATH);

        //FolderDataが入ってるやつ
        folderList = new ArrayList<FolderData>();
        mAdapter = new FolderListAdapter(this.getActivity(), R.layout.grid_folder);



        //mEventListenerの設定と初期化
        ChildEventListener mEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                //try {
                HashMap map = (HashMap) dataSnapshot.getValue();
                final String mUid = (String) map.get("mUid");
                final String date = (String) map.get("date");
                final String name = (String) map.get("name");
                final String count = (String) map.get("count");
                final String cost = (String) map.get("cost");
                final String folderName = (String) map.get("folderName");
                final String imageString = (String) map.get("image");


                FolderData post = new FolderData(mUid, date, name, count, cost, folderName, imageString );
                folderList.add(post);
                mAdapter.setFolderDataArrayList(folderList);
                gridView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //ここでcountの変更を反映させる
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };


        folderPathRef.orderByChild("mUid").equalTo(Uid).addChildEventListener(mEventListener);


        //gridVieを押したときの処理
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Bundle bundle = new Bundle();
                bundle.putString("mUid", folderList.get(position).getUid());
                bundle.putString("folderName", folderList.get(position).getFolderName());
                WatchFragment fragmentWatch = new WatchFragment();
                fragmentWatch.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.container,fragmentWatch,WatchFragment.TAG)
                        .commit();

            }
        });

        view.findViewById(R.id.receivedFolderButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReceivedFragment fragmentReceived = new ReceivedFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container,fragmentReceived,ReceivedFragment.TAG)
                        .commit();
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
        }
    }
}
