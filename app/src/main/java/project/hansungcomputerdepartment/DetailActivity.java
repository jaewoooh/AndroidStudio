package project.hansungcomputerdepartment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {

    private TextView categoryTextView, titleTextView, authorTextView, dateTextView, backTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // 타이틀 표시를 비활성화
        }

        // 뒤로가기 문구 클릭 시 뒤로 가기 설정
        backTextView = findViewById(R.id.backTextView);
        backTextView.setOnClickListener(v -> onBackPressed());

        categoryTextView = findViewById(R.id.detailCategory);
        titleTextView = findViewById(R.id.detailTitle);
        authorTextView = findViewById(R.id.detailAuthor);
        dateTextView = findViewById(R.id.detailDate);

        Intent intent = getIntent();
        if (intent != null) {
            categoryTextView.setText(intent.getStringExtra("category"));
            titleTextView.setText(intent.getStringExtra("title"));
            authorTextView.setText(intent.getStringExtra("author"));
            dateTextView.setText(intent.getStringExtra("date"));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
