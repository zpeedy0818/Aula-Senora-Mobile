package co.edu.aulasenora.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM_SERVICE";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Nuevo token FCM: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Mensaje recibido de: " + remoteMessage.getFrom());

        String title;
        String body;
        Map<String, String> data = remoteMessage.getData();

        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        } else if (data != null) {
            title = data.get("title");
            body = data.get("body");
        } else {
            title = getApplicationInfo().loadLabel(getPackageManager()).toString();
            body = "Tienes una nueva notificación";
        }

        if (title == null) {
            title = getApplicationInfo().loadLabel(getPackageManager()).toString();
        }

        NotificationHelper.showNotification(getApplicationContext(), title, body, data);
    }
}
