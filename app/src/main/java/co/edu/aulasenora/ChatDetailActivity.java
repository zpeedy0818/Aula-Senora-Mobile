package co.edu.aulasenora;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.edu.aulasenora.databinding.ActivityChatDetailBinding;
import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.models.ChatMessage;

public class ChatDetailActivity extends AppCompatActivity {

    private ActivityChatDetailBinding binding;
    private DatabaseHelper dbHelper;
    private String userEmail;
    private int aulaId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("user_email");
        aulaId = getIntent().getIntExtra("aula_id", -1);
        String aulaName = getIntent().getStringExtra("aula_name");

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        binding.includeHeader.btnBack.setOnClickListener(v -> finish());
        binding.includeHeader.tvTitle.setText(aulaName != null ? aulaName : "Chat");

        userName = dbHelper.getUserName(userEmail);
        if (userName == null) userName = userEmail;

        binding.btnSend.setOnClickListener(v -> sendMessage());

        binding.etMessage.setOnEditorActionListener((v, actionId, event) -> {
            sendMessage();
            return true;
        });

        binding.etMessage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.scrollMessages.postDelayed(() ->
                    binding.scrollMessages.fullScroll(View.FOCUS_DOWN), 300);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (aulaId == -1) return;
        loadMessages();
        markAsRead();
        binding.etMessage.requestFocus();
        binding.scrollMessages.postDelayed(() -> {
            binding.scrollMessages.fullScroll(View.FOCUS_DOWN);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.etMessage, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }

    private void sendMessage() {
        String message = binding.etMessage.getText().toString().trim();
        if (message.isEmpty()) {
            Toast.makeText(this, "Escribe un mensaje", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = dbHelper.createChatMessage(aulaId, userEmail, message);
        if (result != -1) {
            binding.etMessage.setText("");
            loadMessages();
            binding.scrollMessages.post(() -> {
                binding.scrollMessages.fullScroll(View.FOCUS_DOWN);
                binding.etMessage.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(binding.etMessage, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        } else {
            Toast.makeText(this, "Error al enviar mensaje", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMessages() {
        List<ChatMessage> messages = dbHelper.getChatMessages(aulaId);
        binding.llMessages.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);
        for (ChatMessage msg : messages) {
            View itemView = inflater.inflate(R.layout.item_chat_message, binding.llMessages, false);

            TextView tvSender = itemView.findViewById(R.id.tvSenderName);
            TextView tvText = itemView.findViewById(R.id.tvMessageText);
            TextView tvTime = itemView.findViewById(R.id.tvMessageTime);
            View cardMessage = itemView.findViewById(R.id.cardMessage);
            View llSpacerLeft = itemView.findViewById(R.id.llSpacerLeft);
            View llSpacerRight = itemView.findViewById(R.id.llSpacerRight);

            boolean isMine = msg.getSenderEmail().equals(userEmail);
            String senderName = isMine ? "Tú" : dbHelper.getUserName(msg.getSenderEmail());
            if (senderName == null) senderName = msg.getSenderEmail();

            tvSender.setText(senderName);
            tvText.setText(msg.getMessage());

            String rawDate = msg.getCreatedAt();
            if (rawDate != null && rawDate.length() >= 16) {
                tvTime.setText(rawDate.substring(11, 16));
            } else {
                tvTime.setText("");
            }

            if (isMine) {
                cardMessage.setBackgroundColor(getColor(R.color.primaryCyan));
                tvSender.setTextColor(getColor(R.color.white));
                tvText.setTextColor(getColor(R.color.white));
                tvTime.setTextColor(getColor(R.color.white));
                llSpacerLeft.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f));
                llSpacerRight.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
            } else {
                cardMessage.setBackgroundColor(getColor(R.color.white));
                tvSender.setTextColor(getColor(R.color.textDark));
                tvText.setTextColor(getColor(R.color.textDark));
                tvTime.setTextColor(getColor(R.color.textGray));
                llSpacerLeft.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
                llSpacerRight.setLayoutParams(new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f));
            }

            binding.llMessages.addView(itemView);
        }
    }

    private void markAsRead() {
        List<ChatMessage> messages = dbHelper.getChatMessages(aulaId);
        if (!messages.isEmpty()) {
            int lastId = messages.get(messages.size() - 1).getId();
            dbHelper.markChatAsRead(aulaId, userEmail, lastId);
        }
    }
}
