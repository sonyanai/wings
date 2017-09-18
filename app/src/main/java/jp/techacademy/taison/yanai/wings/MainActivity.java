package jp.techacademy.taison.yanai.wings;

import android.Manifest;
//import android.Manifest;これは手動で追加する
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
//Fragmentのimportはこれ！！！
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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
    public static String variable;
    DatabaseReference dataBaseReference;
    //public static DatabaseReference fileRef;
    DatabaseReference fileRef;
    FirebaseUser user;
    //firebasestrageをstrageという名前で使いますよ.これで Cloud Storage が使えるようになる
    FirebaseStorage storage;
    //strageの中にimgRefという領域を作りますよ
    StorageReference storageRef;
    //private long fileSize;
    //private long totalSize;
    private FirebaseAuth mAuth;
    Uri uri;
    int count = 0;

    StorageReference imgRef;

    final Calendar calendar = Calendar.getInstance();

    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    final int hour = calendar.get(Calendar.HOUR_OF_DAY);
    final int minute = calendar.get(Calendar.MINUTE);





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

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        //realtimeDatabaseにファイル名を保存する
        dataBaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        //匿名認証のやつ
        mAuth = FirebaseAuth.getInstance();

        mFileArrayList = new ArrayList<Uri>();
        variable = "a";






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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("sign", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            if (user == null){
                                Log.w("sign", "signInAnonymously:failure", task.getException());
                                variable = "ログインに失敗しました1";
                                AlertDialog();
                            }

                        }

                        // ...
                    }
                });
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



                //ここでサイズを取得したい





                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++){
                    try {
                        //エラーが出なかった時にしたい処理
                        ClipData.Item item = clipData.getItemAt(i);

                        uri = item.getUri();
                        //mFileArrayList = new ArrayList<Uri>();
                        //uriをarraylistに追加
                        mFileArrayList.add(uri);
                        //totalSize += fileSize;


                        InputStream in = getContentResolver().openInputStream(uri);
                        //Bitmap img = BitmapFactory.decodeStream(in);
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
        //storage = FirebaseStorage.getInstance();
        //storageRef = storage.getReference();


        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            intentLogin();
        }

        //folderがnullの時は何もしない
        if(SendFragment.folderName != null){
            //fileRef領域を作ってファイル名，日付け，カウントを管理する
            fileRef = dataBaseReference.child(Const.FilePATH).child(user.getUid()).child(SendFragment.folderName);

            for(Uri mUri : mFileArrayList){
                //user.getUid()).child(year +"."+ (month + 1) +"."+ day + " " + hour + ":" + minuteと
                //紐づけるパスの変数を同じにする
                imgRef = storageRef.child(user.getUid()).child(SendFragment.folderName).child(mUri.getLastPathSegment()+ ".jpg");
                UploadTask uploadTask = imgRef.putFile(mUri);


                //realtimeDatabaseに送るよー
                Map<String, String> data = new HashMap<String, String>();

                //送るデータ各種
                //日時
                String dateString = year + "/" + String.format("%02d",(month + 1)) + "/" + String.format("%02d", day);
                String timeString = String.format("%02d", hour) + ":" + String.format("%02d", minute);
                String date = dateString+timeString;

                //名前
                // Preferenceから名前を取る
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                String name = sp.getString(Const.NameKEY, "");

                //Uid
                String mUid =  user.getUid();

                //ファイル名を取得したい
                String aaa = mUri.getPath();
                String bbb = new File(aaa).getName();
                String fileName =bbb+".jpg";







                data.put("date", date);
                data.put("name",name);
                data.put("mUid", mUid);
                data.put("count", String.valueOf(count));
                data.put("fileName", fileName);

                fileRef.push().setValue(data);




                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        variable = "送信に失敗しました";
                        AlertDialog();
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        variable = "送信に成功しました";
                        AlertDialog();
                    }
                });
            }

            //arraylistをクリアーさせる
            mFileArrayList.clear();
        }

    }
    public void intentLogin(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void AlertDialog() {
        // AlertDialog.Builderクラスを使ってAlertDialogの準備をする
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("");
        //alertDialogBuilder.setMessage("送信に成功しました");
        alertDialogBuilder.setMessage(variable);

        // 肯定ボタンに表示される文字列、押したときのリスナーを設定する
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("UI_PARTS", "肯定ボタン");
                    }
                });

        // AlertDialogを作成して表示する
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    public void download(){
        try {

            //2017.9.12 0:11を入力してok押すと2017.9.12 0:11フォルダ内の203102214.jpgが取れる
            //203102214.jpgを変数にするために先に保持しておきたい
            //user.getUid()はアップロードした人のやつ

            File localFile = File.createTempFile("image","jpg");
            //localFile.toURI();でlocalFileのuriを取得できる
            storageRef.child(user.getUid()).child(ReceiveFragment.cord).child("203102214.jpg").getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Log.d("aa","suc");

                    //count += 1;
                    //fileRef.changeEventListener(mEventListener);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });

        } catch( IOException e ) {

        }
    }

    public void notKeyboard(){
        // キーボードが出てたら閉じる
        //InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}