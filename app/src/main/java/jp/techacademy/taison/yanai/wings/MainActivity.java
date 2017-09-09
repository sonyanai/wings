package jp.techacademy.taison.yanai.wings;

import android.Manifest;
//import android.Manifest;これは手動で追加する
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
//Fragmentのimportはこれ！！！
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final int CHOOSER_REQUEST_CODE = 100;
    private ArrayList<Uri> mFileArrayList;
    //メンバ変数として定義
    public ReceiveFragment fragmentReceive;
    public SendFragment fragmentSend;
    public RealmFragment fragmentRealm;
    public ProfileFragment fragmentProfile;
    public ImageView imgView;
    //firebasestrageをstrageという名前で使いますよ.これで Cloud Storage が使えるようになる
    FirebaseStorage storage;
    //strageの中にimgRefという領域を作りますよ
    StorageReference storageRef;
    StorageReference imgRef;
    Uri uri;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.item_receive:
                    // Fragmentを作成・初期化します
                    fragmentReceive = new ReceiveFragment();
                    // コードからFragmentを追加
                    // Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
                    // 新しく追加を行うのでaddを使用します
                    // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
                    transaction.replace(R.id.container, fragmentReceive);
                    // 最後にcommitを使用することで変更を反映します
                    transaction.commit();
                    return true;


                case R.id.item_send:
                    // Fragmentを作成・初期化
                    fragmentSend = new SendFragment();
                    // 他にも、メソッドにはreplace removeがあります
                    // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
                    transaction.replace(R.id.container, fragmentSend);
                    // 最後にcommitを使用することで変更を反映します
                    transaction.commit();
                    return true;

                case R.id.item_realm:
                    fragmentRealm = new RealmFragment();
                    transaction.replace(R.id.container, fragmentRealm);
                    transaction.commit();
                    return true;

                case R.id.item_profile:
                    fragmentProfile = new ProfileFragment();
                    transaction.replace(R.id.container, fragmentProfile);
                    transaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFileArrayList = new ArrayList<Uri>();


        //Fragmentで最初の画面の設定をする
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Fragmentを作成します
        ReceiveFragment fragmentReceive = new ReceiveFragment();
        // コードからFragmentを追加
        // Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
        // 新しく追加を行うのでaddを使用します
        // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
        transaction.add(R.id.container, fragmentReceive);
        // 最後にcommitを使用することで変更を反映します
        transaction.commit();


        //BottomNavigationViewの定義して設置する
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        //リスナーのセット
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    public void onSelfCheck() {
        // パーミッションの許可状態を確認する
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                showChooser();
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);

                return;
            }
        } else {
            showChooser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("ANDROID", "許可された");
                } else {
                    Log.d("ANDROID", "許可されなかった");
                }
                break;
            default:
                break;
        }
    }

    private void showChooser() {
        // ギャラリーから選択するIntent
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        //何を選択できるようにするか
        galleryIntent.setType("image/*,video/*");
        //galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        //複数選択可能
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //選択した動画像を読み込む
        //galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setAction(Intent.ACTION_PICK);
        //galleryに飛ばして選択させる
        startActivityForResult(Intent.createChooser(galleryIntent,"画像/動画を選択"), CHOOSER_REQUEST_CODE);
    }



    //選択した結果を受け取る
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            //選択されたのがnullでない場合
            if (data.getData() != null) {
                //単一選択の場合
                /*if (data ==1){
                    //単一選択の場合
                }else{
                    //複数選択の場合
                }*/

/*
                // 単一選択
                //Exception系の例外が発生しそうな命令を呼び出す場合
                // try-catch文で例外が発生した時の代替処理を用意しておかないとコンパイルエラーになる
                try {
                    //通常なら，エラーがなければ実行する処理

                    //
                    uri = data.getData();

                    //uriをarraylistに追加
                    //mFileArrayList = new ArrayList<Uri>();
                    mFileArrayList.add(uri);

                    //
                    InputStream in = getContentResolver().openInputStream(uri);
                    //
                    Bitmap img = BitmapFactory.decodeStream(in);
                    //ファイルを開いたら閉じなければならない(書き込むときはtry-catch}のあとに書く)
                    in.close();

                    // 選択した画像を表示
                    imgView.setImageBitmap(img);
                    //指定されたパス名のファイルが存在しない時や存在していても何らかの理由でアクセスできない場合
                    //例えば読み込み専用のファイルを書き込みのために開こうとした場合などにそれらのコンストラクタによって投げられる
                } catch (FileNotFoundException e) {
                    //その時の処理
                    e.printStackTrace();
                    //ファイルの読み書きなどの入出力ができない時のエラー
                } catch (IOException e) {
                    //その時の処理
                    e.printStackTrace();
                }
            } else {


                // 複数選択(EXTRA_ALLOW_MULTIPLE)
                //getClipData()で結果が得る
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    // i に対するビューを設定
                    ImageView targetView = null;
                    switch (i) {
                        case 0:
                            targetView = imgView;
                            break;
                        default:
                            break;
                    }
                    // ビューに画像を設定
                    if (targetView != null) {
                        //エラー出てきたときのためのやつ
                        try {
                            //エラーが出なかった時にしたい処理
                            ClipData.Item item = clipData.getItemAt(i);
                            uri = item.getUri();
                            //mFileArrayList = new ArrayList<Uri>();
                            //uriをarraylistに追加
                            mFileArrayList.add(uri);
                            InputStream in = getContentResolver().openInputStream(uri);
                            Bitmap img = BitmapFactory.decodeStream(in);
                            //ファイルを開いたら閉じなければならない(書き込むときはtry-catch}のあとに書く)
                            in.close();
                            // 選択した画像[i]を表示
                            targetView.setImageBitmap(img);
                            //エラー処理
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            //エラー処理
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }*/
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++){
                    try {
                        //エラーが出なかった時にしたい処理
                        ClipData.Item item = clipData.getItemAt(i);
                        // i に対するビューを設定
                        /*ImageView targetView = null;
                        switch (i) {
                            case 0:
                                targetView = imgView;
                                break;
                            default:
                                break;
                        }*/
                        uri = item.getUri();
                        //mFileArrayList = new ArrayList<Uri>();
                        //uriをarraylistに追加
                        mFileArrayList.add(uri);
                        InputStream in = getContentResolver().openInputStream(uri);
                        Bitmap img = BitmapFactory.decodeStream(in);
                        //ファイルを開いたら閉じなければならない(書き込むときはtry-catch}のあとに書く)
                        in.close();
                        // 選択した画像[i]を表示
                        //targetView.setImageBitmap(img);
                        //エラー処理
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //エラー処理
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
    public void onSend(){
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        Uri file = uri;

        //arraylistでループ処理しながらファイルのアップデート269-288ループ


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        for(Uri mFile : mFileArrayList){
            StorageReference imgRef = storageRef.child(user.getUid()).child(file.getLastPathSegment()+ ".jpg");
            UploadTask uploadTask = imgRef.putFile(mFile);
            //UploadTask uploadTask = imgRef.putFile(file);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("aaa","failed");
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Log.d("aaa","success");
                }
            });
        }
        //arraylistをクリアーさせる
        mFileArrayList.clear();
    }
    public void intentLogin(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
}