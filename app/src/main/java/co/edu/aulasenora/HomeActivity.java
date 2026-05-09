package co.edu.aulasenora;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadStatistics();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update stats every time the user comes back to this screen
        loadStatistics();
    }

    private void loadStatistics() {
        int studentCount = dbHelper.getRoleCount("Estudiante");
        int volunteerCount = dbHelper.getRoleCount("Voluntario");

        binding.tvStudentsCount.setText(String.valueOf(studentCount));
        binding.tvVolunteersCount.setText(String.valueOf(volunteerCount));
        
        // These are not implemented yet, default to 0 as requested
        binding.tvTutoriesCount.setText("0");
        binding.tvRatingCount.setText("0");
    }

    private void setupListeners() {
        // All primary CTAs and top login button route to MainActivity (Login)
        binding.btnTopLogin.setOnClickListener(v -> navigateToLogin());
        binding.btnHeroJoinNow.setOnClickListener(v -> navigateToLogin());
        binding.btnBottomStartNow.setOnClickListener(v -> navigateToLogin());
    }

    private void navigateToLogin() {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
