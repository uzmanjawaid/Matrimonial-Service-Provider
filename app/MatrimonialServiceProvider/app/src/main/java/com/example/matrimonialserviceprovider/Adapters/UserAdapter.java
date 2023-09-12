package com.example.matrimonialserviceprovider.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.matrimonialserviceprovider.Models.UserModel;
import com.example.matrimonialserviceprovider.ProfileActivity;
import com.example.matrimonialserviceprovider.R;
import com.example.matrimonialserviceprovider.databinding.ItemServiceBinding;
import com.example.matrimonialserviceprovider.databinding.ItemUserBinding;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>implements Filterable {

    Context context;
    List<UserModel> userList;
    List<UserModel> searchList;

    public UserAdapter(Context context, List<UserModel> userList) {
        this.context = context;
        this.userList = userList;
        this.searchList=new ArrayList<>(userList);
    }

    public void setData(List<UserModel> data) {
        searchList = data;
        searchList = new ArrayList<>(searchList);
    }


    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_service,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {

        UserModel user = userList.get(position);
        holder.binding.txtTitle.setText(user.getName());
        holder.binding.txtDes.setText(user.getEmail());
        holder.binding.txtAge.setText(user.getGender());
        holder.binding.txtCaste.setText(user.getCaste());
        holder.binding.matrial.setText(user.getMarital());
        holder.binding.txtReligion.setText(user.getReligion());


        Glide.with(context)
                .load(userList.get(position).getImageUrl())
                .error(R.drawable.error_image)
                .into(holder.binding.proImg);

        holder.binding.card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("name",userList.get(position).getName());
                intent.putExtra("image",userList.get(position).getImageUrl());
                intent.putExtra("email",userList.get(position).getEmail());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<UserModel> filteredData = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredData.addAll(searchList);
            }else {
                String key = constraint.toString().toLowerCase().trim();
                for (UserModel obj :searchList){
                    if (obj.gender.toLowerCase().contains(key) || obj.name.toLowerCase().contains(key)||
                    obj.email.toLowerCase().contains(key)||obj.religion.toLowerCase().contains(key)||obj.caste.toLowerCase().contains(key)||
                    obj.marital.toLowerCase().contains(key));
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredData;
            results.count = filteredData.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userList.clear();
            userList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemServiceBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemServiceBinding.bind(itemView);
        }
    }
}
