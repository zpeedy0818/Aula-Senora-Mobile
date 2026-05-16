package co.edu.aulasenora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.aulasenora.databinding.ActivityCreateAulaBinding;
import co.edu.aulasenora.db.DatabaseHelper;

public class CreateAulaActivity extends AppCompatActivity {

    private ActivityCreateAulaBinding binding;
    private DatabaseHelper dbHelper;
    private String userEmail;
    private String userSpecialty;
    private List<String> currentSubjects = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    
    private final Map<String, List<String>> specialtyMap = new HashMap<String, List<String>>() {{
        put("Matemáticas", Arrays.asList("Álgebra", "Geometría", "Cálculo", "Estadística"));
        put("Ciencias", Arrays.asList("Física", "Química", "Biología", "Astronomía"));
        put("Lengua", Arrays.asList("Español", "Inglés", "Lingüística", "Literatura"));
        put("Sociales", Arrays.asList("Historia", "Geografía", "Sociología", "Economía"));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreateAulaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("user_email");

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        if (userEmail != null) {
            userSpecialty = dbHelper.getUserSpecialty(userEmail);
        }

        setupSpinner();
        setupListeners();
    }

    private void setupSpinner() {
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currentSubjects);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSubject.setAdapter(spinnerAdapter);

        loadInitialSubjects();

        binding.spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = currentSubjects.get(position);
                if ("Otras materias...".equals(selected)) {
                    loadAllSubjects();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadInitialSubjects() {
        currentSubjects.clear();
        currentSubjects.add("Selecciona submateria...");
        
        if (userSpecialty != null && specialtyMap.containsKey(userSpecialty)) {
            currentSubjects.addAll(specialtyMap.get(userSpecialty));
        } else {
            // Fallback just in case
            loadAllSubjectsInternal();
            return;
        }
        
        currentSubjects.add("Otras materias...");
        spinnerAdapter.notifyDataSetChanged();
        binding.spinnerSubject.setSelection(0);
        binding.tvSpecialtyNotice.setText("Mostrando temas sugeridos para tu especialidad (" + userSpecialty + ")");
    }

    private void loadAllSubjectsInternal() {
        currentSubjects.clear();
        currentSubjects.add("Selecciona submateria...");
        for (List<String> subjects : specialtyMap.values()) {
            currentSubjects.addAll(subjects);
        }
    }

    private void loadAllSubjects() {
        loadAllSubjectsInternal();
        spinnerAdapter.notifyDataSetChanged();
        // Keep dropdown open or just select first after placeholder
        binding.spinnerSubject.setSelection(0);
        binding.spinnerSubject.performClick();
        binding.tvSpecialtyNotice.setText("Mostrando todas las materias disponibles");
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnCreate.setOnClickListener(v -> {
            String name = binding.etAulaName.getText().toString().trim();
            String description = binding.etAulaDescription.getText().toString().trim();
            
            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Por favor completa el nombre y la descripción", Toast.LENGTH_SHORT).show();
                return;
            }
            if (name.length() < 3) {
                Toast.makeText(this, "El nombre del aula debe tener al menos 3 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }
            if (description.length() < 10) {
                Toast.makeText(this, "La descripción debe tener al menos 10 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }
            
            int selectedPosition = binding.spinnerSubject.getSelectedItemPosition();
            if (selectedPosition == 0 || "Otras materias...".equals(currentSubjects.get(selectedPosition))) {
                Toast.makeText(this, "Por favor selecciona una materia específica", Toast.LENGTH_SHORT).show();
                return;
            }
            
            String subject = currentSubjects.get(selectedPosition);
            
            boolean success = dbHelper.createAula(name, description, subject, userEmail);
            if (success) {
                Toast.makeText(this, "¡Aula creada exitosamente!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Error al crear el aula. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
