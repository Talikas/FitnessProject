package com.phrenologue.fitnessproject.activityInput;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.phrenologue.fitnessproject.SetUpProps;
import com.phrenologue.fitnessproject.activityProfile.ProfileView;
import com.phrenologue.fitnessproject.R;
import com.phrenologue.fitnessproject.activityDailyResults.DailyResultsView;
import com.phrenologue.fitnessproject.activityFeed.FeedView;
import com.phrenologue.fitnessproject.constants.UserProps;
import com.phrenologue.fitnessproject.database.Daily;
import com.phrenologue.fitnessproject.database.DatabaseClient;
import com.phrenologue.fitnessproject.databinding.ActivityInputBinding;

public class InputView extends AppCompatActivity implements IInputView {
    private EditText weightInput, dayInput, monthInput, yearInput;
    private TextView dayTitle, monthTitle, yearTitle;
    private Spinner timeOfDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInputBinding binding = ActivityInputBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        final InputPresenter presenter = new InputPresenter(this);

        final SharedPreferences sp = getSharedPreferences("firstInput", MODE_PRIVATE);

        weightInput = binding.weightInput;
        dayTitle = binding.dayTitle;
        dayInput = binding.dayInput;
        monthTitle = binding.monthTitle;
        monthInput = binding.monthInput;
        yearTitle = binding.yearTitle;
        yearInput = binding.yearInput;

        Button submitBtn = binding.submitBtn;
        Button backBtn = binding.backBtn;
        Button logBtn = binding.logBtn;

        presenter.setTime();

        timeOfDay = binding.timeOfDaySpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_of_day, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeOfDay.setAdapter(adapter);

        timeOfDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setSelectedTime(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean firstDone = sp.getBoolean("firstDone", false);
                if (!firstDone) {
                    sp.edit().putBoolean("firstDone", true).apply();
                }

                String time = timeOfDay.getSelectedItem().toString();
                presenter.checkInput(time);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileView.class);
                startActivity(intent);
            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FeedView.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onIncompleteInput() {
        Toast.makeText(getApplicationContext(), getString(R.string.fill_all), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCompleteInput() {
        String dayNight = timeOfDay.getSelectedItem().toString();
        if (dayNight.equals("Day")) {
            saveDailyWeight();
        } else if (dayNight.equals("Night")) {
            getMorningDaily();
        }


    }

    @Override
    public boolean checkInput(String time) {
        if (time.equals("Day")) {
            return exists(weightInput) && exists(dayInput) && exists(monthInput) && exists(yearInput);
        } else {
            return exists(weightInput);
        }

    }

    @Override
    public void setMorning(int day, int month, int year) {
        dayInput.setVisibility(View.VISIBLE);
        dayTitle.setVisibility(View.VISIBLE);
        monthInput.setVisibility(View.VISIBLE);
        monthTitle.setVisibility(View.VISIBLE);
        yearTitle.setVisibility(View.VISIBLE);
        yearInput.setVisibility(View.VISIBLE);

        dayInput.setText(day + "");
        monthInput.setText(month + "");
        yearInput.setText(year + "");
    }

    @Override
    public void setNight() {
        dayInput.setVisibility(View.GONE);
        dayTitle.setVisibility(View.GONE);
        monthInput.setVisibility(View.GONE);
        monthTitle.setVisibility(View.GONE);
        yearTitle.setVisibility(View.GONE);
        yearInput.setVisibility(View.GONE);
    }

    private boolean exists(EditText editText) {
        return editText.getText().toString().length() >= 1;
    }

    private void saveNightlyWeight(final Daily morningDaily) {
        final double weight = Double.parseDouble(weightInput.getText().toString());
        UserProps props = new UserProps(getApplicationContext());
        props.setCurrentWeight(weight);
        class SaveWeight extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                Daily daily = morningDaily;
                daily.setNightWeight(weight);
                double dailyWeight = daily.getDayWeight();
                double avg = (dailyWeight + weight) / 2;
                daily.setDailyAvg(avg);
                DatabaseClient.getInstance(getApplicationContext()).getDatabase().dailyDao()
                        .postNight(daily);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                showDialog("Night", weight, morningDaily.getDayNo());
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }
        SaveWeight sw = new SaveWeight();
        sw.execute();
    }

    private void saveDailyWeight() {
        final double weight = Double.parseDouble(weightInput.getText().toString());
        final int day = Integer.parseInt(dayInput.getText().toString());
        final int month = Integer.parseInt(monthInput.getText().toString());
        final int year = Integer.parseInt(yearInput.getText().toString());
        UserProps props = new UserProps(getApplicationContext());
        props.setCurrentWeight(weight);
        class SaveWeight extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                Daily daily = new Daily();

                daily.setDayWeight(weight);
                daily.setDay(day);
                daily.setMonth(month);
                daily.setYear(year);

                DatabaseClient.getInstance(getApplicationContext()).getDatabase().dailyDao()
                        .postDay(daily);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                showDialog("Day", weight, 0);
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        SaveWeight sw = new SaveWeight();
        sw.execute();
    }

    private void getMorningDaily() {
        class DayID extends AsyncTask<Void, Void, Daily> {

            @Override
            protected Daily doInBackground(Void... voids) {
                Daily morningDaily = DatabaseClient.getInstance(getApplicationContext()).getDatabase()
                        .dailyDao().getPrevDay();
                return morningDaily;
            }

            @Override
            protected void onPostExecute(Daily morningDaily) {
                super.onPostExecute(morningDaily);
                saveNightlyWeight(morningDaily);
            }
        }

        DayID id = new DayID();
        id.execute();
    }

    private void showDialog(final String timeOfDay, final double weight, final int primaryKey) {
        AlertDialog.Builder builder = new AlertDialog.Builder(InputView.this);
        builder.setMessage(R.string.weekly_or_daily);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.dailyDialogBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), DailyResultsView.class);
                if (timeOfDay.equals("Night")) {
                    intent.putExtra("Night", true);
                    intent.putExtra("Weight", weight);
                    intent.putExtra("PrimaryKey", primaryKey);
                }
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
