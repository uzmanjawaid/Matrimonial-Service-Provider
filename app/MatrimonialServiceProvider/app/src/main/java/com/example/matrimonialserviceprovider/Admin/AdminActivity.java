package com.example.matrimonialserviceprovider.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.matrimonialserviceprovider.R;
import com.example.matrimonialserviceprovider.UnblockActivity;
import com.example.matrimonialserviceprovider.ViewFeedbackActivity;
import com.example.matrimonialserviceprovider.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    ActivityAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.viewFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminActivity.this, ViewFeedbackActivity.class);
                startActivity(intent);
            }
        });
        binding.unBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(AdminActivity.this, UnblockActivity.class);
                startActivity(intent);
            }
        });
    }
}