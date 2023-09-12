package com.example.matrimonialserviceprovider.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.matrimonialserviceprovider.Constants;
import com.example.matrimonialserviceprovider.Models.UserModel;
import com.example.matrimonialserviceprovider.R;
import com.example.matrimonialserviceprovider.databinding.ItemUserBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

public class UnblockAdapter extends RecyclerView.Adapter<UnblockAdapter.ViewHolder> {

    Context context;
    List<UserModel>userList;

    public UnblockAdapter(Context context, List<UserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UnblockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_user,parent,false);
        return new UnblockAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnblockAdapter.ViewHolder holder, int position) {

        String status = userList.get(position).getStatus();
        if (status.equals("Unblock")){
            holder.binding.unblockBtn.setVisibility(View.INVISIBLE);
        }else {
            holder.binding.blockBtn.setVisibility(View.INVISIBLE);
        }
        UserModel user = userList.get(position);
        holder.binding.txtName.setText(user.getName());
        holder.binding.txtUserEmail.setText(user.getEmail());
        holder.binding.txtStatus.setText(user.getStatus());
        holder.binding.txtStatus.setTextColor(getColor(position));

        Glide.with(context)
                .load(userList.get(position).getImageUrl())
                .error(R.drawable.error_image)
                .into(holder.binding.userProfile);



        holder.binding.blockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.POST, Constants.UPDATE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Status updated successfully", Toast.LENGTH_SHORT).show();
                        Log.e("REsposn",response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR",error.toString());
                        Toast.makeText(context, "ERRoR updated successfully"+error, Toast.LENGTH_SHORT).show();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        String newStatus = "Block";
                        Map<String, String> params = new HashMap<>();
                        params.put("email", user.getEmail()); // ID of user to be updated
                        params.put("status", newStatus); // New status to be inserted
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(request);

            }
        });

        holder.binding.unblockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.POST, Constants.UPDATE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Status updated successfully", Toast.LENGTH_SHORT).show();
                        Log.e("REsposn",response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR",error.toString());
                        Toast.makeText(context, "ERRoR updated successfully"+error, Toast.LENGTH_SHORT).show();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        String newStatus = "Unblock";
                        Map<String, String> params = new HashMap<>();
                        params.put("email", user.getEmail()); // ID of user to be updated
                        params.put("status",newStatus); // New status to be inserted
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(request);
            }
        });
    }

    private int getColor(int position) {
        String status= userList.get(position).status;
        if (status.equals("Unblock"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.present)));
        else if (status.equals("Block"))
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.absent)));

        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.white)));

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemUserBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding  =ItemUserBinding.bind(itemView);
        }
    }
}
