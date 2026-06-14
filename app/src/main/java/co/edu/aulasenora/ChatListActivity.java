package co.edu.aulasenora;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import co.edu.aulasenora.databinding.ActivityChatListBinding;
import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.models.Aula;

public class ChatListActivity extends AppCompatActivity {

    private ActivityChatListBinding binding;
    private DatabaseHelper dbHelper;
    private String userEmail;
    private boolean isVolunteer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("user_email");
        isVolunteer = getIntent().getBooleanExtra("is_volunteer", false);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        binding.includeHeader.btnBack.setOnClickListener(v -> finish());
        binding.includeHeader.tvTitle.setText("Chats");

        if (isVolunteer) {
            binding.includeHeader.headerBar.setBackgroundColor(getColor(R.color.volunteerPrimary));
        } else {
            binding.includeHeader.headerBar.setBackgroundColor(getColor(R.color.studentPrimary));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadChatAulas();
    }

    private void loadChatAulas() {
        List<Aula> aulas = isVolunteer
                ? dbHelper.getChatAulasForVolunteer(userEmail)
                : dbHelper.getChatAulasForStudent(userEmail);

        binding.llChatAulas.removeAllViews();

        if (aulas.isEmpty()) {
            binding.tvEmptyChats.setVisibility(View.VISIBLE);
            return;
        }
        binding.tvEmptyChats.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Aula aula : aulas) {
            View itemView = inflater.inflate(R.layout.item_chat_list, binding.llChatAulas, false);

            TextView tvChatName = itemView.findViewById(R.id.tvChatName);
            TextView tvChatVolunteer = itemView.findViewById(R.id.tvChatVolunteer);
            TextView badgeUnread = itemView.findViewById(R.id.badgeUnread);

            String volunteerName = dbHelper.getUserName(aula.getVolunteerEmail());
            tvChatName.setText("Chat: " + aula.getName());
            tvChatVolunteer.setText(volunteerName != null ? volunteerName : aula.getVolunteerEmail());

            int unread = dbHelper.getUnreadCount(aula.getId(), userEmail);
            badgeUnread.setVisibility(unread > 0 ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(this, ChatDetailActivity.class);
                intent.putExtra("aula_id", aula.getId());
                intent.putExtra("user_email", userEmail);
                intent.putExtra("aula_name", aula.getName());
                startActivity(intent);
            });

            binding.llChatAulas.addView(itemView);
        }
    }
}
