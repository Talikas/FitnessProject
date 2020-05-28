package com.phrenologue.fitnessproject.activityFeed;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.phrenologue.fitnessproject.R;
import com.phrenologue.fitnessproject.database.Daily;
import com.phrenologue.fitnessproject.database.DatabaseClient;

import java.text.DecimalFormat;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.WeightHolder> {
    private List<Daily> dailies;
    private Context context;
    private LayoutInflater inflater;

    public FeedAdapter(Context context, List<Daily> dailies) {
        this.dailies = dailies;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public WeightHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_weight_feed, parent, false);
        return new WeightHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeightHolder holder, int position) {

        DecimalFormat df = new DecimalFormat("####.##");
        final Daily daily = dailies.get(position);
        Double yestAvg = daily.getDailyAvg();
        Double yestDay = daily.getDayWeight();
        Double yestNight = daily.getNightWeight();
        if (position > 0) {
            Daily yesterday = dailies.get(position - 1);
            yestAvg = yesterday.getDailyAvg();
            yestDay = yesterday.getDayWeight();
            yestNight = yesterday.getNightWeight();
        }
        String date = daily.getYear() + "." + daily.getMonth() + "." + daily.getDay();
        String dayNoStr = daily.getDayNo() + "";
        String avgChange = df.format(100 * ((yestAvg - daily.getDailyAvg())
                / yestAvg))
                + "";
        String avgStr = daily.getDailyAvg() + " (%" + avgChange + ")";
        String morningChange = df.format(100 * ((yestDay - daily.getDayWeight())
                / yestDay)) + "";
        String morningWStr = daily.getDayWeight() + " (%" + morningChange + ")";
        String nightChange = df.format(100 * ((yestNight - daily.getNightWeight())
                / yestNight)) + "";
        String nightWStr = daily.getNightWeight() + " (%" + nightChange + ")";
        holder.date.setText(date);
        holder.dayNo.setText(dayNoStr);
        holder.avg.setText(avgStr);
        holder.morningW.setText(morningWStr);
        holder.nightW.setText(nightWStr);
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delDay(context, daily);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dailies.size();
    }

    class WeightHolder extends RecyclerView.ViewHolder {
        private TextView date, dayNo, avg, morningW, nightW;
        private Button del;

        public WeightHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateTitle);
            dayNo = itemView.findViewById(R.id.dayNo);
            avg = itemView.findViewById(R.id.avg);
            morningW = itemView.findViewById(R.id.morningW);
            nightW = itemView.findViewById(R.id.nightWew);
            del = itemView.findViewById(R.id.del);
        }
    }

    private void delDay(final Context context, final Daily daily) {
        class DeleteDay extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context.getApplicationContext()).getDatabase().dailyDao().delDay(daily);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent(context, FeedView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Toast.makeText(context.getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
            }
        }
        DeleteDay delete = new DeleteDay();
        delete.execute();
    }
}
