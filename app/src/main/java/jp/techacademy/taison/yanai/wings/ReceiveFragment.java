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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
public class ReceiveFragment extends Fragment {
    //receiveFragmentを開いたときに出てくるgridViewのリスト
    private ArrayList<ImageData> gridList = new ArrayList<ImageData>();  //ImageDataList
    //private ArrayList<String> list = new ArrayList<String>();   //fileNameList
    private GridListAdapter mAdapter;
    public static String cord;
    DatabaseReference dataBaseReference;
    DatabaseReference filePathRef;
    DatabaseReference fileRef;
    DatabaseReference fileNameRef;
    DatabaseReference fileTotalRef;
    StorageReference storageRef;
    //firebasestrageをstrageという名前で使いますよ.これで Cloud Storage が使えるようになる
    FirebaseStorage storage;
    FirebaseUser user;
    Button searchButton;
    EditText cordEdit;
    GridView gridView;

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
        gridView = (GridView)v.findViewById(R.id.rankingGridView);
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



        if (user == null){
            user = FirebaseAuth.getInstance().getCurrentUser();
        }

        //realtimeDatabase
        filePathRef = dataBaseReference.child(Const.FilePATH);
        //fileNameRef = filePathRef.child(user.getUid());
        fileNameRef = filePathRef.child("8fnHRfgoMgP5TIE7lnqjs8vTP6Q2");
        //fileTotalRef = fileNameRef.child(SendFragment.folderName);
        //fileTotalRef = fileNameRef.child("gg").child("-KulzLzkoES30F72Wr47");
        fileTotalRef = fileNameRef.child("kjkjk");
        fileRef = fileTotalRef;
        //fileRef = dataBaseReference.child(Const.FilePATH).child(user.getUid()).child("dgf");

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
                    final String mUid = (String) map.get("mUid");
                    final String date = (String) map.get("date");
                    final String[] fileName = {(String) map.get("fileName")};
                    final String name = (String) map.get("name");
                    final String count = (String) map.get("count");
            /*String imageString = (String) map.get("image");
            byte[] bytes;
            if (imageString != null) {
                bytes = Base64.decode(imageString, Base64.DEFAULT);
            } else {
                bytes = new byte[0];
            }*/
            /*ImageData post = new ImageData(mUid,name, date, fileName, count );
            gridList.add(post);
            mAdapter.notifyDataSetChanged();
            mAdapter.setImageDataArrayList(gridList);
            gridView.setAdapter(mAdapter);*/
                    //2017.9.12 0:11を入力してok押すと2017.9.12 0:11フォルダ内の203102214.jpgが取れる
                    //203102214.jpgを変数にするために先に保持しておきたい
                    //user.getUid()はアップロードした人のやつ
                    final File localFile = File.createTempFile("image","jpg");
                    //localFile.toURI();でlocalFileのuriを取得できる
                    storageRef.child("8fnHRfgoMgP5TIE7lnqjs8vTP6Q2").child("kjkjk").child(fileName[0]).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        //storageRef.child(user.getUid()).child("kjkjk").child("203102214.jpg").getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Local temp file has been created

                            //プログレスバー読み込み終わるまで，終わらせる処理も

                            Log.d("aaaaa","suc");
                            //count += 1;
                            //fileRef.changeEventListener(mEventListener);
                            fileName[0] = localFile.toString();
                            ImageData post = new ImageData(mUid, date, fileName[0], name, count );
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
                            /*allow read, write: if request.auth != null;*/
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
        MainActivity.variable = "ログインに成功しました";
        activity.AlertDialog();

        //activity.notKeyboard();


        //ImageDataが入ってるやつ
        //gridList = new ArrayList<ImageData>();
        //mAdapter = new GridListAdapter(this.getActivity(), R.layout.grid_items);

        //mEventListenerの呼び出し
        //MainActivity.fileRef.addChildEventListener(mEventListener);
        fileRef.addChildEventListener(mEventListener);

        // Buttonのクリックした時の処理を書きます
        view.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cord = cordEdit.getText().toString();
                MainActivity activity = (MainActivity)getActivity();
                activity.download();
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
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            //ImageData imageData = new ImageData(user.getUid());
            // List<String> imgList にはファイルのパスを入れる
            //imageDataを作ってから
            //gridList.add(destPath);
        }
    }
}


