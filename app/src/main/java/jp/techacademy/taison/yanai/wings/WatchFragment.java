package jp.techacademy.taison.yanai.wings;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by taiso on 2017/08/27.
 */
//Fraggmentクラスを継承する
public class WatchFragment extends Fragment {
    public static final String TAG = "WatchFragment";
    //receiveFragmentを開いたときに出てくるgridViewのリスト
    private ArrayList<ImageData> gridList = new ArrayList<ImageData>();  //ImageDataList
    private GridListAdapter mAdapter;
    DatabaseReference dataBaseReference;
    DatabaseReference filePathRef;
    DatabaseReference fileRef;
    DatabaseReference fileNameRef;
    DatabaseReference fileTotalRef;
    StorageReference storageRef;
    //firebasestrageをstrageという名前で使いますよ.これで Cloud Storage が使えるようになる
    FirebaseStorage storage;
    FirebaseUser user;
    GridView gridView;
    ImageView visivility;
    public static String cord;
    String intentUid;
    String intentFolderName;

    //Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_watch,container,false);
        MainActivity activity = (MainActivity)getActivity();
        // GridViewのインスタンスを生成
        gridView = (GridView)v.findViewById(R.id.rankingGridView);
        visivility = (ImageView)v.findViewById(R.id.visivility_imageView);
        return v;
    }
    //Viewが生成し終わった時に呼ばれるメソッド
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);


        visivility.setImageAlpha(190);
        visivility.setImageResource(R.drawable.mosaic);

        Bundle bundle = getArguments();
        intentUid = bundle.getString("mUid");
        intentFolderName = bundle.getString("folderName");


        //firebaseを使えるようにする
        dataBaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        //MainActivityのメソッドを使うときはactivity.methodName()でいける
        MainActivity activity = (MainActivity)getActivity();



        if (user == null){
            user = FirebaseAuth.getInstance().getCurrentUser();
        }

        //realtimeDatabase
        filePathRef = dataBaseReference.child(Const.FilePATH);
        fileNameRef = filePathRef.child(intentUid);
        fileTotalRef = fileNameRef.child(intentFolderName);
        fileRef = fileTotalRef;


        //firebaseStorage
        storageRef = storage.getReference();

        //ImageDataが入ってるやつ
        gridList = new ArrayList<ImageData>();
        mAdapter = new GridListAdapter(this.getActivity(), R.layout.grid_items);





        //mEventListenerの設定と初期化
        ChildEventListener mEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                try {
                    HashMap map = (HashMap) dataSnapshot.getValue();
                    final String[] fileName = {(String) map.get("fileName")};

                    //2017.9.12 0:11を入力してok押すと2017.9.12 0:11フォルダ内の203102214.jpgが取れる
                    //203102214.jpgを変数にするために先に保持しておきたい
                    //user.getUid()はアップロードした人のやつ
                    final File localFile = File.createTempFile("image","jpg");
                    //localFile.toURI();でlocalFileのuriを取得できる
                        storageRef.child(intentUid).child(intentFolderName).child(fileName[0]).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Local temp file has been created

                            //プログレスバー読み込み終わるまで，終わらせる処理も


                            Log.d("aaaaa","suc");
                            //count += 1;
                            //fileRef.changeEventListener(mEventListener);

                            //postの第一引数
                            fileName[0] = localFile.toString();

                            //postの第二引数
                            //fileNameをbyte[]に変換する
                            Bitmap bmp = BitmapFactory.decodeFile(fileName[0]);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] bytes = baos.toByteArray();

                            ImageData post = new ImageData(fileName[0],bytes);
                            gridList.add(post);
                            mAdapter.setImageDataArrayList(gridList);
                            gridView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Log.d("aaaa","failure");
                        }
                    });
                } catch( IOException e ) {
                }
                // /assets/image/以下に画像を入れています
                // それのパスを取り出す method
                getImagePath();
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







        //dialogの表示
        MainActivity.variable = "WatchFragmentにログインしました";
        activity.AlertDialog();

        //activity.notKeyboard();

        //mEventListenerの呼び出し
        fileRef.addChildEventListener(mEventListener);



        //gridVieを押したときの処理
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Bundle bundle = new Bundle();
                bundle.putByteArray("bytes", gridList.get(position).getImageBytes());
                FilterFragment fragmentFilter = new FilterFragment();
                fragmentFilter.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.container,fragmentFilter,FilterFragment.TAG)
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


