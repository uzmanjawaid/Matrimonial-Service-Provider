package com.example.matrimonialserviceprovider.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonialserviceprovider.Models.FeedbackModel;
import com.example.matrimonialserviceprovider.R;
import com.example.matrimonialserviceprovider.databinding.ItemFeedbackBinding;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    Context context;
    List<FeedbackModel> feedbackList;

    public FeedbackAdapter(Context context, List<FeedbackModel> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_feedback,parent,false);
        return new FeedbackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.ViewHolder holder, int position) {

        FeedbackModel feedback = feedbackList.get(position);
        holder.binding.txtName.setText(feedback.getUsername());
        holder.binding.feedbackText.setText(feedback.getMessage());

    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemFeedbackBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemFeedbackBinding.bind(itemView);
        }
    }
}
