package com.example.matrimonialserviceprovider;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.matrimonialserviceprovider.databinding.ActivityFeedbackBinding;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class feedbackActivity extends AppCompatActivity {

    ActivityFeedbackBinding binding;
    SharedPreferences sharedPreferences;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(" FeedBack ");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
        email = sharedPreferences.getString("email","");

        Toast.makeText(this, ""+email, Toast.LENGTH_LONG).show();

        LoadUserName();

        binding.feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFeedback();
            }
        });

    }
    private void LoadUserName(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

      //  String url = "http://192.168.1.101//MatrimonialServiceProvider/getusername.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, Constants.LOAD_USERNAME_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> userNames = new ArrayList<>();
                        userNames.add("select Name");
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                String userName = response.getString(i);

                                userNames.add(userName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Step 3: Create an ArrayAdapter with the user names
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(feedbackActivity.this,
                                android.R.layout.simple_spinner_item, userNames);

                        // Step 4: Set the ArrayAdapter as the adapter for the Spinner
                        binding.loadName.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(feedbackActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
                    }
                });

        // Step 5: Add the request to the RequestQueue
        requestQueue.add(request);
    }

    private void AddFeedback(){

//        String url = "http://192.168.1.102//MatrimonialServiceProvider/addfeedback.php";
        ProgressDialog progressDialog = new ProgressDialog(feedbackActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Feedback Sending...");
        progressDialog.show();

        String name = binding.loadName.getItemAtPosition(binding.loadName.getSelectedItemPosition()).toString();
        String username = binding.editTextNameBox.getText().toString();
        String message = binding.editTextMessage.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(message)) {
            progressDialog.dismiss();
            Toast.makeText(this, "All Fields Are Required.", Toast.LENGTH_SHORT).show();
        }else {
            StringRequest request = new StringRequest(Request.Method.POST, Constants.ADD_FEEDBACK_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("Feedback Add Successfully")){
                        progressDialog.dismiss();
                        Toast.makeText(feedbackActivity.this, response, Toast.LENGTH_LONG).show();
                        startActivity( new Intent(feedbackActivity.this,UserActivity.class));
                        finish();
                    }else {
                        progressDialog.dismiss();
                        Log.e("First if Error",response);
                        Toast.makeText(feedbackActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.e("ERROR",error.toString());
                    Toast.makeText(feedbackActivity.this, "Errorr", Toast.LENGTH_LONG).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> param = new HashMap<>();
                    param.put("name",name);
                    param.put("username",username);
                    param.put("message",message);
                    param.put("email",email);
                    return param;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
            MySingleton.getmInstance(feedbackActivity.this).addToRequestQueue(request);
        }
    }
}
