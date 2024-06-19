package project.hansungcomputerdepartment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class EquipmentRentalFragment extends Fragment {

    private Button selectDateButton;
    private Spinner equipmentSpinner;
    private EditText nameEditText, trackEditText, phoneEditText;
    private Button reserveButton;
    private TextView selectedDateTextView, quantityTextView;
    private RadioGroup timeRadioGroup;
    private String selectedDate, selectedTime;
    private ArrayAdapter<String> equipmentAdapter;
    private FirebaseFirestore db;
    private Map<String, Integer> equipmentQuantityMap;

    private static final String TAG = "EquipmentRentalFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        equipmentQuantityMap = new HashMap<>();
        db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_equipment_rental, container, false);

        selectDateButton = view.findViewById(R.id.selectDateButton);
        equipmentSpinner = view.findViewById(R.id.equipmentSpinner);
        nameEditText = view.findViewById(R.id.nameEditText);
        trackEditText = view.findViewById(R.id.trackEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        reserveButton = view.findViewById(R.id.reserveButton);
        selectedDateTextView = view.findViewById(R.id.selectedDateTextView);
        quantityTextView = view.findViewById(R.id.quantityTextView);
        timeRadioGroup = view.findViewById(R.id.timeRadioGroup);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        timeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                if (radioButton != null) {
                    selectedTime = radioButton.getText().toString();
                }
            }
        });

        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    checkAndReserveEquipment();
                }
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });

        setEditTextFocus(nameEditText);
        setEditTextFocus(trackEditText);
        setEditTextFocus(phoneEditText);

        equipmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateQuantityText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                quantityTextView.setText("남은 수량: -");
            }
        });

        loadEquipmentListFromFirestore();
    }

    private void showDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("날짜 선택");

        // CalendarConstraints 설정을 통해 오늘 이전 날짜를 선택할 수 없게 만듦
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());
        builder.setCalendarConstraints(constraintsBuilder.build());

        MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            selectedDate = sdf.format(selection);
            selectedDateTextView.setText("선택된 날짜: " + selectedDate);
        });

        datePicker.show(getParentFragmentManager(), datePicker.toString());
    }

    private boolean validateInputs() {
        String name = nameEditText.getText().toString().trim();
        String track = trackEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String equipment = equipmentSpinner.getSelectedItem().toString();

        if (name.isEmpty()) {
            Toast.makeText(getContext(), "이름을 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (track.isEmpty()) {
            Toast.makeText(getContext(), "트랙을 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.isEmpty()) {
            Toast.makeText(getContext(), "전화번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedDate == null) {
            Toast.makeText(getContext(), "날짜를 선택하세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedTime == null) {
            Toast.makeText(getContext(), "시간을 선택하세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (equipment.isEmpty() || equipment.equals("-선택안함-")) {
            Toast.makeText(getContext(), "기자재를 선택하세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void checkAndReserveEquipment() {
        String equipment = equipmentSpinner.getSelectedItem().toString();
        int availableQuantity = equipmentQuantityMap.getOrDefault(equipment, 0);

        db.collection("reservations")
                .whereEqualTo("date", selectedDate)
                .whereEqualTo("time", selectedTime)
                .whereEqualTo("equipment", equipment)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int reservedCount = task.getResult().size();
                        if (reservedCount < availableQuantity) {
                            reserveEquipment(equipment, availableQuantity - reservedCount - 1);
                        } else {
                            Toast.makeText(getContext(), "선택한 기자재의 예약 가능 수량이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "예약 가능 여부를 확인하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void reserveEquipment(String equipment, int newQuantity) {
        String name = nameEditText.getText().toString().trim();
        String track = trackEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", name);
        reservation.put("track", track);
        reservation.put("phone", phone);
        reservation.put("date", selectedDate);
        reservation.put("time", selectedTime);
        reservation.put("equipment", equipment);
        reservation.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.collection("reservations")
                .add(reservation)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        updateEquipmentQuantity(equipment, newQuantity);
                        Toast.makeText(getContext(), "예약이 완료되었습니다", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Reservation successful: " + reservation.toString());
                        resetFields(); // 필드 초기화
                    } else {
                        Toast.makeText(getContext(), "예약에 실패하였습니다", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "예약에 실패하였습니다", task.getException());
                    }
                });
    }

    private void resetFields() {
        nameEditText.setText("");
        trackEditText.setText("");
        phoneEditText.setText("");
        selectedDateTextView.setText("");
        quantityTextView.setText("");
        timeRadioGroup.clearCheck(); // 라디오 그룹의 선택을 초기화
        selectedDate = null; // 선택된 날짜 초기화
        selectedTime = null; // 선택된 시간 초기화
    }

    private void updateEquipmentQuantity(String equipment, int newQuantity) {
        DocumentReference equipmentRef = db.collection("equipments").document("equipmentList");

        equipmentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) document.get("items");
                    for (Map<String, Object> item : items) {
                        if (item.get("name").equals(equipment)) {
                            item.put("quantity", newQuantity);
                            break;
                        }
                    }
                    equipmentRef.update("items", items)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "기자재 수량이 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                                loadEquipmentListFromFirestore(); // Reload equipment list to update UI
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "기자재 수량 업데이트에 실패하였습니다", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "기자재 수량 업데이트에 실패하였습니다", e);
                            });
                } else {
                    Log.e(TAG, "기자재 목록이 없습니다.");
                }
            } else {
                Log.e(TAG, "기자재 목록을 불러오는 데 실패했습니다.", task.getException());
            }
        });
    }

    private void loadEquipmentListFromFirestore() {
        db.collection("equipments").document("equipmentList").addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                Log.e(TAG, "Listen failed.", e);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                ArrayList<String> equipmentList = new ArrayList<>();
                List<Map<String, Object>> items = (List<Map<String, Object>>) snapshot.get("items");
                equipmentQuantityMap.clear();
                for (Map<String, Object> item : items) {
                    String name = (String) item.get("name");
                    Long quantity = (Long) item.get("quantity");
                    if (name != null && quantity != null) {
                        equipmentList.add(name);
                        equipmentQuantityMap.put(name, quantity.intValue());
                    }
                }
                equipmentAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, equipmentList);
                equipmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                equipmentSpinner.setAdapter(equipmentAdapter);
                updateQuantityText();
            } else {
                Log.e(TAG, "Current data: null");
            }
        });
    }

    private void updateQuantityText() {
        String equipment = equipmentSpinner.getSelectedItem().toString();
        int quantity = equipmentQuantityMap.containsKey(equipment) ? equipmentQuantityMap.get(equipment) : 0;
        quantityTextView.setText("남은 수량: " + quantity);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setEditTextFocus(EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editText.setFocusableInTouchMode(true);
                return false;
            }
        });
    }
}
