package nju.androidchat.client.hw1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hw1PicturePresenter implements Hw1Contract.PicturePresenter{


    public void getImage(String url, Handler handler, UUID messageID, boolean isMe) {
        new Thread(new Runnable() {
            URL fileUrl = null;
            Bitmap bitmap = null;
            @Override
            public void run() {

                try {
                    fileUrl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    HttpURLConnection conn = (HttpURLConnection) fileUrl
                            .openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.obj = bitmap;
                if (isMe){
                    msg.what=1;
                }
                else {
                    msg.what=2;
                }
                Bundle bundle=new Bundle();
                bundle.putString("messageID", messageID.toString());
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }).start();
    }

    public static String checkPicture(String content) {
        //Log.d("内容：",content);
        String pattern = "!\\[.*]\\(\\{(.*)\\}\\)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(content);
        System.out.println(m.find());
        if (!m.find()){
            return content;
        }
        else {
            return m.group(1);
        }
    }

    @Override
    public void start() {

    }
}