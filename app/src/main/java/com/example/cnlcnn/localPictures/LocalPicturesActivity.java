package com.example.cnlcnn.localPictures;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.cnlcnn.adapter.LocalPictureAdapter;
import com.example.cnlcnn.entity.Constants;
import com.example.cnlcnn.model.LocalPictureModel;
import com.example.cnlcnn.utils.CopyLocalPicture;
import com.example.cnlcnn.utils.ScanLocalPicture;
import com.example.cnlcnn.wallpaper.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 *  项目名：  WallPaper
 *  创建者:   LiChuang
 *  创建时间： 2017/5/4.
 *  描述：    添加本地图片
 */

public class LocalPicturesActivity extends AppCompatActivity {

    private String INTENT_TYPE = "image/*";
    private int SELECT_PIC_KITKAT = 0;
    private int SELECT_PIC = 1;

    private ImageView addPictureView;//添加图片按钮
    private Toolbar mToolbar;
    private RecyclerView localPictures;
    private List<LocalPictureModel> mPictureList = new ArrayList<>();//存放本地图片对象的集合
    private LocalPictureAdapter mAdapter;//本地图片适配器

    private String fromPath;          //图片来源路径
    private String toPath;          //图片复制到目标文件夹路径
    private List<String> pathList = new ArrayList<>();//存放添加后本地图片路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_local);

        Intent intent = getIntent();
        String typeName = intent.getStringExtra(Constants.TYPE_NAME);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //设置标题，即侧滑栏的选项
        mToolbar.setTitle(typeName);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        addPictureView = (ImageView) findViewById(R.id.addPicture);
        addPictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用intent调用系统提供的相册功能，
//                使用startActivityForResult是为了获取用户选择的图片
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(INTENT_TYPE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {/**4.4系统以上**/
                    startActivityForResult(intent, SELECT_PIC_KITKAT);
                } else {
                    startActivityForResult(intent, SELECT_PIC);
                }
            }
        });

        localPictures = (RecyclerView) findViewById(R.id.type_picture);
        GridLayoutManager layoutManager = new GridLayoutManager(LocalPicturesActivity.this, 2);
        localPictures.setLayoutManager(layoutManager);

        File sdcardDir = Environment.getExternalStorageDirectory();
        //得到一个路径，内容是添加本地图片的存放目录
        toPath = sdcardDir.getPath() + Constants.WALLPAPER_CACHE;

        initPictures();

    }

    /*
     * toolbar里面菜单的点击事件
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home: {//返回上一个界面
                LocalPicturesActivity.this.finish();
            }
            break;
            default:
        }
        return true;
    }

    /**
     * 显示从本地加载的图片
     */
    private void initPictures() {

        mPictureList.clear();

        ScanLocalPicture scan = new ScanLocalPicture(toPath);
        pathList = scan.getPicturePathFromSD();
        //声明一个本地图片实例
        LocalPictureModel picture;
        for (int i = 0; i < pathList.size(); i++) {
            picture = new LocalPictureModel();             //创建实例
            picture.setPath((String) pathList.get(i));//循环将路径添加到实例对象中
//            Log.d("picturePath", (String) pathList.get(i));
            mPictureList.add(picture);//将每一个图片实例添加到集合中
        }

        mAdapter = new LocalPictureAdapter(mPictureList);
        localPictures.setAdapter(mAdapter);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PIC_KITKAT) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }
            if (DocumentsContract.isDocumentUri(LocalPicturesActivity.this, data.getData())) {
                fromPath = getPath(LocalPicturesActivity.this, data);
//                Log.d("path->(1)", fromPath);
            } else {
                fromPath = getPath(LocalPicturesActivity.this, data);
//                Log.d("path->(2)", fromPath);
            }

        } else {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }

        }

        CopyLocalPicture copy = new CopyLocalPicture();
        copy.copyFile(fromPath, toPath);
        initPictures();
    }

    public static String getPath(final Context context, Intent data) {
        Uri uri = data.getData();
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
