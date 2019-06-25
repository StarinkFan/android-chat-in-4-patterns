package nju.androidchat.client.component;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.StyleableRes;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import lombok.Setter;
import nju.androidchat.client.R;

public class ItemImageSend extends LinearLayout implements View.OnLongClickListener {
    @StyleableRes
    int index0 = 0;

    private ImageView imageView;
    private Context context;
    private UUID messageId;
    @Setter private OnRecallMessageRequested onRecallMessageRequested;

    public ItemImageSend(Context context, Bitmap text, UUID messageId, OnRecallMessageRequested onRecallMessageRequested) {
        super(context);
        this.context = context;
        inflate(context, R.layout.item_image_send, this);
        this.messageId = messageId;

        this.imageView = findViewById(R.id.chat_item_content_image);
        this.imageView.setImageBitmap(text);

        this.onRecallMessageRequested = onRecallMessageRequested;
        this.setOnLongClickListener(this);
    }

//    public String getText() {
//        return textView.getText().toString();
//    }
//
//    public void setText(String text) {
//        textView.setText(text);
//    }

    @Override
    public boolean onLongClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要撤回这条消息吗？")
                .setPositiveButton("是", (dialog, which) -> {
                    if (onRecallMessageRequested != null) {
                        onRecallMessageRequested.onRecallMessageRequested(this.messageId);
                    }
                })
                .setNegativeButton("否", ((dialog, which) -> {
                }))
                .create()
                .show();

        return true;


    }

}
