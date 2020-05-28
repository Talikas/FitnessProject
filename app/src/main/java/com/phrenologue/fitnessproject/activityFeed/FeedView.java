package com.phrenologue.fitnessproject.activityFeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.phrenologue.fitnessproject.R;
import com.phrenologue.fitnessproject.activityInput.InputView;
import com.phrenologue.fitnessproject.database.Daily;
import com.phrenologue.fitnessproject.database.DatabaseClient;
import com.phrenologue.fitnessproject.databinding.ActivityFeedBinding;

import java.util.List;

public class FeedView extends AppCompatActivity {
    private ActivityFeedBinding binding;
    private RecyclerView wRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        wRecycler = binding.weightRecycler;
        getDailies();



    }

    private void getDailies(){
        class GetDailies extends AsyncTask<Void, Void, List<Daily>>{

            @Override
            protected List<Daily> doInBackground(Void... voids) {
                List<Daily> dailies = DatabaseClient.getInstance(getApplicationContext())
                        .getDatabase().dailyDao().getAllDays();
                return dailies;
            }
            @Override
            protected void onPostExecute(List<Daily> dailies){
                super.onPostExecute(dailies);
                FeedAdapter adapter = new FeedAdapter(getApplicationContext(), dailies);
                wRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.VERTICAL, false));
                wRecycler.setAdapter(adapter);
            }
        }
        GetDailies getDailies = new GetDailies();
        getDailies.execute();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), InputView.class);
        startActivity(intent);
    }
}
