package com.example.matrimonialserviceprovider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.matrimonialserviceprovider.databinding.ActivityProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    private static final String TAG = "ProfileActivity";
    SharedPreferences sharedPreferences;
    String senderEmail;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        String name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        String gender = getIntent().getStringExtra("gender");
        String imageUrl = getIntent().getStringExtra("image");
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        senderEmail = sharedPreferences.getString("email", "");
        Toast.makeText(this, "" + senderEmail, Toast.LENGTH_SHORT).show();
//        binding.txtName.setText(name);
//        binding.txtEmail.setText(email);
//        binding.txtEmail.setText(gender);

        Glide.with(ProfileActivity.this)
                .load(imageUrl)
                .error(R.drawable.error_image)
                .into(binding.profileImage);
//        binding.profileWebView.getSettings().setJavaScriptEnabled(true);
//        binding.profileWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                // Get the URL of the clicked link
//                String url = request.getUrl().toString();
//
//                Log.d("url_profile",  url);
//                // Check if it's the profile link you want to handle
//                if (url.equals("http://"+Constants.my_ip_address+"//MatrimonialServiceProvider/userProfile.php?email=" + senderEmail)) {
//                    // Handle the URL in your app
//                    // You can navigate to a specific activity or perform any other action
//                    // Example: Open ProfileActivity
//                    Intent intent = new Intent(ProfileActivity.this, UserProfileActivity.class);
//                    intent.putExtra("profileLink", url);
//                    startActivity(intent);
//                    return true;  // Return true to indicate that the URL is handled by your app
//                }else {
//                    view.loadUrl(url);
//                    return true;
//                }
//
//                // Let WebView handle other URLs
//                //return super.shouldOverrideUrlLoading(view, request);
//            }
//        });

        // Load the initial URL
       // binding.profileWebView.loadUrl("http://"+Constants.my_ip_address+"//MatrimonialServiceProvider/userProfile.php?email=" + senderEmail);

        loadData();
        binding.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profileLink = "http://"+Constants.my_ip_address+"//MatrimonialServiceProvider/userProfile.php?email=" + senderEmail;
                sendProfileLink(email, profileLink);
                Toast.makeText(ProfileActivity.this, "Email Send", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private  void loadData(){
        String url = "http://"+Constants.my_ip_address+"//MatrimonialServiceProvider/userProfile.php?email=" + email;
        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
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
//                            Glide.with(ProfileActivity.this
//                                    )
//                                    .load(imageUrl)
//                                    .error(R.drawable.error_image)
//                                    .into(binding.userProfile);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }
    private void sendProfileLink(String email, String profileLink) {
        OkHttpClient client = new OkHttpClient();


        // Create the request body
        RequestBody requestBody = new FormBody.Builder()
                .add("email", email)
                .add("profile_link", profileLink)
                .build();

        // Create the request
        okhttp3.Request request = new Request.Builder()
                .url("http://"+Constants.my_ip_address+"//MatrimonialServiceProvider/sendemail.php")  // Replace with your PHP script URL
                .post(requestBody)
                .build();

        // Execute the request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    // Handle the successful response
                    Log.d(TAG, "Response: " + responseData);
                } else {
                    // Handle the response failure
                    Log.e(TAG, "Response failed: " + response.code());
                }
                response.close();

            }
        });
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // Handle the failure
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String responseData = response.body().string();
//                    // Handle the successful response
//                    Log.d("ProfileACtivity", "Response: " + responseData);
//                } else {
//                    // Handle the response failure
//                    Log.e("ProfileACtivity", "Response failed: " + response.code());
//                }
//                response.close();
//            }
//        });

    }
}