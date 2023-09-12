package com.example.matrimonialserviceprovider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.matrimonialserviceprovider.Adapters.FeedbackAdapter;
import com.example.matrimonialserviceprovider.Adapters.UserAdapter;
import com.example.matrimonialserviceprovider.Models.FeedbackModel;
import com.example.matrimonialserviceprovider.Models.UserModel;
import com.example.matrimonialserviceprovider.databinding.ActivityViewFeedbackBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewFeedbackActivity extends AppCompatActivity {

    ActivityViewFeedbackBinding binding;

    FeedbackAdapter adapter;

    List<FeedbackModel> feedbackList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.feedbackRecyclerView.setHasFixedSize(true);
        binding.feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        LoadAllFeedback();
    }

    private void LoadAllFeedback() {

        String url = "http://"+Constants.my_ip_address+"//MatrimonialServiceProvider/viewfeedback.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i <response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String name = object.getString("name");
                        //  String id = object.getString("Id");
                        String email =object.getString("email");
                       String username = object.getString("username");
                        String message = object.getString("message");


                        FeedbackModel feedback = new FeedbackModel();
                        feedback.setName(name);
                     //  feedback.setId(id);
                        feedback.setEmail(email);
                       feedback.setUsername(username);
                        feedback.setMessage(message);
                        feedbackList.add(feedback);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                adapter = new FeedbackAdapter(ViewFeedbackActivity.this,feedbackList);
                binding.feedbackRecyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR",error.toString());
                Toast.makeText(ViewFeedbackActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ViewFeedbackActivity.this);
        requestQueue.add(request);
    }
}