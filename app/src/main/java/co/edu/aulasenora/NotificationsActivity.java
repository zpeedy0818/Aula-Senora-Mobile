package co.edu.aulasenora;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.edu.aulasenora.databinding.ActivityNotificationsBinding;
import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.models.NotificationItem;

public class NotificationsActivity extends AppCompatActivity {

    private ActivityNotificationsBinding binding;
    private DatabaseHelper dbHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("user_email");

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        binding.includeHeader.btnBack.setOnClickListener(v -> finish());
        binding.includeHeader.tvTitle.setText("Notificaciones");

        loadNotifications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotifications();
    }

    private void loadNotifications() {
        List<NotificationItem> allItems = new ArrayList<>();
        allItems.addAll(dbHelper.getPendingAccessNotifications(userEmail));
        allItems.addAll(dbHelper.getPendingTutoringNotifications(userEmail));
        allItems.addAll(dbHelper.getUnreadChatNotifications(userEmail));
        Collections.sort(allItems);

        binding.llNotificationsContainer.removeAllViews();

        if (allItems.isEmpty()) {
            binding.tvEmptyNotifications.setVisibility(View.VISIBLE);
            return;
        }
        binding.tvEmptyNotifications.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (NotificationItem item : allItems) {
            View cardView = inflater.inflate(R.layout.item_notification_card, binding.llNotificationsContainer, false);

            FrameLayout flIconFrame = cardView.findViewById(R.id.flIconFrame);
            ImageView ivIcon = cardView.findViewById(R.id.ivNotifIcon);
            TextView tvTitle = cardView.findViewById(R.id.tvNotifTitle);
            TextView tvDesc = cardView.findViewById(R.id.tvNotifDescription);
            TextView tvTime = cardView.findViewById(R.id.tvNotifTime);

            String type = item.getType();
            switch (type) {
                case "access":
                    flIconFrame.setBackgroundResource(R.drawable.bg_circle_orange);
                    ivIcon.setImageResource(R.drawable.ic_users);
                    break;
                case "tutoring":
                    flIconFrame.setBackgroundResource(R.drawable.bg_circle_orange);
                    ivIcon.setImageResource(R.drawable.ic_clock);
                    break;
                case "chat":
                    flIconFrame.setBackgroundResource(R.drawable.bg_circle_cyan);
                    ivIcon.setImageResource(R.drawable.ic_chat);
                    break;
            }

            tvTitle.setText(item.getTitle());
            tvDesc.setText(item.getDescription());
            tvTime.setText(formatTime(item.getTimestamp()));

            cardView.setOnClickListener(v -> {
                if ("chat".equals(type)) {
                    Intent intent = new Intent(this, ChatDetailActivity.class);
                    intent.putExtra("aula_id", item.getAulaId());
                    intent.putExtra("user_email", userEmail);
                    String aulaName = item.getTitle().replace("Nuevo mensaje en ", "");
                    intent.putExtra("aula_name", aulaName);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, ManageAulaActivity.class);
                    intent.putExtra("aula_id", item.getAulaId());
                    intent.putExtra("user_email", userEmail);
                    startActivity(intent);
                }
            });

            binding.llNotificationsContainer.addView(cardView);
        }
    }

    private String formatTime(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(isoDate);
            if (date == null) return isoDate;
            long diff = System.currentTimeMillis() - date.getTime();
            long minutes = diff / 60000;
            if (minutes < 1) return "Ahora";
            if (minutes < 60) return "Hace " + minutes + " min";
            long hours = minutes / 60;
            if (hours < 24) return "Hace " + hours + "h";
            long days = hours / 24;
            if (days < 7) return "Hace " + days + " día" + (days == 1 ? "" : "s");
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
            return df.format(date);
        } catch (Exception e) {
            return isoDate;
        }
    }
}
