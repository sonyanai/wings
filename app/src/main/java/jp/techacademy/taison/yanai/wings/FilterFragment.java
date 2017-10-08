package jp.techacademy.taison.yanai.wings;
import android.content.ClipData;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by taiso on 2017/08/27.
 */
//Fraggmentクラスを継承する
public class FilterFragment extends Fragment {
    public static final String TAG = "FilterFragment";
    ImageView filterImageView;
    String intentFileName;
    String intentFolderName;
    String intentUid;
    StorageReference storageRef;
    byte[] bytes;


    //Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_filter,container,false);
        filterImageView = (ImageView)v.findViewById(R.id.filter_imageView);
        return v;
    }
    //Viewが生成し終わった時に呼ばれるメソッド
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Bundle bundle = getArguments();
        intentUid = bundle.getString("intentUid");
        intentFolderName = bundle.getString("intentFolderName");
        intentFileName = bundle.getString("intentFileName");
        bytes = bundle.getByteArray("bytes");


        Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        //.copy(Bitmap.Config.ARGB_8888, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        String bitmapString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        Log.d("image",bitmapString);

        //ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        filterImageView.setImageBitmap(image);



        /*WatchFragmentで押した画像を表示したい

        File localFile =intentFileName;
        storageRef.child(intentUid).child(intentFolderName).child(intentFileName).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // Handle any errors
            Log.d("aaaa","failure");
        }
    });



/*
        ClipData clipData = data.getClipData();
        //ClipData.Item item = clipData.getItemAt(i);
        uri = item.getUri();

        Bitmap image;
        try{
            ContentResolver contentResolver = getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);
            image = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }catch (Exception e){
            return;
        }

        filterImageView.setImageBitmap(image);

*/

    }
}


