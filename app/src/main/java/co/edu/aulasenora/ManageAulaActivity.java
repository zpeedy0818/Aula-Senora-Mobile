package co.edu.aulasenora;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import co.edu.aulasenora.databinding.ActivityManageAulaBinding;
import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.models.AccessRequest;
import co.edu.aulasenora.models.AdmittedStudent;
import co.edu.aulasenora.models.Aula;
import co.edu.aulasenora.models.ScheduleSlot;
import co.edu.aulasenora.models.SupportMaterial;
import co.edu.aulasenora.models.TutoringRequest;

public class ManageAulaActivity extends AppCompatActivity {

    private ActivityManageAulaBinding binding;
    private DatabaseHelper dbHelper;
    private String userEmail;
    private int aulaId;
    private String aulaName;

    private boolean showingAllTutoring = false;

    private static final int FILE_PICK_REQUEST_CODE = 1001;
    private Uri pendingFileUri;
    private String pendingFileName;
    private String pendingMimeType;
    private long pendingFileSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityManageAulaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("user_email");
        aulaId = getIntent().getIntExtra("aula_id", -1);

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        binding.includeHeader.btnBack.setOnClickListener(v -> finish());
        binding.includeHeader.headerBar.setBackgroundColor(getColor(R.color.volunteerPrimary));

        if (aulaId != -1) {
            List<Aula> allAulas = dbHelper.getAllAulas();
            for (Aula a : allAulas) {
                if (a.getId() == aulaId) {
                    aulaName = a.getName();
                    binding.includeHeader.tvTitle.setText("Gestionar: " + aulaName);
                    break;
                }
            }
        }

        binding.btnViewAllTutoring.setOnClickListener(v -> {
            showingAllTutoring = !showingAllTutoring;
            loadUpcomingTutoringSessions();
            binding.btnViewAllTutoring.setText(showingAllTutoring ? "Ver menos" : "Ver todos");
        });

        binding.btnUploadMaterial.setOnClickListener(v -> pickFile());

        binding.cardCalendar.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScheduleManagementActivity.class);
            intent.putExtra("aula_id", aulaId);
            intent.putExtra("user_email", userEmail);
            intent.putExtra("aula_name", aulaName);
            startActivity(intent);
        });

        binding.llChatButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatDetailActivity.class);
            intent.putExtra("aula_id", aulaId);
            intent.putExtra("user_email", userEmail);
            intent.putExtra("aula_name", aulaName);
            startActivity(intent);
        });
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
                showUploadNameDialog();
            }
        }
    }

    private void showUploadNameDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_upload_material, null);

        TextView tvFileName = dialogView.findViewById(R.id.tvSelectedFileName);
        com.google.android.material.textfield.TextInputEditText etCustomName =
                dialogView.findViewById(R.id.etCustomName);
        Button btnSave = dialogView.findViewById(R.id.btnSaveUpload);
        Button btnCancel = dialogView.findViewById(R.id.btnCancelUpload);

        tvFileName.setText("Archivo seleccionado: " + pendingFileName);
        etCustomName.setText(pendingFileName);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnSave.setOnClickListener(v -> {
            String customName = etCustomName.getText().toString().trim();
            if (customName.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }
            saveUploadedFile(customName);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void saveUploadedFile(String customName) {
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
                loadSupportMaterials();
            } else {
                Toast.makeText(this, "Error al guardar el material", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al subir el archivo", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (aulaId == -1) return;
        showingAllTutoring = false;
        binding.btnViewAllTutoring.setText("Ver todos");
        loadAdmittedStudents();
        loadUpcomingTutoringSessions();
        loadTutoringRequests();
        loadAccessRequests();
        loadSupportMaterials();
        loadChatUnreadBadge();
    }

    // ===== ADMITTED STUDENTS =====

    private void loadAdmittedStudents() {
        List<AdmittedStudent> students = dbHelper.getAdmittedStudents(aulaId);
        binding.llAdmittedStudents.removeAllViews();

        if (students.isEmpty()) {
            binding.tvEmptyAdmitted.setVisibility(View.VISIBLE);
            binding.tvAdmittedCount.setText("0");
            return;
        }
        binding.tvEmptyAdmitted.setVisibility(View.GONE);
        binding.tvAdmittedCount.setText(String.valueOf(students.size()));

        LayoutInflater inflater = LayoutInflater.from(this);
        for (AdmittedStudent student : students) {
            View itemView = inflater.inflate(R.layout.item_admitted_student, binding.llAdmittedStudents, false);

            TextView tvInitial = itemView.findViewById(R.id.tvStudentInitial);
            TextView tvName = itemView.findViewById(R.id.tvStudentName);
            TextView tvEmail = itemView.findViewById(R.id.tvStudentEmail);
            TextView tvDate = itemView.findViewById(R.id.tvAdmissionDate);

            String name = student.getName();
            tvInitial.setText(name != null && !name.isEmpty() ? String.valueOf(name.charAt(0)).toUpperCase() : "?");
            tvName.setText(name);
            tvEmail.setText(student.getEmail());

            String rawDate = student.getAdmissionDate();
            if (rawDate != null && rawDate.length() > 10) {
                tvDate.setText("Ingresó: " + rawDate.substring(0, 10));
            } else {
                tvDate.setText("Ingresó: " + rawDate);
            }

            binding.llAdmittedStudents.addView(itemView);
        }
    }

    // ===== (schedule slots moved to ScheduleManagementActivity) =====

    // ===== UPCOMING TUTORING SESSIONS =====

    private void loadUpcomingTutoringSessions() {
        List<ScheduleSlot> sessions = showingAllTutoring
                ? dbHelper.getAllUpcomingTutoringSessions(aulaId)
                : dbHelper.getUpcomingTutoringSessions(aulaId, 3);
        binding.llPendingTutoring.removeAllViews();

        if (sessions.isEmpty()) {
            binding.tvEmptyPendingTutoring.setVisibility(View.VISIBLE);
            return;
        }
        binding.tvEmptyPendingTutoring.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < sessions.size(); i++) {
            ScheduleSlot session = sessions.get(i);

            View itemView = inflater.inflate(R.layout.item_schedule_slot, binding.llPendingTutoring, false);

            TextView tvDate = itemView.findViewById(R.id.tvSlotDate);
            TextView tvTime = itemView.findViewById(R.id.tvSlotTime);
            TextView tvTopic = itemView.findViewById(R.id.tvSlotTopic);
            TextView tvTarget = itemView.findViewById(R.id.tvSlotTarget);
            TextView tvBadge = itemView.findViewById(R.id.tvSlotBadge);
            TextView btnDelete = itemView.findViewById(R.id.btnDeleteSlot);

            String rawDate = session.getSlotDate();
            if (rawDate != null && rawDate.length() == 10) {
                String[] parts = rawDate.split("-");
                tvDate.setText("#" + (i + 1) + " - " + parts[2] + "/" + parts[1] + "/" + parts[0]);
            } else {
                tvDate.setText("#" + (i + 1) + " - " + rawDate);
            }

            tvTime.setText(session.getStartTime() + " - " + session.getEndTime());

            tvBadge.setBackgroundColor(getColor(R.color.badgeGreen));
            tvBadge.setText("Tutoría");

            if (session.getTopic() != null && !session.getTopic().isEmpty()) {
                tvTopic.setVisibility(View.VISIBLE);
                tvTopic.setText("Tema: " + session.getTopic());
            }

            if (session.getTargetStudentEmail() != null && !session.getTargetStudentEmail().isEmpty()) {
                tvTarget.setVisibility(View.VISIBLE);
                String targetName = session.getTargetStudentName();
                if (targetName != null && !targetName.isEmpty()) {
                    tvTarget.setText("Para: " + targetName);
                } else {
                    tvTarget.setText("Para: " + session.getTargetStudentEmail());
                }
            } else if (session.getTargetStudentEmail() == null) {
                tvTarget.setVisibility(View.VISIBLE);
                tvTarget.setText("Para: Todos los estudiantes");
            }

            btnDelete.setVisibility(View.GONE);

            binding.llPendingTutoring.addView(itemView);
        }
    }

    // ===== TUTORING REQUESTS (existing) =====

    private void loadTutoringRequests() {
        List<TutoringRequest> requests = dbHelper.getTutoringRequestsForAula(aulaId);
        binding.llTutoringRequests.removeAllViews();
        binding.cvEmptyTutoringRequests.setVisibility(View.GONE);

        if (requests.isEmpty()) {
            binding.cvEmptyTutoringRequests.setVisibility(View.VISIBLE);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        for (TutoringRequest req : requests) {
            View cardView = inflater.inflate(R.layout.item_tutoring_request, binding.llTutoringRequests, false);

            TextView tvStudentName = cardView.findViewById(R.id.tvStudentName);
            TextView tvTopic = cardView.findViewById(R.id.tvTutoringTopic);
            TextView tvDescription = cardView.findViewById(R.id.tvTutoringDescription);
            TextView tvDateTime = cardView.findViewById(R.id.tvPreferredDateTime);
            Button btnAccept = cardView.findViewById(R.id.btnAcceptTutoring);
            Button btnDeny = cardView.findViewById(R.id.btnDenyTutoring);

            tvStudentName.setText(req.getStudentName());
            tvTopic.setText("Tema: " + req.getTopic());
            tvDescription.setText(req.getDescription());

            String dateTime = "";
            if (req.getPreferredDate() != null && !req.getPreferredDate().isEmpty()) {
                dateTime += req.getPreferredDate();
            }
            if (req.getPreferredTime() != null && !req.getPreferredTime().isEmpty()) {
                dateTime += " a las " + req.getPreferredTime();
            }
            if (!dateTime.isEmpty()) {
                tvDateTime.setText("Preferencia: " + dateTime);
            } else {
                tvDateTime.setVisibility(View.GONE);
            }

            btnAccept.setOnClickListener(v -> {
                dbHelper.updateTutoringRequestStatus(req.getId(), "approved");

                String endTime = req.getPreferredEndTime();
                if (endTime == null || endTime.isEmpty()) {
                    String[] parts = req.getPreferredTime().split(":");
                    int hour = Integer.parseInt(parts[0]);
                    int minute = Integer.parseInt(parts[1]);
                    hour = (hour + 1) % 24;
                    endTime = String.format("%02d:%02d", hour, minute);
                }

                dbHelper.createScheduleSlot(aulaId, userEmail, req.getPreferredDate(),
                        req.getPreferredTime(), endTime, "tutoring",
                        req.getTopic(), req.getStudentEmail());

                Toast.makeText(this, "Tutoría de " + req.getStudentName() + " aceptada", Toast.LENGTH_SHORT).show();
                loadTutoringRequests();
                loadUpcomingTutoringSessions();
            });

            btnDeny.setOnClickListener(v -> {
                dbHelper.updateTutoringRequestStatus(req.getId(), "rejected");
                Toast.makeText(this, "Tutoría de " + req.getStudentName() + " rechazada", Toast.LENGTH_SHORT).show();
                loadTutoringRequests();
            });

            binding.llTutoringRequests.addView(cardView);
        }
    }

    // ===== ACCESS REQUESTS (existing) =====

    private void loadAccessRequests() {
        List<AccessRequest> requests = dbHelper.getPendingAccessRequestsForAula(aulaId);
        binding.llAccessRequests.removeAllViews();
        binding.cvEmptyAccessRequests.setVisibility(View.GONE);

        if (requests.isEmpty()) {
            binding.cvEmptyAccessRequests.setVisibility(View.VISIBLE);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        for (AccessRequest req : requests) {
            View cardView = inflater.inflate(R.layout.item_access_request, binding.llAccessRequests, false);

            TextView tvStudentName = cardView.findViewById(R.id.tvStudentName);
            TextView tvAulaName = cardView.findViewById(R.id.tvRequestAulaName);
            Button btnAccept = cardView.findViewById(R.id.btnAcceptRequest);
            Button btnDeny = cardView.findViewById(R.id.btnDenyRequest);

            tvStudentName.setText(req.getStudentName());
            tvAulaName.setText("Solicita acceso al aula");

            btnAccept.setOnClickListener(v -> {
                dbHelper.updateAccessRequestStatus(req.getId(), "approved");
                Toast.makeText(this, "Solicitud de " + req.getStudentName() + " aceptada", Toast.LENGTH_SHORT).show();
                loadAdmittedStudents();
                loadAccessRequests();
            });

            btnDeny.setOnClickListener(v -> {
                dbHelper.updateAccessRequestStatus(req.getId(), "rejected");
                Toast.makeText(this, "Solicitud de " + req.getStudentName() + " denegada", Toast.LENGTH_SHORT).show();
                loadAccessRequests();
            });

            binding.llAccessRequests.addView(cardView);
        }
    }

    // ===== CHAT UNREAD BADGE =====

    private void loadChatUnreadBadge() {
        int unread = dbHelper.getUnreadCount(aulaId, userEmail);
        if (unread > 0) {
            binding.badgeChatUnread.setVisibility(View.VISIBLE);
        } else {
            binding.badgeChatUnread.setVisibility(View.GONE);
        }
    }

    // ===== SUPPORT MATERIALS =====

    private void loadSupportMaterials() {
        List<SupportMaterial> materials = dbHelper.getSupportMaterials(aulaId);
        binding.llMaterials.removeAllViews();

        if (materials.isEmpty()) {
            binding.tvEmptyMaterials.setVisibility(View.VISIBLE);
            return;
        }
        binding.tvEmptyMaterials.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (SupportMaterial material : materials) {
            View itemView = inflater.inflate(R.layout.item_support_material, binding.llMaterials, false);

            TextView tvCustomName = itemView.findViewById(R.id.tvCustomName);
            TextView tvUploadDate = itemView.findViewById(R.id.tvUploadDate);
            TextView btnAction = itemView.findViewById(R.id.btnMaterialAction);
            LinearLayout llViewCount = itemView.findViewById(R.id.llViewCount);
            TextView tvViewCount = itemView.findViewById(R.id.tvViewCount);

            tvCustomName.setText(material.getCustomName());

            String rawDate = material.getCreatedAt();
            if (rawDate != null && rawDate.length() >= 10) {
                String datePart = rawDate.substring(0, 10);
                String[] parts = datePart.split("-");
                tvUploadDate.setText(parts[2] + "/" + parts[1] + "/" + parts[0]);
            } else {
                tvUploadDate.setText(rawDate);
            }

            int vc = material.getViewCount();
            tvViewCount.setText(vc + " " + (vc == 1 ? "estudiante" : "estudiantes"));
            llViewCount.setVisibility(View.VISIBLE);

            btnAction.setText("Eliminar");
            btnAction.setBackgroundColor(getColor(R.color.badgeRed));
            btnAction.setOnClickListener(v -> {
                File file = new File(material.getFilePath());
                if (file.exists()) file.delete();
                dbHelper.deleteSupportMaterial(material.getId());
                Toast.makeText(this, "Material eliminado", Toast.LENGTH_SHORT).show();
                loadSupportMaterials();
            });

            binding.llMaterials.addView(itemView);
        }
    }
}
