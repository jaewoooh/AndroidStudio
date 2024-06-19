package project.hansungcomputerdepartment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import project.hansungcomputerdepartment.models.Reservation;

public class MyInfoFragment extends Fragment {

    private ListView reservationsListView;
    private Button logoutButton;
    private FirebaseFirestore db;
    private ReservationsAdapter adapter;
    private List<Reservation> reservationsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);

        reservationsListView = view.findViewById(R.id.reservationsListView);
        logoutButton = view.findViewById(R.id.logoutButton);
        db = FirebaseFirestore.getInstance();
        reservationsList = new ArrayList<>();

        adapter = new ReservationsAdapter(getContext(), reservationsList, new ReservationsAdapter.OnReservationCancelListener() {
            @Override
            public void onReservationCancel(Reservation reservation) {
                cancelReservation(reservation);
            }
        });

        reservationsListView.setAdapter(adapter);

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });

        loadReservations();

        return view;
    }

    public void loadReservations() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("reservations")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reservationsList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Reservation reservation = document.toObject(Reservation.class);
                            reservation.setId(document.getId()); // 예약 ID 설정
                            reservationsList.add(reservation);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // 오류 처리
                    }
                });
    }

    private void cancelReservation(Reservation reservation) {
        db.collection("reservations").document(reservation.getId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        updateEquipmentQuantity(reservation.getEquipment(), 1);
                        loadReservations();
                    } else {
                        // 오류 처리
                    }
                });
    }

    private void updateEquipmentQuantity(String equipment, int delta) {
        DocumentReference equipmentRef = db.collection("equipments").document("equipmentList");
        equipmentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) document.get("items");
                    for (Map<String, Object> item : items) {
                        if (item.get("name").equals(equipment)) {
                            Long quantity = (Long) item.get("quantity");
                            item.put("quantity", quantity + delta);
                            break;
                        }
                    }
                    equipmentRef.update("items", items)
                            .addOnSuccessListener(aVoid -> {
                                // 성공 메시지
                            })
                            .addOnFailureListener(e -> {
                                // 오류 메시지
                            });
                }
            }
        });
    }
}
