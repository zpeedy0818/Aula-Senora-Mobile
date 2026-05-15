package co.edu.aulasenora;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import co.edu.aulasenora.databinding.ActivityStudentDashboardBinding;

public class StudentDashboardActivity extends AppCompatActivity {

    private ActivityStudentDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityStudentDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom); // Left and right padding removed for full width header
            return insets;
        });

        // Set username in header
        String email = getIntent().getStringExtra("user_email");
        if (email != null) {
            String username = email.split("@")[0];
            binding.tvWelcomeName.setText("Hola, " + username + " \uD83D\uDC4B");
        }

        // Setup placeholder clicks
        binding.btnViewAllClasses.setOnClickListener(v -> 
            Toast.makeText(this, "Ver todas las aulas (En construcción)", Toast.LENGTH_SHORT).show()
        );
        
        binding.cardMath.setOnClickListener(v -> 
            Toast.makeText(this, "Detalles de Matemáticas (En construcción)", Toast.LENGTH_SHORT).show()
        );
    }
}
