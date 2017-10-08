package jp.techacademy.taison.yanai.wings;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

/**
 * Created by taiso on 2017/08/27.
 */
//Fraggmentクラスを継承する
public class FilterFragment extends Fragment {
    public static final String TAG = "FilterFragment";
    ImageView filterImageView;
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
        bytes = bundle.getByteArray("bytes");


        Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        String bitmapString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        Log.d("image",bitmapString);

        filterImageView.setImageBitmap(image);


    }
}


