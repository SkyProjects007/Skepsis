package com.mxicoders.skepci.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxicoders.skepci.R;
import com.mxicoders.skepci.activity.ToDoSleepClickTwo;
import com.mxicoders.skepci.activity.ToDoEmotionClickActivity;
import com.mxicoders.skepci.activity.ToDoMoodClickActivity;
import com.mxicoders.skepci.activity.ToDoQuestionnarClickActivity;
import com.mxicoders.skepci.model.ToDoListData;
import com.mxicoders.skepci.network.CommanClass;

import java.util.List;

/**
 * Created by mxicoders on 24/7/17.
 */

public class ToDoYesterdayAdapter extends RecyclerView.Adapter<ToDoYesterdayAdapter.ViewHolder> {
    private List<ToDoListData> countries;
    private int rowLayout;
    public static Context mContext;
    public static Fragment newFragment;

    static String title = "";

    CommanClass cc;

    public ToDoYesterdayAdapter(List<ToDoListData> countries, int rowLayout, Context context) {

        cc = new CommanClass(context);

        this.countries = countries;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }
    @Override
    public int getItemCount() {
        return countries == null ? 0 : countries.size();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        ToDoListData myItem = countries.get(i);

        if (countries.get(i).getName().equals("Sleep")){

            viewHolder.Name.setText(countries.get(i).getName());

        }else {

            viewHolder.Name.setText(countries.get(i).getName() + " " +"-"+" "+ countries.get(i).getTiming());

        }

        viewHolder.d_date.setText(myItem.getD_date());
        viewHolder.d_day.setText(myItem.getD_day());

        viewHolder.lnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToDoListData modelTwo = countries.get(i);

                Log.i("Position",countries.get(i).getName());
                if (modelTwo.getName().contains("Emotions")){

                    Intent emotion = new Intent(mContext, ToDoEmotionClickActivity.class);

                    emotion.putExtra("task_id",countries.get(i).getTask_id());
                    emotion.putExtra("time",countries.get(i).getTiming());
                    emotion.putExtra("date",countries.get(i).getFull_date());

                    ((Activity)mContext).finish();

                    mContext.startActivity(emotion);

                }else if(modelTwo.getName().contains("Sleep")){

                    Intent sleep = new Intent(mContext, ToDoSleepClickTwo.class);
                    sleep.putExtra("task_id",countries.get(i).getTask_id());
                    sleep.putExtra("date",countries.get(i).getFull_date());
                    sleep.putExtra("assign_date",countries.get(i).getAssign_date());
                    ((Activity)mContext).finish();
                    mContext.startActivity(sleep);
                }else if (modelTwo.getName().contains("Mood")){

                    Intent mood = new Intent(mContext, ToDoMoodClickActivity.class);

                    mood.putExtra("task_id",countries.get(i).getTask_id());
                    mood.putExtra("time",countries.get(i).getTiming());
                    mood.putExtra("date",countries.get(i).getFull_date());
                    ((Activity)mContext).finish();

                    mContext.startActivity(mood);
                }else if (modelTwo.getName().contains("Question")){

                    Intent t_history = new Intent(mContext, ToDoQuestionnarClickActivity.class);

                    t_history.putExtra("task_id",countries.get(i).getTask_id());
                    cc.savePrefString("task_id",countries.get(i).getTask_id());
                    t_history.putExtra("time",countries.get(i).getTiming());

                    ((Activity)mContext).finish();

                    mContext.startActivity(t_history);
                }else {


                }

            }
        });


    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Name,d_date,d_day;

        public LinearLayout lnClick;

        public ViewHolder(View itemView) {
            super(itemView);



            Name = (TextView) itemView.findViewById(R.id.text_name);
            d_date= (TextView) itemView.findViewById(R.id.tv_date_list);
            d_day= (TextView) itemView.findViewById(R.id.tv_day_list);
            lnClick = (LinearLayout) itemView.findViewById(R.id.ln_item_click);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }


    }}
