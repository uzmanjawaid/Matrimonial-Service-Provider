package com.example.matrimonialserviceprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.matrimonialserviceprovider.Adapters.UserAdapter;
import com.example.matrimonialserviceprovider.Models.UserModel;
import com.example.matrimonialserviceprovider.databinding.ActivityUserBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {


    ActivityUserBinding binding;

    ArrayList<UserModel>userList = new ArrayList<>();
    ArrayList<UserModel>filterList = new ArrayList<>();

    UserAdapter adapter;

    SharedPreferences sharedPreferences;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.UserRecyclerView.setHasFixedSize(true);
        binding.UserRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        sharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
        email = sharedPreferences.getString("email","");
        // Load All User Data
        LoadAllUsers();


        binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_profile:
                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("prefLoginState","loggedout");
//                        editor.apply();
                        startActivity(new Intent(UserActivity.this,UserProfileActivity.class));
                        break;
                    case R.id.nav_like:
                        Intent intent = new Intent(UserActivity.this,feedbackActivity.class);
                        //intent.putExtra("users","user");
                        startActivity(intent);
                        break;
                    case R.id.nav_search:
                        break;

                }
                return true;
            }
        });

        binding.message1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    binding.UserRecyclerView.setAdapter(new UserAdapter(UserActivity.this,userList));
                    adapter.notifyDataSetChanged();
                }else {
                    Filter(s.toString());
                }
            }
        });
    }

    private void Filter(String text){
        filterList.clear();
        for (UserModel user:userList){
            if (user.getName().contains(text) ||
                    user.getAge().contains(text) ||
                    user.getReligion().contains(text)
            || user.getCaste().contains(text)
            || user.getMarital().contains(text)){
                filterList.add(user);
            }
        }
        binding.UserRecyclerView.setAdapter(new UserAdapter(UserActivity.this,filterList));
        adapter.notifyDataSetChanged();
    }

    private void LoadAllUsers() {

        String url = "http://"+Constants.my_ip_address+"//MatrimonialServiceProvider/getAllUser.php?currentUserEmail=" + email;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i <response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        String name = object.getString("name");
                        String id = object.getString("id");
                        String email =object.getString("email");
                        String imageUrl = object.getString("image");
                        String gender = object.getString("gender");
                        String religion  = object.getString("religion");
                        String caste = object.getString("caste");
                        String marital = object.getString("Marital");
                      //  String age = object.getString("age");


                        String url1 = "http://"+Constants.my_ip_address+"//MatrimonialServiceProvider/Images/"+imageUrl;
                        UserModel user = new UserModel();
                        user.setName(name);
                        user.setId(id);
                        user.setEmail(email);
                        user.setImageUrl(url1);
                        user.setCaste(caste);
                        user.setGender(gender);
                        user.setMarital(marital);
                        user.setReligion(religion);
                        String gen =  sharedPreferences.getString("gender", "");
                        if (!user.getGender().equals(gen)){
                            userList.add(user);
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                adapter = new UserAdapter(UserActivity.this,userList);
                binding.UserRecyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR",error.toString());
                Toast.makeText(UserActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue =Volley.newRequestQueue(UserActivity.this);
        requestQueue.add(request);
    }

//    private void LoadProfilePic(){
//        String url = "http://192.168.1.102//MatrimonialServiceProvider/getUserProfile.php?currentUserEmail=" + email;
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//
//                                String profileImageUrl = response.getString("image");
//                                Glide.with(UserActivity.this)
//                                        .load(profileImageUrl)
//                                        .error(R.drawable.error_image)
//                                        .into(binding.profileImage);
//
//                                Log.e("URL",profileImageUrl);
//
//                            new LoadImageTask().execute(profileImageUrl);
//
//                                // Use the profileImageUrl as needed (e.g., load and display the image)
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            // Handle JSON parsing error
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle the error here
//                    }
//                });
//
//        RequestQueue requestQueue =Volley.newRequestQueue(UserActivity.this);
//        requestQueue.add(request);
//    }

//     private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            String imageURL = strings[0];
//            Bitmap bitmap = null;
//            try {
//                URL url = new URL(imageURL);
//                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            if (bitmap != null) {
//                binding.profileImage.setImageBitmap(bitmap);
//            }
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.search,menu);
//        MenuItem item = menu.findItem(R.id.search);
//        //Search Method
//        SearchMethod(item);
//
//        return super.onCreateOptionsMenu(menu);
//    }
//    private void SearchMethod(MenuItem item){
//        SearchView searchView =(SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//    }
}