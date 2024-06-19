package project.hansungcomputerdepartment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private List<Notice> noticeList;
    private Context context;

    public NoticeAdapter(List<Notice> noticeList, Context context) {
        this.noticeList = noticeList;
        this.context = context;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Notice notice = noticeList.get(position);
        holder.categoryTextView.setText(notice.getCategory());
        holder.titleTextView.setText(notice.getTitle());
        holder.authorTextView.setText(notice.getAuthor());
        holder.dateTextView.setText(notice.getDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("category", notice.getCategory());
            intent.putExtra("title", notice.getTitle());
            intent.putExtra("author", notice.getAuthor());
            intent.putExtra("date", notice.getDate());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView, titleTextView, authorTextView, dateTextView;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.noticeCategory);
            titleTextView = itemView.findViewById(R.id.noticeTitle);
            authorTextView = itemView.findViewById(R.id.noticeAuthor);
            dateTextView = itemView.findViewById(R.id.noticeDate);
        }
    }
}
