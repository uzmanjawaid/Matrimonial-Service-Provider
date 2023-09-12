package com.example.matrimonialserviceprovider;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.matrimonialserviceprovider.databinding.ActivityRegisterBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    ActivityRegisterBinding binding;
    int genderId;
    private static final int PICK_IMAGE_REQUEST = 10;
    String selectedPicture = "";
    String[]list = {"Select Religion","Muslim","Non-Muslim"};

    // ip address 192.168.1.101
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle(" User Register ");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.religion.setAdapter(arrayAdapter);
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        binding.RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidSignDetails()){
                    Register();
                }
            }
        });
        binding.textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                finish();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void showToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.costum_toast_layout,
                (ViewGroup) findViewById(R.id.costume_toast));
        TextView toastTextView = (TextView) layout.findViewById(R.id.txt_message);
        toastTextView.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG); // set the duration for the Toast
        toast.setView(layout); // set the inflated layout
        toast.show(); // display the custom Toast

    }
    private boolean isValidSignDetails(){
        if (binding.editTextName.getText().toString().trim().isEmpty()){
            showToast("Enter your Name.!");
            binding.editTextName.setError("Enter Name.");
            binding.editTextName.requestFocus();
            return false;
        }else if(binding.editTextEmail.getText().toString().trim().isEmpty()){
            showToast("Enter your email.!");
            binding.editTextEmail.setError("Enter email!");
            binding.editTextEmail.requestFocus();
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.editTextEmail.getText().toString()).matches()) {
            showToast("Enter Valid Email");
            binding.editTextEmail.setError(" Enter Valid Email ");
            binding.editTextEmail.requestFocus();
            return false;
        }else if (binding.editTextPassword.getText().toString().isEmpty()){
            showToast("Enter Password");
            binding.editTextPassword.setError("Enter Password");
            binding.editTextPassword.requestFocus();
            return false;

        }else if (binding.editTextCast.getText().toString().trim().isEmpty()){
            binding.editTextCast.setError("Enter caste");
            binding.editTextCast.requestFocus();
            showToast("Enter your caste");
            return false;

        }else if (binding.editTextOccupation.getText().toString().trim().isEmpty()){
            binding.editTextOccupation.setError("Enter Occupation");
            binding.editTextOccupation.requestFocus();
            showToast("Enter your Occupation");
            return false;
        }else if (binding.editTextSalary.getText().toString().trim().isEmpty()){
            binding.editTextSalary.setError("Enter Salary");
            binding.editTextSalary.requestFocus();
            showToast("Enter your Salary");
            return false;
        }else if (binding.editTextMobileNo.getText().toString().trim().isEmpty()){
            binding.editTextMobileNo.setError("Enter Mobile No.");
            binding.editTextMobileNo.requestFocus();
            showToast("Enter Mobile No.");
            return false;
        }else if(binding.editTextCity.getText().toString().trim().isEmpty()) {
            binding.editTextMobileNo.setError("Enter City Name");
            binding.editTextMobileNo.requestFocus();
            showToast("Enter City Name");
            return false;
        }else {
            return true;
        }
    }
    private void Register(){
        String Name = binding.editTextName.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();
        String religion = binding.religion.getItemAtPosition(binding.religion.getSelectedItemPosition()).toString();
        String caste = binding.editTextCast.getText().toString().trim();
        String occupation = binding.editTextOccupation.getText().toString().trim();
        String salary = binding.editTextSalary.getText().toString().trim();
        String mobile = binding.editTextMobileNo.getText().toString().trim();
        String city = binding.editTextCity.getText().toString().trim();

        int genderId = binding.radioSex.getCheckedRadioButtonId();
        RadioButton selected_Gender = binding.radioSex.findViewById(genderId);
        int marryId = binding.radioMarry.getCheckedRadioButtonId();
        RadioButton selected_marry = binding.radioMarry.findViewById(marryId);
        if (selected_Gender==null || selected_marry==null ){
            Toast.makeText(RegisterActivity.this, "Kindly Select Gender and Marry", Toast.LENGTH_SHORT).show();
        }else {
            String selectGender = selected_Gender.getText().toString();
            String selectMarry= selected_marry.getText().toString();
            registerAccount(Name,email,password,religion,caste,occupation,salary,mobile,city,selectGender,selectMarry);
        }


    }

    private void registerAccount(String Name,String email,String password,String religion,String caste,String occupation,String salary,String mobile,String city,String gender,String marry ){
        ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Register New Account");
        progressDialog.show();
        //address 192.168.37.176
      // String url = "http://192.168.1.101//MatrimonialServiceProvider/register.php";

        StringRequest request =new StringRequest(Request.Method.POST, Constants.Register_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Successfully Registered")) {
                  progressDialog.dismiss();

                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                   progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                    Log.e("Eror",response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              progressDialog.dismiss();
                Log.e("ERROR",error.toString());
                Toast.makeText(RegisterActivity.this, "Errorr", Toast.LENGTH_LONG).show();

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("name",Name);
                param.put("email",email);
                param.put("password",password);
                param.put("gender",gender);
                param.put("religion",religion);
                param.put("caste",caste);
                param.put("Marital",marry);
                param.put("salary",salary);
                param.put("Occupation",occupation);
                param.put("Mobile",mobile);
                param.put("City",city);
                param.put("image",selectedPicture);
                param.put("status","Unblock");
                return param;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        MySingleton.getmInstance(RegisterActivity.this).addToRequestQueue(request);


    }
    private void loading(Boolean isLoading){
        if (isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                selectedPicture = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                byte[] bytesImage = Base64.decode(selectedPicture, Base64.DEFAULT);
                Glide.with(getApplicationContext())
                        .load(bytesImage)
                        .into(binding.profileImage);
                Toast.makeText(this, ""+selectedPicture, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}