package co.edu.aulasenora;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import co.edu.aulasenora.databinding.ActivityManageAulasBinding;
import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.models.Aula;

public class ManageAulasActivity extends AppCompatActivity {

    private ActivityManageAulasBinding binding;
    private DatabaseHelper dbHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityManageAulasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("user_email");

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        setupListeners();
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> finish());
        
        binding.fabCreateAula.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateAulaActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAulas();
    }

    private void loadAulas() {
        if (userEmail == null) return;
        
        List<Aula> aulas = dbHelper.getAulasByVolunteer(userEmail);
        
        // Clear all views EXCEPT the empty state which is part of the layout
        binding.llAulasList.removeAllViews();
        binding.llAulasList.addView(binding.llEmptyState);
        
        if (aulas.isEmpty()) {
            binding.llEmptyState.setVisibility(View.VISIBLE);
        } else {
            binding.llEmptyState.setVisibility(View.GONE);
            
            LayoutInflater inflater = LayoutInflater.from(this);
            for (Aula aula : aulas) {
                View cardView = inflater.inflate(R.layout.item_aula_card, binding.llAulasList, false);
                
                TextView tvName = cardView.findViewById(R.id.tvAulaName);
                TextView tvSubject = cardView.findViewById(R.id.tvAulaSubject);
                TextView tvDescription = cardView.findViewById(R.id.tvAulaDescription);
                TextView tvDate = cardView.findViewById(R.id.tvAulaDate);
                Button btnManage = cardView.findViewById(R.id.btnManageAula);
                
                tvName.setText(aula.getName());
                tvSubject.setText(aula.getSubject());
                tvDescription.setText(aula.getDescription());
                
                // Format the date slightly if possible, or just display raw
                String rawDate = aula.getCreatedAt();
                if (rawDate != null && rawDate.length() > 10) {
                    // Extract just the date part YYYY-MM-DD
                    tvDate.setText("Creado: " + rawDate.substring(0, 10));
                } else {
                    tvDate.setText("Creado: " + rawDate);
                }
                
                btnManage.setOnClickListener(v -> {
                    Toast.makeText(this, "Gestionar aula: " + aula.getName() + " (En construcción)", Toast.LENGTH_SHORT).show();
                });
                
                binding.llAulasList.addView(cardView);
            }
        }
    }
}
