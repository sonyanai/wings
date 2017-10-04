package jp.techacademy.taison.yanai.wings;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by taiso on 2017/08/27.
 */
//Fraggmentクラスを継承する
public class ReceiveFragment extends Fragment {
    //receiveFragmentを開いたときに出てくるgridViewのリスト
    private ArrayList<FolderData> folderList = new ArrayList<FolderData>();  //FolderDataList
    //private ArrayList<String> list = new ArrayList<String>();   //fileNameList
    private FolderListAdapter mAdapter;
    DatabaseReference dataBaseReference;
    DatabaseReference filePathRef;
    DatabaseReference fileNameRef;
    DatabaseReference fileRef;
    DatabaseReference fileTotalRef;

    DatabaseReference folderPathRef;
    StorageReference storageRef;
    //firebasestrageをstrageという名前で使いますよ.これで Cloud Storage が使えるようになる
    FirebaseStorage storage;
    FirebaseUser user;
    GridView gridView;
    Button searchButton;
    EditText cordEdit;
    public static String cord;
    private FirebaseAuth mAuth;

    static final String TAG = "FragmentTest";

    /***
     * Activityに関連付けされた際に一度だけ呼び出される
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    /***
     * Fragmentの初期化処理を行う
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    /***
     * 親となるActivityの「onCreate」の終了を知らせる
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    /***
     * Activityの「onStart」に基づき開始される
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
/*
        //realtimeDatabase
        filePathRef = dataBaseReference.child(Const.FilePATH);
        //fileNameRef = filePathRef.child(user.getUid());
        fileNameRef = filePathRef.child("8fnHRfgoMgP5TIE7lnqjs8vTP6Q2");
        //fileTotalRef = fileNameRef.child(SendFragment.folderName);
        //fileTotalRef = fileNameRef.child("gg").child("-KulzLzkoES30F72Wr47");
        fileTotalRef = fileNameRef.child("kjkjk");
        fileRef = fileTotalRef;
        //fileRef = dataBaseReference.child(Const.FilePATH).child(user.getUid()).child("dgf");
*/
        folderPathRef = dataBaseReference.child(Const.FolderPATH);



        //firebaseStorage
        //storageRef = storage.getReference();

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

        folderPathRef.addChildEventListener(mEventListener);

    }

    /***
     * Activityの「onResume」に基づき開始される
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    /***
     * Activityが「onPause」になった場合や、Fragmentが変更更新されて操作を受け付けなくなった場合に呼び出される
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    /***
     * フォアグラウンドでなくなった場合に呼び出される
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    /***
     * Fragmentの内部のViewリソースの整理を行う
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    /***
     * Fragmentが破棄される時、最後に呼び出される
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /***
     * Activityの関連付けから外された時に呼び出される
     */
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

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
        gridView = (GridView)v.findViewById(R.id.folderGridView);
        return v;
    }
    //Viewが生成し終わった時に呼ばれるメソッド
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        //firebaseを使えるようにする
        dataBaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        //MainActivityのメソッドを使うときはactivity.methodName()でいける
        MainActivity activity = (MainActivity)getActivity();



        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            //FirebaseUser currentUser = mAuth.getCurrentUser();
            //匿名認証のやつ
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInAnonymously()
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("sign", "signInAnonymously:success");
                                user = mAuth.getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                if (user == null){
                                    Log.w("sign", "signInAnonymously:failure", task.getException());
                                    MainActivity activity = (MainActivity)getActivity();
                                    activity.variable = "ログインに失敗しました1";
                                    activity.AlertDialog();
                                }

                            }

                            // ...
                        }
                    });
        }



        //refの調整！;
        //あと，送るところきちんと設定する;










        //dialogの表示
        MainActivity.variable = "ログインに成功しました";
        activity.AlertDialog();

        //activity.notKeyboard();



        //mEventListenerの呼び出し
        //fileRef.addChildEventListener(mEventListener);
        //fileNameRef.addChildEventListener(mEventListener);
        //folderPathRef.addChildEventListener(mEventListener);

        // Buttonのクリックした時の処理を書きます
        view.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cord = cordEdit.getText().toString();
                MainActivity activity = (MainActivity)getActivity();
                //activity.download();
                activity.intentWatchFragment();

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
        }
    }
}
