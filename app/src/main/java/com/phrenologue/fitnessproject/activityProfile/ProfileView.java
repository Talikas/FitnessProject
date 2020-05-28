package com.phrenologue.fitnessproject.activityProfile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.phrenologue.fitnessproject.activityDailyResults.DailyResultsView;
import com.phrenologue.fitnessproject.activityFeed.FeedView;
import com.phrenologue.fitnessproject.activityInput.InputView;
import com.phrenologue.fitnessproject.constants.Colors;
import com.phrenologue.fitnessproject.constants.UserProps;
import com.phrenologue.fitnessproject.database.Daily;
import com.phrenologue.fitnessproject.database.DatabaseClient;
import com.phrenologue.fitnessproject.databinding.ActivityProfileBinding;
import com.sdsmdg.harjot.crollerTest.Croller;

import java.util.Calendar;
import java.util.Date;

public class ProfileView extends AppCompatActivity implements IProfileView {
    private ActivityProfileBinding binding;
    private ProfilePresenter presenter;
    private double startingW, goal, stepLength;
    private Croller croller;
    private Double currentW = startingW;
    private Button log, measure, stats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        stats = binding.stats;
        log = binding.log;
        measure = binding.newMeasurement;
        presenter = new ProfilePresenter(this);

        UserProps props = new UserProps(this);
        startingW = props.getStartingW();
        goal = props.getGoal();
        stepLength = props.getStepLength();


        croller = binding.croller;
        croller.setIndicatorWidth(10);
        croller.setMainCircleColor(Colors.mintCream);
        croller.setBackCircleColor(Colors.russianViolet);
        croller.setProgressPrimaryColor(Colors.blueSapphire);
        croller.setProgressSecondaryColor(Colors.coolGray);
        croller.setIsContinuous(false);
        croller.setMax(((int) startingW));
        croller.setEnabled(false);
        croller.setLabel("your progress");
        final SharedPreferences inputPrefs = getSharedPreferences("firstInput", MODE_PRIVATE);
        presenter.setWeight(inputPrefs);



        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DailyResultsView.class);
                intent.putExtra("fromProfile",true);
                startActivity(intent);
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FeedView.class);
                startActivity(intent);
            }
        });

        measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InputView.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void getLastWeight() {
        getWeight();
    }

    @Override
    public void showCurrentWeight() {
        UserProps props = new UserProps(getApplicationContext());
        double currentW = props.getStartingW();
        croller.setProgress((int) currentW);

    }

    private void getWeight(){
        class LastWeight extends AsyncTask<Void, Void, Daily>{

            @Override
            protected Daily doInBackground(Void... voids) {
                Daily last = DatabaseClient.getInstance(getApplicationContext()).getDatabase().dailyDao()
                        .getPrevDay();

                return last;
            }
            @Override
            protected void onPostExecute(Daily last){
                super.onPostExecute(last);
                UserProps props = new UserProps(getApplicationContext());
                int weight = presenter.getLastWeight(last);
                props.setCurrentWeight(presenter.getLastWeightDouble(last));
                croller.setProgress(weight);
            }
        }
        LastWeight lastWeight = new LastWeight();
        lastWeight.execute();
    }
}
