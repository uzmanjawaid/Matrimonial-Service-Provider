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
import com.example.matrimonialserviceprovider.Adapters.UnblockAdapter;
import com.example.matrimonialserviceprovider.Adapters.UserAdapter;
import com.example.matrimonialserviceprovider.Models.UserModel;
import com.example.matrimonialserviceprovider.databinding.ActivityUnblockBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UnblockActivity extends AppCompatActivity {

    ActivityUnblockBinding binding;
    UnblockAdapter adapter;

    List<UserModel> userList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUnblockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.UnblockRecyclerView.setHasFixedSize(true);
        binding.UnblockRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load All User Data
        LoadAllUsers();

    }

    private void LoadAllUsers() {

       // String url = "http://192.168.1.101//MatrimonialServiceProvider/getuser.php";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,Constants.GET_USER_URL,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i <response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String name = object.getString("name");
                        String id = object.getString("id");
                        String email =object.getString("email");
                        String imageUrl = object.getString("image");
                        String gender = object.getString("status");



                        String url1 = "http://192.168.123.236//MatrimonialServiceProvider/Images/"+imageUrl;
                        UserModel user = new UserModel();
                        user.setName(name);
                        user.setId(id);
                        user.setEmail(email);
                        user.setImageUrl(url1);
                        user.setStatus(gender);
                        userList.add(user);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                adapter = new UnblockAdapter(UnblockActivity.this,userList);
                binding.UnblockRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR",error.toString());
                Toast.makeText(UnblockActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(UnblockActivity.this);
        requestQueue.add(request);
    }
}