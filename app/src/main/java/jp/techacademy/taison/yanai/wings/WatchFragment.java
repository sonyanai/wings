package jp.techacademy.taison.yanai.wings;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.Map;

/**
 * Created by taiso on 2017/08/27.
 */
//Fraggmentクラスを継承する
public class WatchFragment extends Fragment {
    public static final String TAG = "WatchFragment";
    //receiveFragmentを開いたときに出てくるgridViewのリスト
    private ArrayList<ImageData> gridList = new ArrayList<ImageData>();  //ImageDataList
    private ArrayList<String> boughtFolderList = new ArrayList<String>();
    private GridListAdapter mAdapter;
    DatabaseReference dataBaseReference;
    DatabaseReference filePathRef;
    DatabaseReference favoritePathRef;
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
    Button buyButton;
    DatabaseReference folderPathRef;
    private ArrayList<KeyFolderData> KeyFolderList = new ArrayList<KeyFolderData>();
    KeyFolderData post;


    //Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_watch,container,false);
        MainActivity activity = (MainActivity)getActivity();
        // GridViewのインスタンスを生成
        gridView = (GridView)v.findViewById(R.id.rankingGridView);
        visivility = (ImageView)v.findViewById(R.id.visivility_imageView);
        buyButton = (Button)v.findViewById(R.id.buyButton);
        return v;
    }
    //Viewが生成し終わった時に呼ばれるメソッド
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);


        //フィルター画像
        visivility.setImageAlpha(190);
        visivility.setImageResource(R.drawable.mosaic);

        Bundle bundle = getArguments();
        intentUid = bundle.getString("mUid");
        intentFolderName = bundle.getString("folderName");


        //firebaseを使えるようにする
        dataBaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        //MainActivityのメソッドを使うときはactivity.methodName()でいける
        final MainActivity activity = (MainActivity)getActivity();



        if (user == null){
            user = FirebaseAuth.getInstance().getCurrentUser();
        }

        //realtimeDatabase
        filePathRef = dataBaseReference.child(Const.FilePATH);
        fileNameRef = filePathRef.child(intentUid);
        fileTotalRef = fileNameRef.child(intentFolderName);
        fileRef = fileTotalRef;
        folderPathRef = dataBaseReference.child(Const.FolderPATH);
        favoritePathRef = dataBaseReference.child(Const.UsersPATH).child(user.getUid());


        //firebaseStorage
        storageRef = storage.getReference();

        //ImageDataが入ってるやつ
        gridList = new ArrayList<ImageData>();
        mAdapter = new GridListAdapter(this.getActivity(), R.layout.grid_items);
        //FolderDataが入ってるやつ
        KeyFolderList = new ArrayList<KeyFolderData>();


        //fEventListenerの設定と初期化
        ChildEventListener fEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {

                HashMap map = (HashMap) dataSnapshot.getValue();
                final String mUid = (String) map.get("mUid");
                final String date = (String) map.get("date");
                final String name = (String) map.get("name");
                final String count = (String) map.get("count");
                final String cost = (String) map.get("cost");
                final String folderName = (String) map.get("folderName");
                final String imageString = (String) map.get("image");
                String folderKey = dataSnapshot.getKey();


                post = new KeyFolderData(mUid, date, name, count, cost, folderName, imageString, folderKey);
                //すべての投稿とkeyが入っているリスト
                KeyFolderList.add(post);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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

        //fEventListenerの呼び出し
        folderPathRef.addChildEventListener(fEventListener);



        ChildEventListener bEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {

                boughtFolderList.clear();

                HashMap map = (HashMap) dataSnapshot.getValue();


                //280行ランダムのキーだからどうやってfolderNameを取得すればいいん？
                final String folderName = (String) map.get("aaa");

                if(folderName.length()>7){
                    String boughtPost = new String( folderName );
                    boughtFolderList.add(boughtPost);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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

        favoritePathRef.child("boughtFolderName").addChildEventListener(bEventListener);




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

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                favoritePathRef.child("boughtFolderName").addChildEventListener(bEventListener);



                //countに+1する処理
                //購入ボタンを押したフォルダを見つけてcountに+1,userの領域にfoldername追加
                for(KeyFolderData compareFolderName : KeyFolderList){
                    if(compareFolderName.getFolderName().equals(intentFolderName)) {
                        /*if(boughtFolderList.contains(compareFolderName.getFolderName())){
                            MainActivity.variable="in";
                            activity.AlertDialog();
                        }else{
                            MainActivity.variable="no";
                            activity.AlertDialog();;
                        }*/
                        //勝った人の領域にそのfolderNameを追加する
                        //Firebaseにデータ作成、データのkey取得
                        String key = favoritePathRef.child("boughtFolder").push().getKey();
                        //送信するデータを指定
                        String value = intentFolderName;
                        //postvalueをHashMapで初期化,String型からMap型に変換
                        Map<String, Object> postValues = new HashMap<>();
                        //key,valueの設定
                        postValues.put("gotFolder",value);
                        // 送信用Map初期化
                        Map<String, Object> childUpdates = new HashMap<>();
                        // 送信用Mapにデータを設定
                        childUpdates.put(key, postValues);
                        // Firebaseに送信用Mapを渡し、更新を依頼
                        favoritePathRef.child("boughtFolder").updateChildren(childUpdates);


                        //countを取得して+1
                        String oldCount = compareFolderName.getCount();
                        int sameOldCount =Integer.parseInt(oldCount);
                        int totalCount;
                        totalCount = sameOldCount + 1;
                        String newCount = String.valueOf(totalCount);

                        //keyを取得
                        String gotFolderKey = compareFolderName.getKey();

                        String cost = compareFolderName.getCost();
                        String date = compareFolderName.getDate();
                        String folderName = compareFolderName.getFolderName();
                        String imageString = compareFolderName.getImageBytes();
                        String mUid = compareFolderName.getUid();
                        String name = compareFolderName.getName();

                        //countの変更
                        Map<String, String> data = new HashMap<String, String>();
                        //一旦すべてのデータが消えてここでputしたやつしか保存されないからすべて保存しなおす
                        data.put("cost", cost);
                        data.put("count", newCount);
                        data.put("date", date);
                        data.put("folderName", folderName);
                        data.put("image", imageString);
                        data.put("mUid", mUid);
                        data.put("name", name);
                        folderPathRef.child(gotFolderKey).setValue(data);
                    }
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


