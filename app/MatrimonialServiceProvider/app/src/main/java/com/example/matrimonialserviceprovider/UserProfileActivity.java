package com.example.matrimonialserviceprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.matrimonialserviceprovider.databinding.ActivityUserProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileActivity extends AppCompatActivity {

    ActivityUserProfileBinding binding;
    SharedPreferences sharedPreferences;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
        email = sharedPreferences.getString("email","");
        String profileLink = getIntent().getStringExtra("profileLink");

//        http://192.168.1.102//MatrimonialServiceProvider/getAllUser.php?currentUserEmail=" + email
        String url = "http://"+Constants.my_ip_address+"//MatrimonialServiceProvider/userProfile.php?email=" + email;
        RequestQueue queue = Volley.newRequestQueue(UserProfileActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("name");
                            String email1 = response.getString("email");
                            String mobile = response.getString("Mobile");
                            String address = response.getString("City");
                            String gender = response.getString("gender");
                            String salary = response.getString("salary");
                            String cast = response.getString("caste");
                            String imageUrl = response.getString("image");

                            binding.userName.setText(name);
                            binding.userEmail.setText(email1);
                            binding.userTxtName.setText(name);
                            binding.userTxtMobile.setText(mobile);
                            binding.userTxtAddress.setText(address);
                            binding.userTxtGender.setText(gender);
                            binding.userTxtSalary.setText(salary);
                            binding.userTxtCast.setText(cast);
                            Glide.with(UserProfileActivity.this
                                    )
                                    .load(imageUrl)
                                    .error(R.drawable.error_image)
                                    .into(binding.userProfile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserProfileActivity.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);

        binding.imageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("prefLoginState","loggedout");
                        editor.apply();
                startActivity(new Intent(UserProfileActivity.this,MainActivity.class));
                finish();

            }
        });
    }
}