package nju.androidchat.client.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.LinearLayout;
import android.widget.ImageView;

import androidx.annotation.StyleableRes;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import nju.androidchat.client.R;

public class ItemImageReceive extends LinearLayout {


    @StyleableRes
    int index0 = 0;

    private ImageView imageView;
    private Context context;
    private UUID messageId;
    private OnRecallMessageRequested onRecallMessageRequested;


    public ItemImageReceive(Context context, Bitmap text, UUID messageId) {
        super(context);
        this.context = context;
        inflate(context, R.layout.item_image_receive, this);
        this.messageId = messageId;
        this.imageView = findViewById(R.id.chat_item_content_image);
        this.imageView.setImageBitmap(text);
    }


    public void init(Context context) {

    }

//    public String getText() {
//        return textView.getText().toString();
//    }
//
//    public void setText(String text) {
//        imageView.set.setText(text);
//    }

}
