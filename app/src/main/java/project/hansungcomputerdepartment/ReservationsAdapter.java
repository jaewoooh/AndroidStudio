package project.hansungcomputerdepartment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import project.hansungcomputerdepartment.models.Reservation;

public class ReservationsAdapter extends BaseAdapter {

    private Context context;
    private List<Reservation> reservations;
    private OnReservationCancelListener cancelListener;

    public interface OnReservationCancelListener {
        void onReservationCancel(Reservation reservation);
    }

    public ReservationsAdapter(Context context, List<Reservation> reservations, OnReservationCancelListener cancelListener) {
        this.context = context;
        this.reservations = reservations;
        this.cancelListener = cancelListener;
    }

    @Override
    public int getCount() {
        return reservations.size();
    }

    @Override
    public Object getItem(int position) {
        return reservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        }

        Reservation reservation = reservations.get(position);

        TextView reservationDetails = convertView.findViewById(R.id.reservationDetails);
        Button cancelReservationButton = convertView.findViewById(R.id.cancelReservationButton);

        reservationDetails.setText(String.format("예약 정보: %s, %s, %s", reservation.getDate(), reservation.getTime(), reservation.getEquipment()));

        cancelReservationButton.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.onReservationCancel(reservation);
            }
        });

        return convertView;
    }
}
