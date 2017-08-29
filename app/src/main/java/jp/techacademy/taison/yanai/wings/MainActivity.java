package jp.techacademy.taison.yanai.wings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.item_receive:
                    // Fragmentを作成します
                    ReceiveFragment fragmentReceive = new ReceiveFragment();
                    // コードからFragmentを追加
                    // Fragmentの追加や削除といった変更を行う際は、Transactionを利用します
                    // 新しく追加を行うのでaddを使用します
                    // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
                    transaction.replace(R.id.container, fragmentReceive);
                    // 最後にcommitを使用することで変更を反映します
                    transaction.commit();
                    return true;

                case R.id.item_send:
                    // Fragmentを作成します
                    SendFragment fragmentSend = new SendFragment();
                    // 他にも、メソッドにはreplace removeがあります
                    // メソッドの1つ目の引数は対象のViewGroupのID、2つ目の引数は追加するfragment
                    transaction.replace(R.id.container, fragmentSend);
                    // 最後にcommitを使用することで変更を反映します
                    transaction.commit();
                    return true;

                case R.id.item_realm:
                    RealmFragment fragmentRealm = new RealmFragment();
                    transaction.replace(R.id.container, fragmentRealm);
                    transaction.commit();
                    return true;

                case R.id.item_profile:
                    ProfileFragment fragmentProfile = new ProfileFragment();
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
        galleryIntent.setType("image/*");
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
    }
}
