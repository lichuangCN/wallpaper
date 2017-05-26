package com.example.cnlcnn.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.cnlcnn.wallpaper.R;

/*
 *  项目名：  WallPaper
 *  创建者:   LiChuang
 *  创建时间:  2017/5/25
 *  描述：
 */

public class CustomDialog extends Dialog {
    public CustomDialog(Context context, int layout, int style) {
        this(context, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, layout, style,
                Gravity.CENTER);
    }

    public CustomDialog(Context context, int width, int height, int layout,
                        int style, int gravity, int anim) {
        super(context, style);

        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = gravity;
        window.setAttributes(params);
        window.setWindowAnimations(anim);
    }

    public CustomDialog(Context context, int width, int height, int layout,
                        int style, int gravity) {
        this(context, width, height, layout, style, gravity,
                R.style.pop_anim_style);
    }
}
