package cn.modificator.launcher.model;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import cn.modificator.launcher.R;
import cn.modificator.launcher.widgets.ObserverFontTextView;
import cn.modificator.launcher.widgets.RatioImageView;

import static androidx.core.content.ContextCompat.startActivity;

public class LikebookNotebook {

    ObserverFontTextView appName;
    RatioImageView appImage;

    Context mContext;
    int showNameRes;
    int showIconRes;

    private static LikebookNotebook instance;

    public static void init(Context context){
        instance = new LikebookNotebook(context.getApplicationContext());
    }

    private LikebookNotebook(Context context) {
        mContext = context;
        showNameRes = R.string.notebook;
        showIconRes = R.drawable.notebook;
    }

    private void updateStatus(){
        if (appName!=null) {
            appName.setText("Notebook");
            appImage.setImageResource(showIconRes);
        }
    }

    public static void bind(View view){
        if (view==null){
            instance.appImage=null;
            instance.appName = null;
            return;
        }
        instance.appName = view.findViewById(R.id.appName);
        instance.appImage = view.findViewById(R.id.appImage);
        instance.updateStatus();
    }

    public static void onClickNotebookItem(){
        Intent intent = new Intent();
        intent.setClassName("com.boyue.app.byreader", "com.boyue.note.app.view.activity.NoteIndexActivity");
        instance.mContext.startActivity(intent);
    }
}
