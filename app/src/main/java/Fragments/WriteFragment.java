package Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xueyuanzhang.weibo.AccountInfo;
import com.example.xueyuanzhang.weibo.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by xueyuanzhang on 16/5/7.
 */
public class WriteFragment extends Fragment {
    public interface OnSettingListener {
        void onInitToolbar(Toolbar toolbar);
    }

    public OnSettingListener onSettingListener;
    public AccountInfo accountInfo = new AccountInfo();
    private EditText editText;
    private Button button;
    private Toolbar toolbar;
    private ImageView imageView;
    private ImageView takeIcon;
    private ImageView show;
    private ImageView showBox;
    private String picPath;
    public Bitmap bitmap;
    public final static int SELECT_PIC = 1;
    public final static int TAKE_PHOTO_KEY = 2;
    private SharedPreferences preferences;

    public void setOnSetttingListener(OnSettingListener onSettingListener) {
        this.onSettingListener = onSettingListener;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences=getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        accountInfo.accessToken=preferences.getString("access_token","no_exist");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.write_fragment, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbarOfWrite);
        // onSettingListener.onInitToolbar(toolbar);
        editText = (EditText) rootView.findViewById(R.id.sendWeibo);
        button = (Button) rootView.findViewById(R.id.sendButton);

        imageView = (ImageView) rootView.findViewById(R.id.sendPic);
        takeIcon = (ImageView) rootView.findViewById(R.id.TakePhoto);
        show = (ImageView) rootView.findViewById(R.id.selectPic);
        showBox = (ImageView) rootView.findViewById(R.id.showBox);

        //从相册中获取照片
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/s/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, SELECT_PIC);
            }
        });

        //拍摄获取照片
        takeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SDstate = Environment.getExternalStorageState();
                if (SDstate.equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, TAKE_PHOTO_KEY);
                } else {
                    Toast.makeText(getActivity(), "为检测到sd卡,请检查你的sd卡状态!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
           bitmap=doPhoto(requestCode, data);
        }

    }

    public Bitmap doPhoto(int requestCode, Intent data) {
        if (requestCode == SELECT_PIC) {
            if (data == null) {
                Toast.makeText(getActivity(), "选择图片文件失败", Toast.LENGTH_SHORT).show();
            } else {
                Uri photouri = data.getData();
                if (photouri == null) {
                    Toast.makeText(getActivity(), "选择文件图片出错", Toast.LENGTH_SHORT).show();
                } else {
                    String[] po = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().managedQuery(photouri, po, null, null, null);
                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndexOrThrow(po[0]);
                        cursor.moveToFirst();
                        picPath = cursor.getString(columnIndex);
                        if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                            cursor.close();
                        }
                    }
                    File file = new File(picPath);
                    Picasso.with(getActivity()).load(file).into(show);
                }

            }

        } else if (requestCode == TAKE_PHOTO_KEY) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                    return bitmap;



        }

        return null;
    }



    public InputStream convertPic(String picPath) {
        try {
            InputStream in = new FileInputStream(new File(picPath));
            return in;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showPhotoToken(){
        show.setImageBitmap(bitmap);
    }


}
