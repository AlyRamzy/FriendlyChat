package com.google.firebase.udacity.friendlychat.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.firebase.udacity.friendlychat.FriendlyMessage;
import com.google.firebase.udacity.friendlychat.repositories.FriendlyMessageRepository;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Aly on 7/10/2019.
 */

public class MainActivityViewModel extends ViewModel{
    private MutableLiveData<List<FriendlyMessage>> messages;
    private FriendlyMessageRepository friendlyMessageRepository;

    @Override
    protected void onCleared() {
        super.onCleared();
        friendlyMessageRepository.clean();
    }
    public void init(){
        friendlyMessageRepository=FriendlyMessageRepository.getInstance();
        friendlyMessageRepository.init();
    }

    public void getMessagesFromRepository() {
        if (messages == null) {
            messages = new MutableLiveData<List<FriendlyMessage>>();
            List<FriendlyMessage> messageList=new ArrayList<>();
            messages.setValue(messageList);
            loadMessages();
        }

    }
    public LiveData<List<FriendlyMessage>> getMessages(){

        return messages;
    }

    private void loadMessages() {
        friendlyMessageRepository.addListener(new FriendlyMessageRepository.FirebaseDatabaseRepositoryCallback<FriendlyMessage>() {
            @Override
            public void onSuccess(FriendlyMessage result) {
                Log.e(TAG, "Triger onChildAdded"+messages.getValue().size() );
                List<FriendlyMessage> messageList=messages.getValue();
                messageList.add(result);

                messages.setValue(messageList);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        // do async operation to fetch articles
    }


}
