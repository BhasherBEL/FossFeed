package be.bhasher.fossfeed;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import be.bhasher.fossfeed.databinding.ActivityNewsubBinding;
import be.bhasher.fossfeed.ui.home.Feed;
import be.bhasher.fossfeed.ui.managesubs.SubsAdapter;
import be.bhasher.fossfeed.utils.cache.AppDatabase;

public class NewSubActivity extends AppCompatActivity {
    private ActivityNewsubBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewsubBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button button = findViewById(R.id.newsub_button);
        TextInputEditText titleInput = findViewById(R.id.newsub_titleinputedit);
        TextInputEditText urlInput = findViewById(R.id.newsub_urlinputedit);

        button.setOnClickListener(v -> {
            if(urlInput.getText().length() > 0){
                new AddSubscription(titleInput.getText().toString(), urlInput.getText().toString()).execute();
            }
        });
    }

    class AddSubscription extends AsyncTask<String, Void, Boolean>{
        private final String title;
        private final String url;

        public AddSubscription(String title, String url){
            this.title = title;
            this.url = url;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            AppDatabase.getInstance().insert(new Feed(title, url));
            return true;
        }

        @Override
        public void onPostExecute(Boolean bool){
            if(bool){
                onBackPressed();
                //new SubsAdapter.Init(binding.getRoot()).execute();
            }
        }
    }
}