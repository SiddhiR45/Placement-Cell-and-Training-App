package chats;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationViewModel extends ViewModel {

    private MutableLiveData<List<String>> notificationMessagesLiveData = new MutableLiveData<>();
    private List<String> notificationMessages = new ArrayList<>();

    public NotificationViewModel() {
        notificationMessagesLiveData.setValue(notificationMessages);
    }

    public void addNotificationMessage(String message) {
        notificationMessages.add(message);
        notificationMessagesLiveData.setValue(notificationMessages);
    }

    public LiveData<List<String>> getNotificationMessagesLiveData() {
        return notificationMessagesLiveData;
    }
}
