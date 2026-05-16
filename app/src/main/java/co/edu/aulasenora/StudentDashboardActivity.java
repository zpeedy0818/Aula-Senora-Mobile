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

import co.edu.aulasenora.databinding.ActivityStudentDashboardBinding;
import co.edu.aulasenora.db.DatabaseHelper;

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
            updateHoursUI();
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityStudentDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        dbHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom); // Left and right padding removed for full width header
            return insets;
        });

        // Set username in header
        userEmail = getIntent().getStringExtra("user_email");
        if (userEmail != null) {
            String username = userEmail.split("@")[0];
            binding.tvWelcomeName.setText("Hola, " + username + " \uD83D\uDC4B");
            
            // Start timer
            totalSeconds = dbHelper.getTimeSpent(userEmail);
            updateHoursUI();
            timerHandler.postDelayed(timerRunnable, 1000);
        }

        // Setup placeholder clicks
        binding.btnViewAllClasses.setOnClickListener(v -> 
            Toast.makeText(this, "Ver todas las aulas (En construcción)", Toast.LENGTH_SHORT).show()
        );
        
        binding.cardMath.setOnClickListener(v -> 
            Toast.makeText(this, "Detalles de Matemáticas (En construcción)", Toast.LENGTH_SHORT).show()
        );
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
