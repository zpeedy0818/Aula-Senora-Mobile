package co.edu.aulasenora;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatabaseHelper dbHelper;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        dbHelper = new DatabaseHelper(this);
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupListeners();
    }

    private void setupListeners() {
        // Lógica para visualizar u ocultar la contraseña
        binding.ivTogglePassword.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                binding.ivTogglePassword.setAlpha(1.0f);
            } else {
                binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.ivTogglePassword.setAlpha(0.5f);
            }
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        });

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor llena ambos campos", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.btnLogin.setEnabled(false);
            binding.btnLogin.setText("Ingresando...");

            boolean exists = dbHelper.checkUser(email, password);
            
            binding.btnLogin.setEnabled(true);
            binding.btnLogin.setText("Ingresar");
            
            if (exists) {
                String role = dbHelper.getUserRole(email, password);
                if ("Estudiante".equals(role)) {
                    Toast.makeText(MainActivity.this, "Iniciando sesión como Estudiante...", Toast.LENGTH_SHORT).show();
                    android.content.Intent intent = new android.content.Intent(MainActivity.this, StudentDashboardActivity.class);
                    intent.putExtra("user_email", email);
                    startActivity(intent);
                    finish();
                } else if ("Voluntario".equals(role)) {
                    Toast.makeText(MainActivity.this, "Iniciando sesión como Voluntario...", Toast.LENGTH_SHORT).show();
                    android.content.Intent intent = new android.content.Intent(MainActivity.this, VolunteerDashboardActivity.class);
                    intent.putExtra("user_email", email);
                    startActivity(intent);
                    finish();
                } else if ("Admin".equals(role)) {
                    Toast.makeText(MainActivity.this, "Iniciando sesión como Administrador...", Toast.LENGTH_SHORT).show();
                    android.content.Intent intent = new android.content.Intent(MainActivity.this, AdminDashboardActivity.class);
                    intent.putExtra("user_email", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "¡Bienvenido! Rol no reconocido.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "El usuario no se encuentra en la base de datos o la contraseña es incorrecta.", Toast.LENGTH_LONG).show();
            }
        });

        binding.llRegisterLink.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, RoleSelectionActivity.class);
            startActivity(intent);
        });
    }
}