package co.edu.aulasenora;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import co.edu.aulasenora.databinding.ActivityRoleSelectionBinding;

public class RoleSelectionActivity extends AppCompatActivity {

    private ActivityRoleSelectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRoleSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupListeners();
    }

    private void setupListeners() {
        binding.cardStudent.setOnClickListener(v -> navigateToRegister("Estudiante"));
        binding.btnStudent.setOnClickListener(v -> navigateToRegister("Estudiante"));
        
        binding.cardVolunteer.setOnClickListener(v -> navigateToRegister("Voluntario"));
        binding.btnVolunteer.setOnClickListener(v -> navigateToRegister("Voluntario"));
    }

    private void navigateToRegister(String role) {
        Intent intent = new Intent(RoleSelectionActivity.this, RegisterActivity.class);
        intent.putExtra("selected_role", role);
        startActivity(intent);
    }
}
