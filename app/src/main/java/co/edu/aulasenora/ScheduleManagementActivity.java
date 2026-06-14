package co.edu.aulasenora;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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

import co.edu.aulasenora.db.DatabaseHelper;
import co.edu.aulasenora.models.AdmittedStudent;
import co.edu.aulasenora.models.ScheduleSlot;

public class ScheduleManagementActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private String userEmail;
    private int aulaId;

    private String selectedDate = "";
    private String selectedStartTime = "";
    private String selectedEndTime = "";

    private TextView tvTitle;
    private LinearLayout llScheduleSlots;
    private TextView tvEmptySchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule_management);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("user_email");
        aulaId = getIntent().getIntExtra("aula_id", -1);

        tvTitle = findViewById(R.id.tvTitle);
        llScheduleSlots = findViewById(R.id.llScheduleSlots);
        tvEmptySchedule = findViewById(R.id.tvEmptySchedule);

        String aulaName = getIntent().getStringExtra("aula_name");
        if (aulaName != null) {
            tvTitle.setText("Horarios: " + aulaName);
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnAddSlot).setOnClickListener(v -> showAddScheduleSlotDialog());

        loadScheduleSlots();
    }

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

        rbTutoring.setOnCheckedChangeListener((buttonView, isChecked) -> {
            llTutoringFields.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        rbSpecificStudent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            spinnerStudents.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

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

        tvDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, day) -> {
                selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day);
                tvDate.setText(String.format("%02d/%02d/%04d", day, month + 1, year));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        tvStart.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new TimePickerDialog(this, (view, hour, minute) -> {
                selectedStartTime = String.format("%02d:%02d", hour, minute);
                tvStart.setText(selectedStartTime);
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        });

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
            } else {
                Toast.makeText(this, "Error al guardar el horario", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void loadScheduleSlots() {
        List<ScheduleSlot> slots = dbHelper.getScheduleSlotsForAula(aulaId);
        llScheduleSlots.removeAllViews();

        if (slots.isEmpty()) {
            tvEmptySchedule.setVisibility(View.VISIBLE);
            return;
        }
        tvEmptySchedule.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(this);
        for (ScheduleSlot slot : slots) {
            View itemView = inflater.inflate(R.layout.item_schedule_slot, llScheduleSlots, false);

            TextView tvDate = itemView.findViewById(R.id.tvSlotDate);
            TextView tvTime = itemView.findViewById(R.id.tvSlotTime);
            TextView tvTopic = itemView.findViewById(R.id.tvSlotTopic);
            TextView tvTarget = itemView.findViewById(R.id.tvSlotTarget);
            TextView tvBadge = itemView.findViewById(R.id.tvSlotBadge);
            TextView btnDelete = itemView.findViewById(R.id.btnDeleteSlot);

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
            });

            llScheduleSlots.addView(itemView);
        }
    }
}
