package co.edu.aulasenora;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import co.edu.aulasenora.databinding.ActivityStudentDashboardBinding;
import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.models.Aula;
import co.edu.aulasenora.models.ScheduleSlot;

public class StudentDashboardActivity extends AppCompatActivity {

    private ActivityStudentDashboardBinding binding;
    private DatabaseHelper dbHelper;
    private String userEmail;
    private long totalSeconds = 0;
    private Handler timerHandler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            totalSeconds++;
            if (totalSeconds % 30 == 0) {
                dbHelper.updateTimeSpent(userEmail, totalSeconds);
            }
            updateHoursUI();
            timerHandler.postDelayed(this, 1000);
        }
    };
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private List<Aula> lastLoadedAulas;
    private boolean isSearching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityStudentDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        dbHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        userEmail = getIntent().getStringExtra("user_email");
        if (userEmail != null) {
            String username = userEmail.split("@")[0];
            binding.tvWelcomeName.setText("Hola, " + username + " \uD83D\uDC4B");
            
            totalSeconds = dbHelper.getTimeSpent(userEmail);
            updateHoursUI();
            timerHandler.postDelayed(timerRunnable, 1000);
        }

        binding.btnViewAllClasses.setOnClickListener(v -> 
            Toast.makeText(this, "Ver todas las aulas (En construcción)", Toast.LENGTH_SHORT).show()
        );
        
        setupSearch();

        // Bottom navigation
        binding.navInicio.setOnClickListener(v ->
            Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show()
        );

        binding.navChat.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatListActivity.class);
            intent.putExtra("user_email", userEmail);
            intent.putExtra("is_volunteer", false);
            startActivity(intent);
        });

        binding.navPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("user_email", userEmail);
            intent.putExtra("is_volunteer", false);
            startActivity(intent);
        });
    }

    private void setupSearch() {
        binding.etSearchAula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                searchRunnable = () -> {
                    String query = s.toString().trim();
                    isSearching = !query.isEmpty();
                    if (isSearching) {
                        List<Aula> results = dbHelper.searchAulasByName(query);
                        displayAulas(results);
                    } else {
                        loadRecommendedAulas();
                    }
                };
                searchHandler.postDelayed(searchRunnable, 300);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userEmail == null) return;
        loadUpcomingSessions();
        loadActiveTutoring();
        loadChatUnreadBadge();
        loadMyAulas();
        if (!isSearching) {
            loadRecommendedAulas();
        }
    }

    private void loadUpcomingSessions() {
        List<ScheduleSlot> sessions = dbHelper.getUpcomingTutoringForStudent(userEmail, 2);
        binding.llUpcomingContainer.removeAllViews();

        if (sessions.isEmpty()) {
            binding.llUpcomingContainer.setVisibility(View.GONE);
            return;
        }
        binding.llUpcomingContainer.setVisibility(View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (ScheduleSlot slot : sessions) {
            View cardView = inflater.inflate(R.layout.item_upcoming_session_card, binding.llUpcomingContainer, false);

            TextView tvAulaName = cardView.findViewById(R.id.tvUpcomingAulaName);
            TextView tvDate = cardView.findViewById(R.id.tvUpcomingDate);

            tvAulaName.setText(slot.getAulaName() != null ? slot.getAulaName() : "Aula " + slot.getAulaId());
            tvDate.setText(slot.getSlotDate());

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(StudentDashboardActivity.this, StudentAulaDetailActivity.class);
                intent.putExtra("aula_id", slot.getAulaId());
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
            });

            binding.llUpcomingContainer.addView(cardView);
        }
    }

    private void loadActiveTutoring() {
        List<ScheduleSlot> sessions = dbHelper.getTodayTutoringForStudent(userEmail);
        binding.llActiveTutoringContainer.removeAllViews();

        if (sessions.isEmpty()) {
            binding.llActiveTutoringContainer.setVisibility(View.GONE);
            return;
        }
        binding.llActiveTutoringContainer.setVisibility(View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (ScheduleSlot slot : sessions) {
            View cardView = inflater.inflate(R.layout.item_active_tutoring_student_card, binding.llActiveTutoringContainer, false);

            TextView tvTitle = cardView.findViewById(R.id.tvTutoringTitle);
            TextView tvTopic = cardView.findViewById(R.id.tvTutoringTopic);
            TextView tvTime = cardView.findViewById(R.id.tvTutoringTime);

            String aulaName = slot.getAulaName() != null ? slot.getAulaName() : "Aula " + slot.getAulaId();
            tvTitle.setText("Tutoría en " + aulaName);

            tvTopic.setText(slot.getTopic() != null ? slot.getTopic() : "");
            String time = slot.getStartTime() != null && slot.getStartTime().length() >= 5
                    ? slot.getStartTime().substring(0, 5) : slot.getStartTime();
            tvTime.setText("Hoy " + time);

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(StudentDashboardActivity.this, StudentAulaDetailActivity.class);
                intent.putExtra("aula_id", slot.getAulaId());
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
            });

            binding.llActiveTutoringContainer.addView(cardView);
        }
    }

    private void loadChatUnreadBadge() {
        int unread = dbHelper.getUnreadChatNotifications(userEmail).size();
        binding.badgeChatUnread.setVisibility(unread > 0 ? View.VISIBLE : View.GONE);
    }

    private void loadMyAulas() {
        List<Aula> aulas = dbHelper.getEnrolledAulas(userEmail);
        binding.llMyAulasContainer.removeAllViews();

        if (aulas.isEmpty()) {
            binding.cvEmptyMyAulas.setVisibility(View.VISIBLE);
            return;
        }
        binding.cvEmptyMyAulas.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Aula aula : aulas) {
            View cardView = inflater.inflate(R.layout.item_aula_card, binding.llMyAulasContainer, false);

            TextView tvName = cardView.findViewById(R.id.tvAulaName);
            TextView tvSubject = cardView.findViewById(R.id.tvAulaSubject);
            TextView tvDescription = cardView.findViewById(R.id.tvAulaDescription);
            TextView tvDate = cardView.findViewById(R.id.tvAulaDate);
            TextView tvVolunteer = cardView.findViewById(R.id.tvVolunteerName);
            Button btnAction = cardView.findViewById(R.id.btnManageAula);

            tvName.setText(aula.getName());
            tvSubject.setText(aula.getSubject());
            tvDescription.setText(aula.getDescription());

            String rawDate = aula.getCreatedAt();
            if (rawDate != null && rawDate.length() > 10) {
                tvDate.setText("Inscrito: " + rawDate.substring(0, 10));
            }

            String volunteerName = dbHelper.getUserName(aula.getVolunteerEmail());
            if (volunteerName != null) {
                tvVolunteer.setVisibility(View.VISIBLE);
                tvVolunteer.setText("Por: " + volunteerName);
            }

            btnAction.setText("Ver Aula");
            btnAction.setOnClickListener(v -> {
                Intent intent = new Intent(StudentDashboardActivity.this, StudentAulaDetailActivity.class);
                intent.putExtra("aula_id", aula.getId());
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
            });

            binding.llMyAulasContainer.addView(cardView);
        }
    }

    private void loadRecommendedAulas() {
        List<Aula> aulas = dbHelper.getRecentAulas(3);
        displayAulas(aulas);
    }

    private void displayAulas(List<Aula> aulas) {
        lastLoadedAulas = aulas;
        binding.llRecommendedContainer.removeAllViews();
        binding.cvEmptyRecommended.setVisibility(View.GONE);

        List<Integer> enrolledIds = dbHelper.getEnrolledAulaIds(userEmail);

        // Filter out enrolled aulas
        java.util.ArrayList<Aula> filtered = new java.util.ArrayList<>();
        for (Aula a : aulas) {
            if (!enrolledIds.contains(a.getId())) {
                filtered.add(a);
            }
        }

        if (filtered.isEmpty()) {
            binding.cvEmptyRecommended.setVisibility(View.VISIBLE);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        for (Aula aula : filtered) {
            View cardView = inflater.inflate(R.layout.item_aula_card, binding.llRecommendedContainer, false);

            TextView tvName = cardView.findViewById(R.id.tvAulaName);
            TextView tvSubject = cardView.findViewById(R.id.tvAulaSubject);
            TextView tvDescription = cardView.findViewById(R.id.tvAulaDescription);
            TextView tvDate = cardView.findViewById(R.id.tvAulaDate);
            TextView tvVolunteer = cardView.findViewById(R.id.tvVolunteerName);
            Button btnAction = cardView.findViewById(R.id.btnManageAula);

            tvName.setText(aula.getName());
            tvSubject.setText(aula.getSubject());
            tvDescription.setText(aula.getDescription());

            String rawDate = aula.getCreatedAt();
            if (rawDate != null && rawDate.length() > 10) {
                tvDate.setText("Creado: " + rawDate.substring(0, 10));
            } else {
                tvDate.setText("Creado: " + rawDate);
            }

            String volunteerName = dbHelper.getUserName(aula.getVolunteerEmail());
            if (volunteerName != null) {
                tvVolunteer.setVisibility(View.VISIBLE);
                tvVolunteer.setText("Por: " + volunteerName);
            }

            String status = dbHelper.getAccessRequestStatus(aula.getId(), userEmail);

            if (status == null) {
                btnAction.setText("Solicitar Acceso");
                btnAction.setEnabled(true);
                btnAction.setOnClickListener(v -> {
                    long result = dbHelper.createAccessRequest(aula.getId(), userEmail);
                    if (result != -1) {
                        Toast.makeText(this, "Solicitud enviada a " + aula.getName(), Toast.LENGTH_SHORT).show();
                        loadRecommendedAulas();
                    } else {
                        Toast.makeText(this, "Error al enviar la solicitud", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if ("pending".equals(status)) {
                btnAction.setText("Solicitud Pendiente");
                btnAction.setEnabled(false);
            } else if ("rejected".equals(status)) {
                btnAction.setText("Solicitud Denegada");
                btnAction.setEnabled(false);
            }

            binding.llRecommendedContainer.addView(cardView);
        }
    }

    private void updateHoursUI() {
        long hours = totalSeconds / 3600;
        binding.tvTotalHours.setText(String.valueOf(hours));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (userEmail != null) {
            dbHelper.updateTimeSpent(userEmail, totalSeconds);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
        if (userEmail != null) {
            dbHelper.updateTimeSpent(userEmail, totalSeconds);
        }
    }
}
