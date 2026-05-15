package co.edu.aulasenora;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.databinding.ActivityHomeBinding;
import android.util.Log;
import co.edu.aulasenora.api.ApiClient;
import co.edu.aulasenora.models.RandomUserResponse;
import co.edu.aulasenora.models.User;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import java.util.List;
import co.edu.aulasenora.adapters.TutorAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "API_TEST";
    private ActivityHomeBinding binding;
    private DatabaseHelper dbHelper;
    private TutorAdapter tutorAdapter;

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
        
        // Configurar RecyclerView
        RecyclerView rvTutors = findViewById(R.id.rvTutors);
        rvTutors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tutorAdapter = new TutorAdapter();
        rvTutors.setAdapter(tutorAdapter);

        // Ejecutar prueba de la API al iniciar la pantalla
        fetchRandomUsers();
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

    // Método de prueba para ejecutar la API y ver los resultados en la consola
    private void fetchRandomUsers() {
        try {
            // Pediremos 5 usuarios con nacionalidad US o GB
            ApiClient.getService().getRandomUsers(5, "us,gb").enqueue(new Callback<RandomUserResponse>() {
                @Override
                public void onResponse(Call<RandomUserResponse> call, Response<RandomUserResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<User> users = response.body().getResults();
                        
                        // Pasar datos al adapter
                        tutorAdapter.setTutors(users);
                        
                        // Mostrar la sección ahora que tenemos datos
                        findViewById(R.id.sectionTutors).setVisibility(View.VISIBLE);
                        
                    } else {
                        String error = "Error HTTP: " + response.code();
                        Log.e(TAG, error);
                        Toast.makeText(HomeActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RandomUserResponse> call, Throwable t) {
                    String error = "Fallo de red: " + t.getMessage();
                    Log.e(TAG, error, t);
                    Toast.makeText(HomeActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            String error = "Excepcion: " + e.getMessage();
            Log.e(TAG, error, e);
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }
}
