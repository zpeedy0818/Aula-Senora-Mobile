package co.edu.aulasenora;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.List;

import co.edu.aulasenora.databinding.ActivityStudentAulaDetailBinding;
import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.models.Aula;
import co.edu.aulasenora.models.ScheduleSlot;
import co.edu.aulasenora.models.SupportMaterial;
import co.edu.aulasenora.models.TutoringRequest;

public class StudentAulaDetailActivity extends AppCompatActivity {

    private ActivityStudentAulaDetailBinding binding;
    private DatabaseHelper dbHelper;
    private String userEmail;
    private int aulaId;
    private Aula aula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityStudentAulaDetailBinding.inflate(getLayoutInflater());
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
        binding.includeHeader.headerBar.setBackgroundColor(getColor(R.color.studentPrimary));

        if (aulaId != -1) {
            aula = dbHelper.getAulaById(aulaId);
            if (aula != null) {
                binding.includeHeader.tvTitle.setText(aula.getName());
                binding.tvAulaName.setText(aula.getName());
                binding.tvAulaSubject.setText(aula.getSubject());
                binding.tvAulaDescription.setText(aula.getDescription());
                String volunteerName = dbHelper.getUserName(aula.getVolunteerEmail());
                if (volunteerName != null) {
                    binding.tvVolunteerName.setText("Por: " + volunteerName);
                }
            }
        }

        binding.btnRequestTutoring.setOnClickListener(v -> showRequestTutoringDialog());

        binding.llChatButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatDetailActivity.class);
            intent.putExtra("aula_id", aulaId);
            intent.putExtra("user_email", userEmail);
            intent.putExtra("aula_name", aula != null ? aula.getName() : "Chat");
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (aulaId == -1) return;
        loadScheduleSlots();
        loadTutoringSessions();
        loadSupportMaterials();
        loadMyPendingRequest();
        loadChatUnreadBadge();
    }

    private void loadScheduleSlots() {
        List<ScheduleSlot> slots = dbHelper.getScheduleSlotsForAula(aulaId);
        binding.llScheduleSlots.removeAllViews();

        if (slots.isEmpty()) {
            binding.tvEmptySchedule.setVisibility(View.VISIBLE);
            return;
        }
        binding.tvEmptySchedule.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (ScheduleSlot slot : slots) {
            View itemView = inflater.inflate(R.layout.item_schedule_slot, binding.llScheduleSlots, false);

            TextView tvDate = itemView.findViewById(R.id.tvSlotDate);
            TextView tvTime = itemView.findViewById(R.id.tvSlotTime);
            TextView tvTopic = itemView.findViewById(R.id.tvSlotTopic);
            TextView tvTarget = itemView.findViewById(R.id.tvSlotTarget);
            TextView tvBadge = itemView.findViewById(R.id.tvSlotBadge);
            TextView btnDelete = itemView.findViewById(R.id.btnDeleteSlot);

            btnDelete.setVisibility(View.GONE);

            String rawDate = slot.getSlotDate();
            if (rawDate != null && rawDate.length() == 10) {
                String[] parts = rawDate.split("-");
                tvDate.setText(parts[2] + "/" + parts[1] + "/" + parts[0]);
            } else {
                tvDate.setText(rawDate);
            }

            tvTime.setText(slot.getStartTime() + " - " + slot.getEndTime());

            if ("tutoring".equals(slot.getType())) {
                tvBadge.setBackgroundColor(getColor(R.color.badgeGreen));
                tvBadge.setText("Tutoría");

                if (slot.getTopic() != null && !slot.getTopic().isEmpty()) {
                    tvTopic.setVisibility(View.VISIBLE);
                    tvTopic.setText("Tema: " + slot.getTopic());
                }

                if (slot.getTargetStudentEmail() != null && !slot.getTargetStudentEmail().isEmpty()) {
                    tvTarget.setVisibility(View.VISIBLE);
                    String targetName = slot.getTargetStudentName();
                    if (targetName != null && !targetName.isEmpty()) {
                        tvTarget.setText("Para: " + targetName);
                    } else {
                        tvTarget.setText("Para: " + slot.getTargetStudentEmail());
                    }
                } else if (slot.getTargetStudentEmail() == null) {
                    tvTarget.setVisibility(View.VISIBLE);
                    tvTarget.setText("Para: Todos los estudiantes");
                }
            } else {
                tvBadge.setBackgroundColor(getColor(R.color.studentPrimary));
                tvBadge.setText("Disponible");
            }

            binding.llScheduleSlots.addView(itemView);
        }
    }

    private void loadTutoringSessions() {
        List<ScheduleSlot> sessions = dbHelper.getUpcomingTutoringSessions(aulaId, 3);
        binding.llTutoringSessions.removeAllViews();

        if (sessions.isEmpty()) {
            binding.tvEmptyTutoring.setVisibility(View.VISIBLE);
            return;
        }
        binding.tvEmptyTutoring.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < sessions.size(); i++) {
            ScheduleSlot session = sessions.get(i);
            View itemView = inflater.inflate(R.layout.item_schedule_slot, binding.llTutoringSessions, false);

            TextView tvDate = itemView.findViewById(R.id.tvSlotDate);
            TextView tvTime = itemView.findViewById(R.id.tvSlotTime);
            TextView tvTopic = itemView.findViewById(R.id.tvSlotTopic);
            TextView tvTarget = itemView.findViewById(R.id.tvSlotTarget);
            TextView tvBadge = itemView.findViewById(R.id.tvSlotBadge);
            TextView btnDelete = itemView.findViewById(R.id.btnDeleteSlot);

            btnDelete.setVisibility(View.GONE);

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

            binding.llTutoringSessions.addView(itemView);
        }
    }

    private void showRequestTutoringDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_request_tutoring, null);

        com.google.android.material.textfield.TextInputEditText etTopic =
                dialogView.findViewById(R.id.etTopic);
        RadioButton rbUseAvailable = dialogView.findViewById(R.id.rbUseAvailableSlot);
        RadioButton rbCustom = dialogView.findViewById(R.id.rbCustomDateTime);
        View llAvailableSlots = dialogView.findViewById(R.id.llAvailableSlots);
        View llCustomDateTime = dialogView.findViewById(R.id.llCustomDateTime);
        Spinner spinnerSlots = dialogView.findViewById(R.id.spinnerAvailableSlots);
        TextView tvDate = dialogView.findViewById(R.id.tvSelectDate);
        TextView tvTime = dialogView.findViewById(R.id.tvSelectTime);
        Button btnSave = dialogView.findViewById(R.id.btnSaveRequest);
        Button btnCancel = dialogView.findViewById(R.id.btnCancelRequest);

        // RadioGroup doesn't exist as a view in this dialog layout
        // Let's use the existing android ids
        android.widget.RadioGroup rgMode = dialogView.findViewById(R.id.rgDateTimeMode);

        String[] customDate = {""};
        String[] customTime = {""};

        // Toggle between modes
        rgMode.setOnCheckedChangeListener((group, checkedId) -> {
            boolean useAvailable = checkedId == R.id.rbUseAvailableSlot;
            llAvailableSlots.setVisibility(useAvailable ? View.VISIBLE : View.GONE);
            llCustomDateTime.setVisibility(useAvailable ? View.GONE : View.VISIBLE);
        });

        // Load available slots into spinner
        List<ScheduleSlot> availableSlots = dbHelper.getAvailableScheduleSlotsForAula(aulaId);
        List<String> slotLabels = new java.util.ArrayList<>();
        slotLabels.add("Seleccionar horario...");
        for (ScheduleSlot s : availableSlots) {
            String date = s.getSlotDate();
            String displayDate = date;
            if (date != null && date.length() == 10) {
                String[] parts = date.split("-");
                displayDate = parts[2] + "/" + parts[1] + "/" + parts[0];
            }
            slotLabels.add(displayDate + " " + s.getStartTime() + " - " + s.getEndTime());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, slotLabels);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSlots.setAdapter(spinnerAdapter);

        // Custom date picker
        tvDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                customDate[0] = String.format("%04d-%02d-%02d", year, month + 1, day);
                tvDate.setText(String.format("%02d/%02d/%04d", day, month + 1, year));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Custom time picker
        tvTime.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new TimePickerDialog(this, (view, hour, minute) -> {
                customTime[0] = String.format("%02d:%02d", hour, minute);
                tvTime.setText(customTime[0]);
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnSave.setOnClickListener(v -> {
            String topic = etTopic.getText().toString().trim();
            if (topic.isEmpty()) {
                Toast.makeText(this, "La razón/tema es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            String preferredDate;
            String preferredTime;
            String preferredEndTime;

            if (rbUseAvailable.isChecked()) {
                int pos = spinnerSlots.getSelectedItemPosition();
                if (pos <= 0) {
                    Toast.makeText(this, "Selecciona un horario disponible", Toast.LENGTH_SHORT).show();
                    return;
                }
                ScheduleSlot selectedSlot = availableSlots.get(pos - 1);
                preferredDate = selectedSlot.getSlotDate();
                preferredTime = selectedSlot.getStartTime();
                preferredEndTime = selectedSlot.getEndTime();
            } else {
                if (customDate[0].isEmpty() || customTime[0].isEmpty()) {
                    Toast.makeText(this, "Completa la fecha y hora", Toast.LENGTH_SHORT).show();
                    return;
                }
                preferredDate = customDate[0];
                preferredTime = customTime[0];
                // Default to 1 hour duration
                String[] parts = preferredTime.split(":");
                int hour = Integer.parseInt(parts[0]);
                int minute = Integer.parseInt(parts[1]);
                hour = (hour + 1) % 24;
                preferredEndTime = String.format("%02d:%02d", hour, minute);
            }

            long result = dbHelper.createTutoringRequest(aulaId, userEmail, topic, "",
                    preferredDate, preferredTime, preferredEndTime);
            if (result != -1) {
                Toast.makeText(this, "Solicitud de tutoría enviada", Toast.LENGTH_LONG).show();
                dialog.dismiss();
                loadMyPendingRequest();
            } else {
                Toast.makeText(this, "Error al enviar la solicitud", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void loadChatUnreadBadge() {
        int unread = dbHelper.getUnreadCount(aulaId, userEmail);
        if (unread > 0) {
            binding.badgeChatUnread.setVisibility(View.VISIBLE);
        } else {
            binding.badgeChatUnread.setVisibility(View.GONE);
        }
    }

    private void loadMyPendingRequest() {
        boolean hasPending = dbHelper.hasPendingTutoringRequest(aulaId, userEmail);

        if (hasPending) {
            binding.btnRequestTutoring.setVisibility(View.GONE);
            binding.cardMyPendingRequest.setVisibility(View.VISIBLE);

            TutoringRequest req = dbHelper.getPendingTutoringForStudent(aulaId, userEmail);
            if (req != null) {
                binding.tvMyRequestTopic.setText("Tema: " + req.getTopic());
                String dateStr = req.getPreferredDate();
                if (dateStr != null && dateStr.length() == 10) {
                    String[] parts = dateStr.split("-");
                    binding.tvMyRequestDate.setText("Fecha preferida: " + parts[2] + "/" + parts[1] + "/" + parts[0]);
                } else {
                    binding.tvMyRequestDate.setText("Fecha preferida: " + dateStr);
                }
                binding.tvMyRequestTime.setText("Hora preferida: " + req.getPreferredTime());
            }
        } else {
            binding.btnRequestTutoring.setVisibility(View.VISIBLE);
            binding.cardMyPendingRequest.setVisibility(View.GONE);
        }
    }

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

            tvCustomName.setText(material.getCustomName());

            String rawDate = material.getCreatedAt();
            if (rawDate != null && rawDate.length() >= 10) {
                String datePart = rawDate.substring(0, 10);
                String[] parts = datePart.split("-");
                tvUploadDate.setText(parts[2] + "/" + parts[1] + "/" + parts[0]);
            } else {
                tvUploadDate.setText(rawDate);
            }

            btnAction.setText("Descargar");
            btnAction.setOnClickListener(v -> {
                dbHelper.recordMaterialView(material.getId(), userEmail);
                File file = new File(material.getFilePath());
                if (file.exists()) {
                    Uri fileUri = FileProvider.getUriForFile(this,
                            getPackageName() + ".fileprovider", file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(fileUri, material.getMimeType());
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(this, "No se puede abrir el archivo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "El archivo ya no está disponible", Toast.LENGTH_SHORT).show();
                }
            });

            binding.llMaterials.addView(itemView);
        }
    }
}
