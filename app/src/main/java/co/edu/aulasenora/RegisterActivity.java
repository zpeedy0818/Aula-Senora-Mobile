package co.edu.aulasenora;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.transition.TransitionManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private DatabaseHelper dbHelper;
    private String selectedRole = "Estudiante"; // Default
    private boolean isPasswordVisible = false;

    private final String[] specialties = {"Selecciona especialidad...", "Matemáticas", "Ciencias", "Lengua", "Sociales"};
    private final Map<String, String> subareasMap = new HashMap<String, String>() {{
        put("Matemáticas", "Álgebra • Geometría • Cálculo • Estadística");
        put("Ciencias", "Física • Química • Biología • Astronomía");
        put("Lengua", "Español • Inglés • Lingüística • Literatura");
        put("Sociales", "Historia • Geografía • Sociología • Economía");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        // Receive intent extras
        if (getIntent() != null && getIntent().hasExtra("selected_role")) {
            selectedRole = getIntent().getStringExtra("selected_role");
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupSpecialtySection();
        setupListeners();
    }

    private void setupSpecialtySection() {
        if ("Voluntario".equals(selectedRole)) {
            binding.llSpecialtySection.setVisibility(View.VISIBLE);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, specialties);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerSpecialty.setAdapter(adapter);

            binding.spinnerSpecialty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TransitionManager.beginDelayedTransition(binding.llSpecialtySection);
                    if (position == 0) {
                        binding.cvSubareasPanel.setVisibility(View.GONE);
                    } else {
                        String selected = specialties[position];
                        binding.tvSubareasContent.setText(subareasMap.get(selected));
                        binding.cvSubareasPanel.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    binding.cvSubareasPanel.setVisibility(View.GONE);
                }
            });
        } else {
            binding.llSpecialtySection.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
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

        binding.btnRegister.setOnClickListener(v -> performRegistration());
        
        binding.llLoginLink.setOnClickListener(v -> {
            // Already came from MainActivity, so we just finish this activity
            finish();
        });
    }

    private void performRegistration() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etConfirmPassword.getText().toString();
        String specialty = null;

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Por favor llena todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("Voluntario".equals(selectedRole)) {
            if (binding.spinnerSpecialty.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Por favor selecciona una especialidad.", Toast.LENGTH_SHORT).show();
                return;
            }
            specialty = binding.spinnerSpecialty.getSelectedItem().toString();
        }

        if (password.length() < 8) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!binding.cbTerms.isChecked()) {
            Toast.makeText(this, "Debes aceptar los términos y condiciones.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Database checks
        if (!dbHelper.isEmailAvailable(email)) {
            Toast.makeText(this, "El correo introducido ya está registrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.registerUser(name, email, password, selectedRole, specialty);

        if (success) {
            Toast.makeText(this, "Usuario registrado exitosamente como " + selectedRole, Toast.LENGTH_LONG).show();
            // Volver directamente al Login limpiando la pila (RoleSelection se cierra también)
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error al crear la cuenta. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }
}
