package co.edu.aulasenora;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.List;

import co.edu.aulasenora.databinding.ActivityManageAulaBinding;
import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.models.AccessRequest;
import co.edu.aulasenora.models.AdmittedStudent;
import co.edu.aulasenora.models.Aula;
import co.edu.aulasenora.models.ScheduleSlot;
import co.edu.aulasenora.models.TutoringRequest;

public class ManageAulaActivity extends AppCompatActivity {

    private ActivityManageAulaBinding binding;
    private DatabaseHelper dbHelper;
    private String userEmail;
    private int aulaId;

    // Dialog state
    private String selectedDate = "";
    private String selectedStartTime = "";
    private String selectedEndTime = "";

    private boolean showingAllSchedule = false;
    private boolean showingAllTutoring = false;

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
                    binding.includeHeader.tvTitle.setText("Gestionar: " + a.getName());
                    break;
                }
            }
        }

        binding.btnAddSlot.setOnClickListener(v -> showAddScheduleSlotDialog());

        binding.btnViewAllSchedule.setOnClickListener(v -> {
            showingAllSchedule = !showingAllSchedule;
            loadScheduleSlots();
            binding.btnViewAllSchedule.setText(showingAllSchedule ? "Ver menos" : "Ver todos");
        });

        binding.btnViewAllTutoring.setOnClickListener(v -> {
            showingAllTutoring = !showingAllTutoring;
            loadUpcomingTutoringSessions();
            binding.btnViewAllTutoring.setText(showingAllTutoring ? "Ver menos" : "Ver todos");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (aulaId == -1) return;
        showingAllSchedule = false;
        showingAllTutoring = false;
        binding.btnViewAllSchedule.setText("Ver todos");
        binding.btnViewAllTutoring.setText("Ver todos");
        loadAdmittedStudents();
        loadScheduleSlots();
        loadUpcomingTutoringSessions();
        loadTutoringRequests();
        loadAccessRequests();
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

    // ===== SCHEDULE SLOTS =====

    private void showAddScheduleSlotDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_schedule_slot, null);

        TextView tvDate = dialogView.findViewById(R.id.tvSelectDate);
        TextView tvStart = dialogView.findViewById(R.id.tvSelectStartTime);
        TextView tvEnd = dialogView.findViewById(R.id.tvSelectEndTime);
        RadioButton rbTutoring = dialogView.findViewById(R.id.rbTutoring);
        RadioButton rbAvailability = dialogView.findViewById(R.id.rbAvailability);
        View llTutoringFields = dialogView.findViewById(R.id.llTutoringFields);
        com.google.android.material.textfield.TextInputEditText etTopic =
                dialogView.findViewById(R.id.etTutoringTopic);
        RadioButton rbAllStudents = dialogView.findViewById(R.id.rbAllStudents);
        RadioButton rbSpecificStudent = dialogView.findViewById(R.id.rbSpecificStudent);
        Spinner spinnerStudents = dialogView.findViewById(R.id.spinnerStudents);
        Button btnSave = dialogView.findViewById(R.id.btnSaveSlot);
        Button btnCancel = dialogView.findViewById(R.id.btnCancelSlot);

        selectedDate = "";
        selectedStartTime = "";
        selectedEndTime = "";

        // Type toggle
        rbTutoring.setOnCheckedChangeListener((buttonView, isChecked) -> {
            llTutoringFields.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Target toggle
        rbSpecificStudent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            spinnerStudents.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Load admitted students into spinner
        List<AdmittedStudent> admittedList = dbHelper.getAdmittedStudents(aulaId);
        List<String> studentLabels = new java.util.ArrayList<>();
        studentLabels.add("Seleccionar estudiante...");
        for (AdmittedStudent s : admittedList) {
            studentLabels.add(s.getName() + " (" + s.getEmail() + ")");
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, studentLabels);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudents.setAdapter(spinnerAdapter);

        // Date picker
        tvDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day);
                tvDate.setText(String.format("%02d/%02d/%04d", day, month + 1, year));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Start time picker
        tvStart.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new TimePickerDialog(this, (view, hour, minute) -> {
                selectedStartTime = String.format("%02d:%02d", hour, minute);
                tvStart.setText(selectedStartTime);
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        });

        // End time picker
        tvEnd.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new TimePickerDialog(this, (view, hour, minute) -> {
                selectedEndTime = String.format("%02d:%02d", hour, minute);
                tvEnd.setText(selectedEndTime);
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnSave.setOnClickListener(v -> {
            if (selectedDate.isEmpty() || selectedStartTime.isEmpty() || selectedEndTime.isEmpty()) {
                Toast.makeText(this, "Completa la fecha y hora", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedStartTime.compareTo(selectedEndTime) >= 0) {
                Toast.makeText(this, "La hora fin debe ser mayor a la hora inicio", Toast.LENGTH_SHORT).show();
                return;
            }

            String type = rbTutoring.isChecked() ? "tutoring" : "availability";
            String topic = null;
            String targetEmail = null;

            if ("tutoring".equals(type)) {
                topic = etTopic.getText().toString().trim();
                if (topic.isEmpty()) {
                    Toast.makeText(this, "El tema de la tutoría es obligatorio", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (rbSpecificStudent.isChecked()) {
                    int pos = spinnerStudents.getSelectedItemPosition();
                    if (pos <= 0) {
                        Toast.makeText(this, "Selecciona un estudiante específico", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String label = studentLabels.get(pos);
                    targetEmail = label.substring(label.indexOf("(") + 1, label.indexOf(")"));
                }
            }

            if (dbHelper.hasScheduleConflict(userEmail, selectedDate, selectedStartTime, selectedEndTime)) {
                String conflictAula = dbHelper.getConflictAulaName(userEmail, selectedDate, selectedStartTime, selectedEndTime);
                String msg = "Ya tienes un horario que se sobrepone en esa fecha/hora";
                if (conflictAula != null) {
                    msg += " en: " + conflictAula;
                }
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                return;
            }

            long result = dbHelper.createScheduleSlot(aulaId, userEmail, selectedDate,
                    selectedStartTime, selectedEndTime, type, topic, targetEmail);
            if (result != -1) {
                Toast.makeText(this, "Horario agregado", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadScheduleSlots();
                loadUpcomingTutoringSessions();
            } else {
                Toast.makeText(this, "Error al guardar el horario", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void loadScheduleSlots() {
        List<ScheduleSlot> slots = showingAllSchedule
                ? dbHelper.getScheduleSlotsForAula(aulaId)
                : dbHelper.getRecentScheduleSlots(aulaId, 3);
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

            // Format date for display
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
                tvBadge.setBackgroundColor(getColor(R.color.volunteerPrimary));
                tvBadge.setText("Disponible");
            }

            btnDelete.setOnClickListener(v -> {
                dbHelper.deleteScheduleSlot(slot.getId());
                Toast.makeText(this, "Horario eliminado", Toast.LENGTH_SHORT).show();
                loadScheduleSlots();
                loadUpcomingTutoringSessions();
            });

            binding.llScheduleSlots.addView(itemView);
        }
    }

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
                Toast.makeText(this, "Tutoría de " + req.getStudentName() + " aceptada", Toast.LENGTH_SHORT).show();
                loadTutoringRequests();
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
}
