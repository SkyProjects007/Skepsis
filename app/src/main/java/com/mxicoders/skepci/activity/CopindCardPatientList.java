package com.mxicoders.skepci.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxicoders.skepci.R;
import com.mxicoders.skepci.adapter.CopingCardAdapter;
import com.mxicoders.skepci.model.CopingData;
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
 * Created by mxicoders on 18/8/17.
 */

public class CopindCardPatientList extends AppCompatActivity {

    CommanClass cc;

    RelativeLayout toolbar;
    TextView tooName;
    ImageView imgToolOne,imgToolTwo,imgToolBack;

    EditText edMessage;
    Button btnSub;

    String message,target_patient_id;
    ProgressDialog pDialog,pDialog2;

    GridView gridMessage;
    CopingCardAdapter cp;
    public static List<CopingData> card_names;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.coping_card_patientlist);

        cc = new CommanClass(this);

        target_patient_id = cc.loadPrefString("patient_id_main");

        edMessage = (EditText)findViewById(R.id.ed_coping_message);
        btnSub = (Button)findViewById(R.id.btn_coping_sub);

        gridMessage = (GridView) findViewById(R.id.gridMessage);



        toolbar = (RelativeLayout)findViewById(R.id.toolbarr);
        tooName = (TextView)findViewById(R.id.tv_tool_name);
        imgToolBack = (ImageView)findViewById(R.id.back_toolbar);
        imgToolOne = (ImageView)findViewById(R.id.img_tool_one);
        imgToolTwo = (ImageView)findViewById(R.id.img_tool_two);

        imgToolOne.setVisibility(View.GONE);
        imgToolTwo.setVisibility(View.GONE);
        tooName.setText("Coping Cards");
        tooName.setPadding(80, 0, 0, 0);

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CopingCard();
            }
        });
    }

    private void CopingCard() {

        message = edMessage.getText().toString().trim();

        if (!cc.isConnectingToInternet()) {
            AndyUtils.showToast(CopindCardPatientList.this,getString(R.string.no_internet));
        } else if (edMessage.equals("")) {
            AndyUtils.showToast(CopindCardPatientList.this,getString(R.string.coping_message));
        }else {
            createCopingCard(message,target_patient_id);
        }

    }

    private void createCopingCard(final String message, final String target_patient_id) {

        pDialog = new ProgressDialog(CopindCardPatientList.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.COPING_CARD_CREATE,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:createdCoping", response);

                        jsonParseCreateCopingCard(response);
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                AndyUtils.showToast(CopindCardPatientList.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("message", message);
                params.put("target_user_id", target_patient_id);

                Log.i("request copingcard", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put(Const.Params.Authorization, Const.Params.Authorization_value);
                Log.i("request header", headers.toString());
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    private void jsonParseCreateCopingCard(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            pDialog.dismiss();
            if (jsonObject.getString("status").equals("200")) {

                cc.showToast(jsonObject.getString("message"));

                getCopingCard();

            } else {

                cc.showToast(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            AndyUtils.showToast(CopindCardPatientList.this,getString(R.string.ws_error));
        }
    }

    private void getCopingCard() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_COPING_CARD,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:getCoping", response);

                        card_names = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                JSONArray dataArray = jsonObject.getJSONArray("Coping Card detail");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);

                                    CopingData model = new CopingData();

                                    model.setMessage(jsonObject1.getString("message"));
                                    model.setId(jsonObject1.getString("id"));

                                    card_names.add(model);

                                }


                                cp = new CopingCardAdapter(getApplicationContext(),card_names,CopindCardPatientList.this);
                                gridMessage.setAdapter(cp);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("get card Error", e.toString());
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                AndyUtils.showToast(CopindCardPatientList.this, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("target_user_id",target_patient_id);

                Log.i("request getcard", params.toString());

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put(Const.Params.Authorization, Const.Params.Authorization_value);
                Log.i("request header", headers.toString());
                return headers;
            }


        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }


    @Override
    protected void onResume() {
        super.onResume();

        getCopingCard();

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
