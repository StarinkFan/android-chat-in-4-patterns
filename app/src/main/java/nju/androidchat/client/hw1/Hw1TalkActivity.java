package nju.androidchat.client.hw1;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.java.Log;
import nju.androidchat.client.ClientMessage;
import nju.androidchat.client.R;
import nju.androidchat.client.Utils;
import nju.androidchat.client.component.ItemImageReceive;
import nju.androidchat.client.component.ItemImageSend;
import nju.androidchat.client.component.ItemTextReceive;
import nju.androidchat.client.component.ItemTextSend;
import nju.androidchat.client.component.OnRecallMessageRequested;

@Log
public class Hw1TalkActivity extends AppCompatActivity implements Hw1Contract.View, TextView.OnEditorActionListener, OnRecallMessageRequested {
    private Hw1Contract.Presenter presenter;
    private Hw1PicturePresenter picturePresenter=new Hw1PicturePresenter();
    private String userName;

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler() {
        //Handler里的handMessage方法
        public void handleMessage(Message msg) {
            if (msg.what==1){
                addImageViewMe((Bitmap)msg.obj,UUID.fromString(msg.getData().getString("messageID")));
            }
            else {
                addImageView((Bitmap)msg.obj,UUID.fromString(msg.getData().getString("messageID")));
            }
        }
    };

    private void addImageViewMe(Bitmap imageView,UUID messageID){
        LinearLayout content = findViewById(R.id.chat_content);
        content.addView(new ItemImageSend(this, imageView,messageID , this));
    }

    private void addImageView(Bitmap imageView,UUID messageID){
        LinearLayout content = findViewById(R.id.chat_content);
        content.addView(new ItemImageReceive(this, imageView,messageID));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Hw1TalkModel hw1TalkModel=new Hw1TalkModel();

        // Create the presenter
        this.presenter = new Hw1TalkPresenter(hw1TalkModel, this, new ArrayList<>());
        hw1TalkModel.setHw1TalkPresenter(this.presenter);
        userName=this.presenter.getUsername();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showMessageList(List<ClientMessage> messages) {
        runOnUiThread(() -> {
                    LinearLayout content = findViewById(R.id.chat_content);

                    // 删除所有已有的ItemText
                    content.removeAllViews();

                    // 增加Item
                    for (ClientMessage message : messages) {
                        String text = String.format("%s", message.getMessage());

                        if(!checkPicture(text).equals(text)){
                            if (message.getSenderUsername().equals(this.presenter.getUsername())) {
                                picturePresenter.getImage(checkPicture(text), handler,message.getMessageId(),true);
                            } else {
                                picturePresenter.getImage(checkPicture(text), handler,message.getMessageId(),false);
                            }
                        }
                        else {
                            if (message.getSenderUsername().equals(this.presenter.getUsername())) {
                                content.addView(new ItemTextSend(this, text, message.getMessageId(), this));
                            } else {
                                content.addView(new ItemTextReceive(this, text, message.getMessageId()));
                            }
                        }
                    }

                    Utils.scrollListToBottom(this);
                }
        );
    }

    @Override
    public void setPresenter(Hw1Contract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            return hideKeyboard();
        }
        return super.onTouchEvent(event);
    }

    private boolean hideKeyboard() {
        return Utils.hideKeyboard(this);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (Utils.send(actionId, event)) {
            hideKeyboard();
            // 异步地让Controller处理事件
            sendText();
        }
        return false;
    }

    private void sendText() {
        EditText text = findViewById(R.id.et_content);
        AsyncTask.execute(() -> {
            this.presenter.sendMessage(text.getText().toString());
        });
        text.setText("");
    }

    public void onBtnSendClicked(View v) {
        hideKeyboard();
        sendText();
    }

    // 当用户长按消息，并选择撤回消息时做什么，MVP-0不实现
    @Override
    public void onRecallMessageRequested(UUID messageId) {

    }

    public String checkPicture(String url) {
        String pattern = "!\\[.*]\\((.*)\\)$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        if (!m.find()){
            return url;
        }
        else {
            return ( m.group(1).substring(0,4).equals("http")&& (!m.group(1).substring(0,5).equals("https")) ) ? (m.group(1).substring(0,4)+"s"+m.group(1).substring(4)) : m.group(1);
        }
    }
}
