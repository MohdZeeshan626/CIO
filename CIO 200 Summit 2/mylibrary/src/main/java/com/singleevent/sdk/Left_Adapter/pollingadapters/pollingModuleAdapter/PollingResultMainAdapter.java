package com.singleevent.sdk.Left_Adapter.pollingadapters.pollingModuleAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.singleevent.sdk.Left_Adapter.pollingadapters.PollingResultAdapter;
import com.singleevent.sdk.R;
import com.singleevent.sdk.pojo.pollingpojo.PollingResultMainPojo;

import java.util.List;

public class PollingResultMainAdapter extends RecyclerView.Adapter<PollingResultMainAdapter.myViewHolder> {
    PollingResultAdapter pollingResultAdapter;
    Context context;
    List<PollingResultMainPojo> pollingResultMainPojos;

    public PollingResultMainAdapter(Context context, List<PollingResultMainPojo> pollingResultMainPojos) {
        this.context = context;
        this.pollingResultMainPojos = pollingResultMainPojos;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.polling_result_main_row_items, parent, false);
        return new PollingResultMainAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.total_answer_count.setText("Total Number of Answers : " + pollingResultMainPojos.get(position).getNumberOfParticipants());
        holder.question.setText(pollingResultMainPojos.get(position).getQuestionName());
        pollingResultAdapter = new PollingResultAdapter(context, pollingResultMainPojos.get(position).getResultPojos());
        holder.poll_result_recycler.setAdapter(pollingResultAdapter);
    }

    @Override
    public int getItemCount() {
        return pollingResultMainPojos.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        RecyclerView poll_result_recycler;
        TextView question, total_answer_count;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            poll_result_recycler = itemView.findViewById(R.id.poll_result_recycler);
            question = itemView.findViewById(R.id.question);
            total_answer_count = itemView.findViewById(R.id.total_answer_count);
            poll_result_recycler.setLayoutManager(new LinearLayoutManager(context));
        }
    }
}
