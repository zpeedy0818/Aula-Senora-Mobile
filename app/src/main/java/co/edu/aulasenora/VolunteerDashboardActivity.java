package co.edu.aulasenora;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import android.content.Intent;

import co.edu.aulasenora.databinding.ActivityVolunteerDashboardBinding;
import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.models.AccessRequest;
import co.edu.aulasenora.models.Aula;
import co.edu.aulasenora.models.ScheduleSlot;
import co.edu.aulasenora.models.SupportMaterial;

public class VolunteerDashboardActivity extends AppCompatActivity {

    private ActivityVolunteerDashboardBinding binding;
    private DatabaseHelper dbHelper;
    private String userEmail;
    private long totalSeconds = 0;
    private static final int FILE_PICK_REQUEST_CODE = 1002;
    private Uri pendingFileUri;
    private String pendingFileName;
    private String pendingMimeType;
    private long pendingFileSize;
    private Handler timerHandler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            totalSeconds++;
            updateHoursUI();
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityVolunteerDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        dbHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        // Set username in header from intent extra
        userEmail = getIntent().getStringExtra("user_email");
        if (userEmail != null) {
            String username = userEmail.split("@")[0];
            binding.tvWelcomeName.setText("Hola, " + username + " \uD83C\uDF93");
            
            // Start timer
            totalSeconds = dbHelper.getTimeSpent(userEmail);
            updateHoursUI();
            timerHandler.postDelayed(timerRunnable, 1000);
            
            loadAulas();
            loadNotifBadge();
        }

        // Setup placeholder click listeners for all interactive elements
        setupPlaceholderClicks();
    }

    private void setupPlaceholderClicks() {
        // Notification bell
        binding.btnNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(this, NotificationsActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });

        // Next session card - functionality moved to loadUpcomingSessions()

        // View all classes
        binding.btnViewAllClasses.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageAulasActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });

        // Create aula placeholder
        binding.btnCreateAulaPlaceholder.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateAulaActivity.class);
            intent.putExtra("user_email", userEmail);
            startActivity(intent);
        });

        // Active tutoring card - functionality moved to loadActiveTutoring()

        // Add resource button
        binding.btnAddResource.setOnClickListener(v -> pickFile());

        // Ver todas solicitudes
        binding.btnViewAllRequests.setOnClickListener(v -> {
            List<AccessRequest> allRequests = dbHelper.getAllPendingRequestsForVolunteer(userEmail);
            displayAccessRequests(allRequests);
        });

        // Bottom navigation
        binding.navInicio.setOnClickListener(v ->
            Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show()
        );

        binding.navSolicitudes.setOnClickListener(v -> {
            binding.scrollView.post(() ->
                binding.scrollView.smoothScrollTo(0, binding.llRequestsSection.getTop()));
            loadAccessRequests();
        });

        binding.navPerfil.setOnClickListener(v ->
            Toast.makeText(this, "Perfil (En construcción)", Toast.LENGTH_SHORT).show()
        );

        binding.navChat.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatListActivity.class);
            intent.putExtra("user_email", userEmail);
            intent.putExtra("is_volunteer", true);
            startActivity(intent);
        });
    }

    private void loadAccessRequests() {
        List<AccessRequest> requests = dbHelper.getPendingRequestsForVolunteer(userEmail, 3);
        displayAccessRequests(requests);
    }

    private void displayAccessRequests(List<AccessRequest> requests) {
        binding.llRequestsContainer.removeAllViews();
        binding.cvEmptyRequests.setVisibility(View.GONE);

        if (requests.isEmpty()) {
            binding.cvEmptyRequests.setVisibility(View.VISIBLE);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        for (AccessRequest req : requests) {
            View cardView = inflater.inflate(R.layout.item_access_request, binding.llRequestsContainer, false);

            TextView tvStudentName = cardView.findViewById(R.id.tvStudentName);
            TextView tvAulaName = cardView.findViewById(R.id.tvRequestAulaName);
            Button btnAccept = cardView.findViewById(R.id.btnAcceptRequest);
            Button btnDeny = cardView.findViewById(R.id.btnDenyRequest);

            tvStudentName.setText(req.getStudentName());
            tvAulaName.setText("Solicita acceso a: " + req.getAulaName());

            btnAccept.setOnClickListener(v -> {
                dbHelper.updateAccessRequestStatus(req.getId(), "approved");
                Toast.makeText(this, "Solicitud de " + req.getStudentName() + " aceptada", Toast.LENGTH_SHORT).show();
                loadAccessRequests();
            });

            btnDeny.setOnClickListener(v -> {
                dbHelper.updateAccessRequestStatus(req.getId(), "rejected");
                Toast.makeText(this, "Solicitud de " + req.getStudentName() + " denegada", Toast.LENGTH_SHORT).show();
                loadAccessRequests();
            });

            binding.llRequestsContainer.addView(cardView);
        }
    }

    private void loadAulas() {
        if (userEmail == null) return;
        
        // Remove dynamic cards to avoid duplicates, keeping the empty state
        int childCount = binding.llAulasContainer.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            android.view.View child = binding.llAulasContainer.getChildAt(i);
            if (child.getId() != R.id.cvEmptyAulas) {
                binding.llAulasContainer.removeViewAt(i);
            }
        }
        
        List<Aula> aulas = dbHelper.getRecentAulasByVolunteer(userEmail, 2);
        
        if (aulas.isEmpty()) {
            binding.cvEmptyAulas.setVisibility(android.view.View.VISIBLE);
        } else {
            binding.cvEmptyAulas.setVisibility(android.view.View.GONE);
            
            android.view.LayoutInflater inflater = android.view.LayoutInflater.from(this);
            for (Aula aula : aulas) {
                android.view.View cardView = inflater.inflate(R.layout.item_aula_card, binding.llAulasContainer, false);
                
                android.widget.TextView tvName = cardView.findViewById(R.id.tvAulaName);
                android.widget.TextView tvSubject = cardView.findViewById(R.id.tvAulaSubject);
                android.widget.TextView tvDescription = cardView.findViewById(R.id.tvAulaDescription);
                android.widget.TextView tvDate = cardView.findViewById(R.id.tvAulaDate);
                android.widget.Button btnManage = cardView.findViewById(R.id.btnManageAula);
                
                tvName.setText(aula.getName());
                tvSubject.setText(aula.getSubject());
                tvDescription.setText(aula.getDescription());
                
                String rawDate = aula.getCreatedAt();
                if (rawDate != null && rawDate.length() > 10) {
                    tvDate.setText("Creado: " + rawDate.substring(0, 10));
                } else {
                    tvDate.setText("Creado: " + rawDate);
                }
                
                btnManage.setOnClickListener(v -> {
                    android.content.Intent intent = new android.content.Intent(VolunteerDashboardActivity.this, ManageAulaActivity.class);
                    intent.putExtra("aula_id", aula.getId());
                    intent.putExtra("user_email", userEmail);
                    startActivity(intent);
                });
                
                binding.llAulasContainer.addView(cardView);
            }
        }
    }

    private void loadResources() {
        if (userEmail == null) return;
        List<SupportMaterial> materials = dbHelper.getRecentMaterialsByVolunteer(userEmail, 2);
        binding.llResourcesContainer.removeAllViews();

        if (materials.isEmpty()) {
            binding.llResourcesContainer.setVisibility(View.GONE);
            binding.tvEmptyResources.setVisibility(View.VISIBLE);
            return;
        }
        binding.llResourcesContainer.setVisibility(View.VISIBLE);
        binding.tvEmptyResources.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (SupportMaterial material : materials) {
            View cardView = inflater.inflate(R.layout.item_resource_card, binding.llResourcesContainer, false);

            TextView tvName = cardView.findViewById(R.id.tvResourceName);
            TextView tvInfo = cardView.findViewById(R.id.tvResourceInfo);

            tvName.setText(material.getCustomName());

            String rawDate = material.getCreatedAt();
            String dateStr;
            if (rawDate != null && rawDate.length() >= 10) {
                String[] parts = rawDate.substring(0, 10).split("-");
                dateStr = parts[2] + "/" + parts[1] + "/" + parts[0];
            } else {
                dateStr = rawDate;
            }
            tvInfo.setText(material.getViewCount() + " estudiantes  •  " + dateStr);

            binding.llResourcesContainer.addView(cardView);
        }
    }

    private void loadActiveTutoring() {
        List<ScheduleSlot> sessions = dbHelper.getTodayTutoringForVolunteer(userEmail);
        binding.llActiveTutoringContainer.removeAllViews();

        if (sessions.isEmpty()) {
            binding.llActiveTutoringContainer.setVisibility(View.GONE);
            return;
        }
        binding.llActiveTutoringContainer.setVisibility(View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (ScheduleSlot slot : sessions) {
            View cardView = inflater.inflate(R.layout.item_active_tutoring_card, binding.llActiveTutoringContainer, false);

            TextView tvTitle = cardView.findViewById(R.id.tvTutoringTitle);
            TextView tvTopic = cardView.findViewById(R.id.tvTutoringTopic);
            TextView tvTime = cardView.findViewById(R.id.tvTutoringTime);

            boolean hasTargetStudent = slot.getTargetStudentEmail() != null && !slot.getTargetStudentEmail().isEmpty();
            if (hasTargetStudent) {
                tvTitle.setText(slot.getTargetStudentName() != null ? slot.getTargetStudentName() : slot.getTargetStudentEmail());
            } else {
                tvTitle.setText(slot.getAulaName() != null ? slot.getAulaName() : "Aula " + slot.getAulaId());
            }

            tvTopic.setText(slot.getTopic() != null ? slot.getTopic() : "");
            String time = slot.getStartTime() != null && slot.getStartTime().length() >= 5
                    ? slot.getStartTime().substring(0, 5) : slot.getStartTime();
            tvTime.setText("Hoy " + time);

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(VolunteerDashboardActivity.this, ManageAulaActivity.class);
                intent.putExtra("aula_id", slot.getAulaId());
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
            });

            binding.llActiveTutoringContainer.addView(cardView);
        }
    }

    private void loadUpcomingSessions() {
        List<ScheduleSlot> sessions = dbHelper.getUpcomingTutoringForVolunteer(userEmail, 2);
        binding.llUpcomingContainer.removeAllViews();

        if (sessions.isEmpty()) {
            binding.llUpcomingContainer.setVisibility(View.GONE);
            return;
        }
        binding.llUpcomingContainer.setVisibility(View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (ScheduleSlot slot : sessions) {
            View cardView = inflater.inflate(R.layout.item_upcoming_session_card, binding.llUpcomingContainer, false);

            TextView tvAulaName = cardView.findViewById(R.id.tvUpcomingAulaName);
            TextView tvDate = cardView.findViewById(R.id.tvUpcomingDate);

            tvAulaName.setText(slot.getAulaName() != null ? slot.getAulaName() : "Aula " + slot.getAulaId());
            tvDate.setText(slot.getSlotDate());

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(VolunteerDashboardActivity.this, ManageAulaActivity.class);
                intent.putExtra("aula_id", slot.getAulaId());
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
            });

            binding.llUpcomingContainer.addView(cardView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userEmail != null) {
            loadAulas();
            loadAccessRequests();
            loadUpcomingSessions();
            loadActiveTutoring();
            loadResources();
            loadNotifBadge();
        }
    }

    private void loadNotifBadge() {
        int total = 0;
        total += dbHelper.getPendingAccessNotifications(userEmail).size();
        total += dbHelper.getPendingTutoringNotifications(userEmail).size();
        total += dbHelper.getUnreadChatNotifications(userEmail).size();
        binding.badgeNotifUnread.setVisibility(total > 0 ? View.VISIBLE : View.GONE);
    }

    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_PICK_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            pendingFileUri = data.getData();
            if (pendingFileUri != null) {
                try (android.database.Cursor cursor = getContentResolver().query(pendingFileUri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                        pendingFileName = nameIndex >= 0 ? cursor.getString(nameIndex) : "archivo";
                        pendingFileSize = sizeIndex >= 0 ? cursor.getLong(sizeIndex) : 0;
                    }
                }
                pendingMimeType = getContentResolver().getType(pendingFileUri);
                if (pendingMimeType == null) pendingMimeType = "*/*";
                showUploadDialog();
            }
        }
    }

    private void showUploadDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_upload_resource, null);

        TextView tvFileName = dialogView.findViewById(R.id.tvSelectedFileName);
        Spinner spAula = dialogView.findViewById(R.id.spAulaSelector);
        TextInputEditText etCustomName = dialogView.findViewById(R.id.etCustomName);
        Button btnSave = dialogView.findViewById(R.id.btnSaveUpload);
        Button btnCancel = dialogView.findViewById(R.id.btnCancelUpload);

        tvFileName.setText("Archivo seleccionado: " + pendingFileName);
        etCustomName.setText(pendingFileName);

        List<Aula> aulas = dbHelper.getAulasByVolunteer(userEmail);
        String[] aulaNames = new String[aulas.size()];
        final int[] aulaIds = new int[aulas.size()];
        for (int i = 0; i < aulas.size(); i++) {
            aulaNames[i] = aulas.get(i).getName();
            aulaIds[i] = aulas.get(i).getId();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aulaNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAula.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnSave.setOnClickListener(v -> {
            String customName = etCustomName.getText().toString().trim();
            if (customName.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }
            if (aulas.isEmpty()) {
                Toast.makeText(this, "No tienes aulas para subir material", Toast.LENGTH_SHORT).show();
                return;
            }
            int selectedPosition = spAula.getSelectedItemPosition();
            int selectedAulaId = aulaIds[selectedPosition];
            saveUploadedFile(customName, selectedAulaId);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void saveUploadedFile(String customName, int aulaId) {
        try {
            File dir = new File(getFilesDir(), "materials/aula_" + aulaId);
            if (!dir.exists()) dir.mkdirs();

            String uniqueName = UUID.randomUUID().toString() + "_" + pendingFileName;
            File destFile = new File(dir, uniqueName);

            try (InputStream in = getContentResolver().openInputStream(pendingFileUri);
                 FileOutputStream out = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            long result = dbHelper.createSupportMaterial(aulaId, userEmail, customName,
                    pendingFileName, destFile.getAbsolutePath(), pendingMimeType, pendingFileSize);
            if (result != -1) {
                Toast.makeText(this, "Material subido", Toast.LENGTH_SHORT).show();
                loadResources();
            } else {
                Toast.makeText(this, "Error al guardar el material", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al subir el archivo", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateHoursUI() {
        long hours = totalSeconds / 3600;
        binding.tvDonatedHours.setText(String.valueOf(hours));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (userEmail != null) {
            dbHelper.updateTimeSpent(userEmail, totalSeconds);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
        if (userEmail != null) {
            dbHelper.updateTimeSpent(userEmail, totalSeconds);
        }
    }
}
