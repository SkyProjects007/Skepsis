package com.mxicoders.skepci.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.QuestionResponseTodoAdapter;
import com.mxicoders.skepci.adapter.SleepAdapterOne;
import com.mxicoders.skepci.model.QuestionDetailsData;
import com.mxicoders.skepci.model.ToDoData;
import com.mxicoders.skepci.model.ToDoListData;
import com.mxicoders.skepci.network.CommanClass;
import com.mxicoders.skepci.network.Const;
import com.mxicoders.skepci.utils.AndyUtils;
import com.mxicoders.skepci.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mxicoders on 24/7/17.
 */

public class ToDoQuestionnarClickActivity extends AppCompatActivity {

    CommanClass cc;
    ProgressDialog pDialog;

    String task_id,timing,question_title_id,question_id,is_answered;
    String option = null;

    TextView tvQuestion;

    RadioGroup rg_option_group;
    RadioButton rb_option_1,rb_option_2,rb_option_3,rb_option_4;


    TextView tvTaskSave,tv_total_question_count;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    LinearLayout ln_option_select_number;



    RecyclerView rc_question_number;
    QuestionResponseTodoAdapter response_number_adapter;
    ArrayList<ToDoData> list;

    String question_position;


    public static List<QuestionDetailsData> question_respons_list;
    public static List<QuestionDetailsData> question_respons_list_count;

    public ToDoQuestionnarClickActivity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.frag_patient_todo_questionar_click);
        super.onCreate(savedInstanceState);

        cc = new CommanClass(ToDoQuestionnarClickActivity.this);

        task_id = getIntent().getStringExtra("task_id");
        //timing = getIntent().getStringExtra("time");


       /* LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));*/

        if (cc.loadPrefString3("question_position").isEmpty()){

            question_position = "0";

        }else {

            question_position = cc.loadPrefString3("question_position");
        }


        init();

    }
/*
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
        }
    };*/

    private void init() {

        rg_option_group = (RadioGroup) findViewById(R.id.rg_option_group);

        rb_option_1 = (RadioButton)findViewById(R.id.rb_option_1);
        rb_option_2 = (RadioButton)findViewById(R.id.rb_option_2);
        rb_option_3 = (RadioButton)findViewById(R.id.rb_option_3);
        rb_option_4 = (RadioButton)findViewById(R.id.rb_option_4);

        tvQuestion = (TextView)findViewById(R.id.tv_question);
        ln_option_select_number = (LinearLayout) findViewById(R.id.ln_option_select_number);




        tvTaskSave = (TextView)findViewById(R.id.tv_task_save);
        tv_total_question_count = (TextView)findViewById(R.id.tv_total_question_count);

        LinearLayoutManager llm = new LinearLayoutManager(this);

        rc_question_number = (RecyclerView) findViewById(R.id.rc_question_number);

        llm.setStackFromEnd(true);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
//        rc_question_number.setLayoutManager(new LinearLayoutManager(ToDoQuestionnarClickActivity.this, LinearLayoutManager.HORIZONTAL, true));
        rc_question_number.setLayoutManager(llm);
        rc_question_number.setItemAnimator(new DefaultItemAnimator());

        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText("Questionnaries");
        tooName.setPadding(80, 0, 0, 0);


        rg_option_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_option_1:

                        option = ((RadioButton)findViewById(rg_option_group.getCheckedRadioButtonId())).getText().toString();;

                        Log.i("@@1RadioClick",option);
                        break;
                    case R.id.rb_option_2:

                        option = ((RadioButton)findViewById(rg_option_group.getCheckedRadioButtonId())).getText().toString();;
                        Log.i("@@2RadioClick",option);
                        break;

                    case R.id.rb_option_3:

                        option = ((RadioButton)findViewById(rg_option_group.getCheckedRadioButtonId())).getText().toString();;
                        Log.i("@@3RadioClick",option);

                        break;

                    case R.id.rb_option_4:

                        option = ((RadioButton)findViewById(rg_option_group.getCheckedRadioButtonId())).getText().toString();;
                        Log.i("@@4RadioClick",option);

                        break;
                }

            }
        });



        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent backToDo = new Intent(ToDoQuestionnarClickActivity.this,TodoPatientMenuActivity.class);

                startActivity(backToDo);
                finish();
            }
        });


        tvTaskSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (option == null){

                  //  option = tvOptionOne.getText().toString().trim();

                    AndyUtils.showToast(ToDoQuestionnarClickActivity.this,getString(R.string.option_select));

                }else {

                    postQuestionResponseValidation(option);

                    Log.e("SelectedOption",option);
                }


            }
        });


    }

    private void postQuestionResponseValidation(String option) {

        Log.i("SelectedOption",option);

        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(ToDoQuestionnarClickActivity.this,getString(R.string.no_internet));
        } else if (this.option.equals("")) {
            AndyUtils.showToast(ToDoQuestionnarClickActivity.this,getString(R.string.option_select));
        }
        else {

            if (is_answered.equals("Yes")){

                cc.showToast("Already answered");

            }else {

                postQuestionResponse(task_id,timing,option,question_title_id,question_id);
            }


        }

    }


    private void getQuestionDetails(final String task_id) {

        pDialog = new ProgressDialog(ToDoQuestionnarClickActivity.this);
        pDialog.setMessage("Please Wait");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_QUESTION_DETAIL,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:question data", response);

                        question_respons_list = new ArrayList<>();
                        question_respons_list_count = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();

                                JSONArray dataArray = jsonObject.getJSONArray("question_detail");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    QuestionDetailsData model = new QuestionDetailsData();


                                    model.setTask_id(jsonObject1.getString("id"));
                                    model.setQuestion(jsonObject1.getString("question"));
                                    model.setTiming(jsonObject1.getString("timing"));
                                    model.setOption1(jsonObject1.getString("option1"));
                                    model.setOption2(jsonObject1.getString("option2"));
                                    model.setOption3(jsonObject1.getString("option3"));
                                    model.setOption4(jsonObject1.getString("option4"));
                                    model.setTask_type(jsonObject1.getString("task_type"));
                                    model.setQuestion_title_id(jsonObject1.getString("title_id"));
                                    model.setQuestion_id(jsonObject1.getString("question_id"));
                                    model.setTiming(jsonObject1.getString("timing"));
                                    model.setIs_answered(jsonObject1.getString("is_answered"));

                                    question_respons_list.add(model);

                                    model.setNumber_count(String.valueOf(question_respons_list.size()));

                                    question_respons_list_count.add(model);

                                }

                                if(response_number_adapter != null){
                                    response_number_adapter = null;
                                }

                                response_number_adapter = new QuestionResponseTodoAdapter(question_respons_list_count, R.layout.question_response_todo_item,ToDoQuestionnarClickActivity.this);
                                rc_question_number.setAdapter(response_number_adapter);


                                for (int in = 0; in < question_respons_list.size(); in++){

                                    QuestionDetailsData qs = question_respons_list.get(in);

                                    if (in == Integer.parseInt(question_position)){

                                        rb_option_1.setText(qs.getOption1());
                                        rb_option_2.setText(qs.getOption2());
                                        rb_option_3.setText(qs.getOption3());
                                        rb_option_4.setText(qs.getOption4());

                                        tvQuestion.setText(qs.getQuestion());

                                        question_title_id = qs.getQuestion_title_id();
                                        question_id = qs.getQuestion_id();
                                        timing = qs.getTiming();
                                        is_answered = qs.getIs_answered();

                                        tv_total_question_count.setText(in+1 + "/" + question_respons_list.size());

                                        cc.logoutapp3();
                                    }

                                }


                            } else {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(ToDoQuestionnarClickActivity.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("task_id",task_id);
                Log.e("request question data", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    // Changes 19/12

    private void postQuestionResponse(final String task_id, final String timing, final String option, final String question_title_id, final String question_id) {

        pDialog = new ProgressDialog(ToDoQuestionnarClickActivity.this);
        pDialog.setMessage("Please Wait");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.QUESTION_RESPONSE,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:questionp data", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                pDialog.dismiss();

                                final Dialog dialog = new Dialog(ToDoQuestionnarClickActivity.this);
                                dialog.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                dialog.setContentView(R.layout.task_saved_dialog);
                                dialog.setCancelable(false);
                                dialog.show();

                                final Handler handler  = new Handler();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();


                                            Intent backToDo = new Intent(ToDoQuestionnarClickActivity.this,TodoPatientMenuActivity.class);

                                            startActivity(backToDo);
                                            finish();

                                        }
                                    }
                                };

                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        handler.removeCallbacks(runnable);
                                    }
                                });

                                handler.postDelayed(runnable, 5000);

                            } else {
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(ToDoQuestionnarClickActivity.this,getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("task_id",task_id);
                params.put("answer",option);
                params.put("question_title_id",question_title_id);
                params.put("question_id",question_id);
                params.put("timing",timing);
                Log.e("request questionpo data", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    @Override
    protected void onResume() {

        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(ToDoQuestionnarClickActivity.this,getString(R.string.no_internet));
        }
        else {
            getQuestionDetails(task_id);
        }

        super.onResume();

        if (cc.loadPrefBoolean("isInactiveDevice") || cc.loadPrefBoolean("isDialogOpen") ){


            Log.e("IsLoginDevice","Psychologist...If");

            cc.savePrefBoolean("isInactiveDevice",false);

        }else {

            if (cc.loadPrefBoolean("Inactive")) {

                cc.savePrefBoolean("Inactive",false);

                cc.savePrefBoolean("isDialogOpen",true);

                cc.showInactiveDialog();
            }


            Log.e("IsLoginDevice","Psychologist...Else");
        }

    }
}
