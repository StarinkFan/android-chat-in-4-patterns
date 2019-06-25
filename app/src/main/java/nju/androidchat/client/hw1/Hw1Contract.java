package nju.androidchat.client.hw1;

import android.os.Handler;

import java.util.List;
import java.util.UUID;

import nju.androidchat.client.ClientMessage;

public interface Hw1Contract {
    interface View extends BaseView<Presenter> {
        void showMessageList(List<ClientMessage> messages);
    }

    interface Presenter extends BasePresenter {
        void sendMessage(String content);

        void receiveMessage(ClientMessage content);

        String getUsername();

        //撤回消息mvp0不实现
        void recallMessage(int index0);
    }

    interface PicturePresenter extends BasePresenter {

    }

    interface Model {
        ClientMessage sendInformation(String message);

        String getUsername();
    }
}
