package co.edu.aulasenora;

import android.os.Bundle;
import android.widget.Toast;

import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import android.content.Intent;

import co.edu.aulasenora.databinding.ActivityVolunteerDashboardBinding;
import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.models.Aula;

public class VolunteerDashboardActivity extends AppCompatActivity {

    private ActivityVolunteerDashboardBinding binding;
    private DatabaseHelper dbHelper;
    private String userEmail;
    private long totalSeconds = 0;
    private Handler timerHandler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            totalSeconds++;
            updateHoursUI();
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityVolunteerDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        dbHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        // Set username in header from intent extra
        userEmail = getIntent().getStringExtra("user_email");
        if (userEmail != null) {
            String username = userEmail.split("@")[0];
            binding.tvWelcomeName.setText("Hola, " + username + " \uD83C\uDF93");
            
            // Start timer
            totalSeconds = dbHelper.getTimeSpent(userEmail);
            updateHoursUI();
            timerHandler.postDelayed(timerRunnable, 1000);
            
            loadAulas();
        }

        // Setup placeholder click listeners for all interactive elements
        setupPlaceholderClicks();
    }

    private void setupPlaceholderClicks() {
        // Notification bell
        binding.btnNotifications.setOnClickListener(v ->
            Toast.makeText(this, "Notificaciones (En construcción)", Toast.LENGTH_SHORT).show()
        );

        // Prepare session button
        binding.btnPrepare.setOnClickListener(v ->
            Toast.makeText(this, "Preparar sesión (En construcción)", Toast.LENGTH_SHORT).show()
        );

        // Next session card
        binding.cardNextSession.setOnClickListener(v ->
            Toast.makeText(this, "Sesiones próximas (En construcción)", Toast.LENGTH_SHORT).show()
        );

        // View all classes
        binding.btnViewAllClasses.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageAulasActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });

        // Create aula placeholder
        binding.btnCreateAulaPlaceholder.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateAulaActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });

        // Active tutoring card
        binding.cardActiveTutoring.setOnClickListener(v ->
            Toast.makeText(this, "Tutoría activa con María López (En construcción)", Toast.LENGTH_SHORT).show()
        );

        // View tutoring button
        binding.btnViewTutoring.setOnClickListener(v ->
            Toast.makeText(this, "Ver tutoría (En construcción)", Toast.LENGTH_SHORT).show()
        );

        // Add resource button
        binding.btnAddResource.setOnClickListener(v ->
            Toast.makeText(this, "Agregar recurso (En construcción)", Toast.LENGTH_SHORT).show()
        );

        // Resource cards
        binding.cardResource1.setOnClickListener(v ->
            Toast.makeText(this, "Material Álgebra Lineal (En construcción)", Toast.LENGTH_SHORT).show()
        );

        binding.cardResource2.setOnClickListener(v ->
            Toast.makeText(this, "Ejercicios de Cálculo (En construcción)", Toast.LENGTH_SHORT).show()
        );

        // Bottom navigation
        binding.navInicio.setOnClickListener(v ->
            Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show()
        );

        binding.navHistorial.setOnClickListener(v ->
            Toast.makeText(this, "Historial (En construcción)", Toast.LENGTH_SHORT).show()
        );

        binding.navPerfil.setOnClickListener(v ->
            Toast.makeText(this, "Perfil (En construcción)", Toast.LENGTH_SHORT).show()
        );
    }

    private void loadAulas() {
        if (userEmail == null) return;
        
        // Remove dynamic cards to avoid duplicates, keeping the empty state
        int childCount = binding.llAulasContainer.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            android.view.View child = binding.llAulasContainer.getChildAt(i);
            if (child.getId() != R.id.cvEmptyAulas) {
                binding.llAulasContainer.removeViewAt(i);
            }
        }
        
        List<Aula> aulas = dbHelper.getRecentAulasByVolunteer(userEmail, 2);
        
        if (aulas.isEmpty()) {
            binding.cvEmptyAulas.setVisibility(android.view.View.VISIBLE);
        } else {
            binding.cvEmptyAulas.setVisibility(android.view.View.GONE);
            
            android.view.LayoutInflater inflater = android.view.LayoutInflater.from(this);
            for (Aula aula : aulas) {
                android.view.View cardView = inflater.inflate(R.layout.item_aula_card, binding.llAulasContainer, false);
                
                android.widget.TextView tvName = cardView.findViewById(R.id.tvAulaName);
                android.widget.TextView tvSubject = cardView.findViewById(R.id.tvAulaSubject);
                android.widget.TextView tvDescription = cardView.findViewById(R.id.tvAulaDescription);
                android.widget.TextView tvDate = cardView.findViewById(R.id.tvAulaDate);
                android.widget.Button btnManage = cardView.findViewById(R.id.btnManageAula);
                
                tvName.setText(aula.getName());
                tvSubject.setText(aula.getSubject());
                tvDescription.setText(aula.getDescription());
                
                String rawDate = aula.getCreatedAt();
                if (rawDate != null && rawDate.length() > 10) {
                    tvDate.setText("Creado: " + rawDate.substring(0, 10));
                } else {
                    tvDate.setText("Creado: " + rawDate);
                }
                
                btnManage.setOnClickListener(v -> {
                    android.widget.Toast.makeText(this, "Gestionar aula: " + aula.getName() + " (En construcción)", android.widget.Toast.LENGTH_SHORT).show();
                });
                
                binding.llAulasContainer.addView(cardView);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userEmail != null) {
            loadAulas();
        }
    }

    private void updateHoursUI() {
        long hours = totalSeconds / 3600;
        binding.tvDonatedHours.setText(String.valueOf(hours));
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
