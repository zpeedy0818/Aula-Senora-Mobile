package co.edu.aulasenora;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import co.edu.aulasenora.databinding.ActivityAdminDashboardBinding;
import co.edu.aulasenora.db.DatabaseHelper;

public class AdminDashboardActivity extends AppCompatActivity {

    private ActivityAdminDashboardBinding binding;
    private DatabaseHelper dbHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
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
            loadStats();
        }

        setupListeners();
    }

    private void loadStats() {
        int totalUsers = dbHelper.getRoleCount("Estudiante") + dbHelper.getRoleCount("Voluntario") + 1;
        int volunteers = dbHelper.getRoleCount("Voluntario");
        int activeTutorings = dbHelper.getAulasCount();

        binding.tvTotalUsers.setText(String.valueOf(totalUsers));
        binding.tvVolunteers.setText(String.valueOf(volunteers));
        binding.tvActiveTutorings.setText(String.valueOf(activeTutorings));
    }

    private void setupListeners() {
        binding.btnNotifications.setOnClickListener(v ->
                Toast.makeText(this, "Notificaciones (En construcción)", Toast.LENGTH_SHORT).show()
        );

        binding.btnViewAllUsers.setOnClickListener(v ->
                Toast.makeText(this, "Ver todos los usuarios (En construcción)", Toast.LENGTH_SHORT).show()
        );

        binding.cardUser1.setOnClickListener(v ->
                Toast.makeText(this, "Perfil de Laura García (En construcción)", Toast.LENGTH_SHORT).show()
        );

        binding.cardUser2.setOnClickListener(v ->
                Toast.makeText(this, "Perfil de Pedro Sánchez (En construcción)", Toast.LENGTH_SHORT).show()
        );

        binding.btnResolve1.setOnClickListener(v ->
                Toast.makeText(this, "Reporte de conducta resuelto", Toast.LENGTH_SHORT).show()
        );

        binding.btnDismiss1.setOnClickListener(v ->
                Toast.makeText(this, "Reporte de conducta ignorado", Toast.LENGTH_SHORT).show()
        );

        binding.btnResolve2.setOnClickListener(v ->
                Toast.makeText(this, "Contenido resuelto", Toast.LENGTH_SHORT).show()
        );

        binding.btnDismiss2.setOnClickListener(v ->
                Toast.makeText(this, "Contenido ignorado", Toast.LENGTH_SHORT).show()
        );

        binding.btnConfigUsers.setOnClickListener(v ->
                Toast.makeText(this, "Gestión de usuarios (En construcción)", Toast.LENGTH_SHORT).show()
        );

        binding.btnConfigReports.setOnClickListener(v ->
                Toast.makeText(this, "Reportes del sistema (En construcción)", Toast.LENGTH_SHORT).show()
        );

        binding.btnConfigStats.setOnClickListener(v -> {
                Intent intent = new Intent(this, StatsActivity.class);
                startActivity(intent);
        });

        binding.btnConfigSettings.setOnClickListener(v ->
                Toast.makeText(this, "Ajustes del sistema (En construcción)", Toast.LENGTH_SHORT).show()
        );

        binding.navInicio.setOnClickListener(v ->
                Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show()
        );

        binding.navHistorial.setOnClickListener(v ->
                Toast.makeText(this, "Historial (En construcción)", Toast.LENGTH_SHORT).show()
        );

        binding.navPerfil.setOnClickListener(v ->
                Toast.makeText(this, "Perfil (En construcción)", Toast.LENGTH_SHORT).show()
        );

        binding.navAdmin.setOnClickListener(v ->
                Toast.makeText(this, "Ya estás en el panel de administración", Toast.LENGTH_SHORT).show()
        );
    }
}
