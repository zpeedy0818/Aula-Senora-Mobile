package co.edu.aulasenora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import co.edu.aulasenora.db.DatabaseHelper;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String userEmail;
    private boolean isVolunteer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("user_email");
        isVolunteer = getIntent().getBooleanExtra("is_volunteer", false);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Set header color based on role
        findViewById(R.id.headerBar).setBackgroundColor(
                ContextCompat.getColor(this, isVolunteer ? R.color.volunteerPrimary : R.color.primaryCyan)
        );

        // Avatar circle color
        findViewById(R.id.frameAvatar).getBackground().setTint(
                ContextCompat.getColor(this, isVolunteer ? R.color.primaryOrange : R.color.primaryCyan)
        );

        loadProfile();
    }

    private void loadProfile() {
        if (userEmail == null) return;

        String name = dbHelper.getUserName(userEmail);
        String role = dbHelper.getUserRoleByEmail(userEmail);
        String specialty = dbHelper.getUserSpecialty(userEmail);
        long timeSpent = dbHelper.getTimeSpent(userEmail);

        // Name
        if (name == null || name.isEmpty()) {
            name = userEmail.split("@")[0];
        }
        ((TextView) findViewById(R.id.tvProfileName)).setText(name);

        // Email
        ((TextView) findViewById(R.id.tvProfileEmail)).setText(userEmail);
        ((TextView) findViewById(R.id.tvInfoEmail)).setText(userEmail);

        // Initial
        String initial = name.substring(0, 1).toUpperCase();
        ((TextView) findViewById(R.id.tvAvatarInitial)).setText(initial);

        // Role badge
        String roleDisplay = "Voluntario".equals(role) ? "Voluntario" : "Estudiante";
        ((TextView) findViewById(R.id.tvProfileRole)).setText(roleDisplay);

        // Specialty (only for volunteers)
        if (isVolunteer && specialty != null && !specialty.isEmpty()) {
            findViewById(R.id.llSpecialty).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tvSpecialty)).setText(specialty);
        } else {
            findViewById(R.id.llSpecialty).setVisibility(View.GONE);
        }

        // Time spent
        long hours = timeSpent / 3600;
        long minutes = (timeSpent % 3600) / 60;
        ((TextView) findViewById(R.id.tvTimeSpent)).setText(
                "Tiempo en la app: " + hours + "h " + minutes + "m"
        );

        // Logout
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
