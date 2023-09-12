package com.example.matrimonialserviceprovider;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonialserviceprovider.Admin.AdminActivity;
import com.example.matrimonialserviceprovider.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String loginStatus = sharedPreferences.getString("prefLoginState","");
        if (loginStatus.equals("loggedin")){
            startActivity(new Intent(MainActivity.this,UserActivity.class));
        }

        binding.textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                finish();
            }
        });
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTxt = binding.editTextEmail.getText().toString();
                String passwordTxt = binding.editTextTextPassword.getText().toString();

                if (TextUtils.isEmpty(emailTxt) || TextUtils.isEmpty(passwordTxt)){
                    Toast.makeText(MainActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                }else {
                    login(emailTxt,passwordTxt);
                }
            }

        });
    }
    private void  login(String email,String password){
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Login An Account");
        progressDialog.show();

        if (email.equals("admin@gmail.com") && password.equals("123456")){
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);
        }else {
            // for run in emulator change the ip address of the your internet
            //String url = "http://192.168.1.101//MatrimonialServiceProvider/login.php";
            StringRequest request = new StringRequest(Request.Method.POST, Constants.LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject arr = null;
                    try {
                        arr = new JSONObject(response);
                        Log.d("response", String.valueOf(arr));
                    }catch (Exception e){

                    }


                    if (arr!=null){
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        if (binding.checkbox.isChecked()){
                            editor.putString("prefLoginState","loggedin");
                            editor.putString("email",email);

                            try {
                                editor.putString("gender", arr.getString("gender"));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }else {
                            editor.putString("prefLoginState","loggedout");
                            editor.putString("email",email);

                        }
                        editor.apply();
                        startActivity(new Intent(MainActivity.this,UserActivity.class));


                    }else if(response.equals("blocked")){
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"Your account has been blocked. Please contact the administrator.", Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog.dismiss();
                        Log.e("ERR",response);
                        Toast.makeText(MainActivity.this,"Invalid Email And Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.e("ERORR",error.toString());
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();

                    param.put("email", email);
                    param.put("password", password);
                    return param;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
            MySingleton.getmInstance(MainActivity.this).addToRequestQueue(request);
        }


    }
}