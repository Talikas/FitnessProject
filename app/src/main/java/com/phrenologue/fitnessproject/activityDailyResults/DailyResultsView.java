package com.phrenologue.fitnessproject.activityDailyResults;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.phrenologue.fitnessproject.R;
import com.phrenologue.fitnessproject.activityInput.InputView;
import com.phrenologue.fitnessproject.database.Daily;
import com.phrenologue.fitnessproject.database.DatabaseClient;
import com.phrenologue.fitnessproject.databinding.ActivityDailyResultsBinding;

import java.text.DecimalFormat;
import java.util.List;

public class DailyResultsView extends AppCompatActivity implements IDailyResultsView {
    private DailyResultsPresenter presenter;
    private TextView wChange, dailyAvg, wAvgChange, changeTitle, avgTitle, avgChangeTitle;
    private boolean isNight;
    private double nightWeight = 0, morningWeight = 0;
    private int primaryKey;
    private double prevAvg;
    private CombinedChart dailyChart;
    private Spinner chartSpinner;
    private boolean fromProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDailyResultsBinding binding = ActivityDailyResultsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new DailyResultsPresenter(this);

        Intent intent = getIntent();

        isNight = intent.getBooleanExtra("Night", false);
        if (isNight) {
            nightWeight = intent.getDoubleExtra("Weight", 0.0);
        } else {
            morningWeight = intent.getDoubleExtra("Weight", 0.0);
        }
        primaryKey = intent.getIntExtra("PrimaryKey", 0);
        wChange = binding.changeNo;
        dailyAvg = binding.dailyAvg;
        wAvgChange = binding.changeNo3;
        changeTitle = binding.changeTitle;
        avgTitle = binding.dailyAvgTitle;
        avgChangeTitle = binding.dailyAvgChangeTitle;
        dailyChart = binding.dailyChart;
        chartSpinner = binding.chartSpinner;

        fromProfile = intent.getBooleanExtra("fromProfile", false);
        presenter.checkIntentRood(fromProfile);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.chart_type, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chartSpinner.setAdapter(adapter);
        chartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDataForChart(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getPrevAvg();

        presenter.getPrevWeight(primaryKey, isNight);
        Button backBtn = binding.backBtn;


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InputView.class);
                startActivity(intent);
            }
        });

        YAxis rightAxis = dailyChart.getAxisRight();
        rightAxis.setEnabled(false);
        YAxis leftAxis = dailyChart.getAxisLeft();
        leftAxis.setAxisMinimum(55f);
        dailyChart.getXAxis().setEnabled(false);
    }

    private void getPrevAvg() {
        class PrevDay extends AsyncTask<Void, Void, Double> {

            @Override
            protected Double doInBackground(Void... voids) {
                Double yesterdayAvg = DatabaseClient.getInstance(getApplicationContext())
                        .getDatabase().dailyDao().getPrevDay().getDailyAvg();
                return yesterdayAvg;
            }

            @Override
            protected void onPostExecute(Double yesterdayAvg) {
                super.onPostExecute(yesterdayAvg);
                prevAvg = yesterdayAvg;
                getData();
            }
        }

        PrevDay prevDay = new PrevDay();
        prevDay.execute();
    }

    private void getData() {
        class GetData extends AsyncTask<Void, Void, Double> {

            @Override
            protected Double doInBackground(Void... voids) {
                Double dWeight = DatabaseClient.getInstance(getApplicationContext()).getDatabase()
                        .dailyDao().getTodayWeight();
                return dWeight;
            }

            @Override
            protected void onPostExecute(Double dWeight) {
                super.onPostExecute(dWeight);
                DecimalFormat df = new DecimalFormat("####.##");
                if (isNight) {
                    double dailyAvgDouble = (nightWeight + dWeight) / 2;
                    String avgChange = df.format(100 * (dailyAvgDouble - prevAvg) / prevAvg);
                    dailyAvg.setText(dailyAvgDouble + "");
                    wAvgChange.setText(avgChange);
                    saveDailyAvg(primaryKey, dailyAvgDouble);
                } else {
                    dailyAvg.setText("0");
                }
            }
        }
        GetData getData = new GetData();
        getData.execute();
    }

    private void getDataForChart(final int position) {
        class GetDataForChart extends AsyncTask<Void, Void, List<Daily>> {

            @Override
            protected List<Daily> doInBackground(Void... voids) {
                List<Daily> dailiesList = DatabaseClient.getInstance(getApplicationContext())
                        .getDatabase().dailyDao().getAllDays();
                return dailiesList;
            }

            @Override
            protected void onPostExecute(List<Daily> dailiesList) {
                super.onPostExecute(dailiesList);
                presenter.drawChart(dailiesList, position);
            }
        }
        GetDataForChart getDataForChart = new GetDataForChart();
        getDataForChart.execute();
    }

    private void saveDailyAvg(final int primaryKey, final double dailyAvg) {
        class SaveDailyAvg extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                Daily daily = new Daily();
                daily.setDayNo(primaryKey);
                daily.setDailyAvg(dailyAvg);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Daily Avg. Saved!", Toast.LENGTH_LONG).show();
            }
        }
        SaveDailyAvg saveDailyAvg = new SaveDailyAvg();
        saveDailyAvg.execute();
    }

    @Override
    public void getPrevMorningWeight(final int primaryKey) {
        class PrevMorningWeight extends AsyncTask<Void, Void, Double> {

            @Override
            protected Double doInBackground(Void... voids) {
                Double prevMorningWeight = DatabaseClient.getInstance(getApplicationContext())
                        .getDatabase().dailyDao().getPrevDayWeight(primaryKey);
                return prevMorningWeight;
            }

            @Override
            protected void onPostExecute(Double prevMorningWeight) {
                super.onPostExecute(prevMorningWeight);
                presenter.calculateWeightChange(prevMorningWeight, morningWeight);
            }
        }

        PrevMorningWeight prevMorningWeight = new PrevMorningWeight();
        prevMorningWeight.execute();

    }

    @Override
    public void getPrevNightWeight(final int primaryKey) {
        class PrevNightWeight extends AsyncTask<Void, Void, Double> {

            @Override
            protected Double doInBackground(Void... voids) {
                Double prevNightWeight = DatabaseClient.getInstance(getApplicationContext())
                        .getDatabase().dailyDao().getPrevNightWeight(primaryKey - 1);
                Log.e("", "");
                return prevNightWeight;
            }

            @Override
            protected void onPostExecute(Double prevNightWeight) {
                super.onPostExecute(prevNightWeight);
                presenter.calculateWeightChange(prevNightWeight, nightWeight);
            }
        }
        PrevNightWeight prevNightWeight = new PrevNightWeight();
        prevNightWeight.execute();
    }

    @Override
    public void printWeightChange(int change) {
        String wChangeStr = "%" + change;
        wChange.setText(wChangeStr);
    }

    @Override
    public void drawChart(CombinedData data) {
        dailyChart.getDescription().setEnabled(false);
        dailyChart.setDrawGridBackground(false);
        dailyChart.setData(data);
        dailyChart.invalidate();
    }

    @Override
    public void hideReports() {

        changeTitle.setVisibility(View.GONE);
        wChange.setVisibility(View.GONE);
        avgChangeTitle.setVisibility(View.GONE);
        wAvgChange.setVisibility(View.GONE);
        dailyAvg.setVisibility(View.GONE);
        avgTitle.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), InputView.class);
        startActivity(intent);
    }

}
