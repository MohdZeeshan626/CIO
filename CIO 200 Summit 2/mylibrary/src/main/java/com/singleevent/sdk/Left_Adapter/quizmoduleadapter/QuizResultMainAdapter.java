package com.singleevent.sdk.Left_Adapter.quizmoduleadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.singleevent.sdk.R;
import com.singleevent.sdk.View.LeftActivity.QuizModule.QuizResultActivity;
import com.singleevent.sdk.pojo.pollingpojo.PollingResultPojo;
import com.singleevent.sdk.pojo.quizmodulepojo.QuizResultMainPojo;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import com.github.nkzawa.emitter.Emitter;
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;

//public class QuizResultMainAdapter extends RecyclerView.Adapter<QuizResultMainAdapter.myViewHolder> {
//    QuizResultAdapter pollingResultAdapter;
//    Context context;
//    List<QuizResultMainPojo> pollingResultMainPojos;
//    String url = "https://health1.webmobi.in/socket/con";
   // private Socket mSocket;

//    {
//       // try {
//           // mSocket = IO.socket(url);
//
//       // } catch (URISyntaxException e) {
//        //}
//    }

//    public QuizResultMainAdapter(Context context, List<QuizResultMainPojo> pollingResultMainPojos) {
//        this.context = context;
//        this.pollingResultMainPojos = pollingResultMainPojos;
//    }

//    @NonNull
//    @Override
//    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.polling_result_main_row_items, parent, false);
//        return new myViewHolder(view);
//    }

//    @Override
//    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
//        holder.setIsRecyclable(false);
    //    try {
      //      if (mSocket.connected()) {
        //        Log.d("check_connection", "onCreatehhh: " + mSocket.connected() + "asdf");
          ///  }
       //     mSocket.on("Quiz_2021", new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    ((QuizResultActivity)context).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            JSONObject jsonObject = (JSONObject) args[0];
//                            try {
//                                JSONObject object = jsonObject.getJSONObject("updatedData");
//
//                                String question_id = object.getString("question_id");
//                                if (question_id.equals(pollingResultMainPojos.get(position).getQuestion_id())) {
//                                    int total_votes = object.getInt("total_votes");
//                                    String votes = object.getString("votes");
//                                    List<PollingResultPojo> resultPojos = new ArrayList<>();
//                                    Log.d("chcel", "run: \n" + "object String" + object.toString());
//
//                                    try {
//                                        JSONObject obj = new JSONObject(votes);
//                                        Log.d("gggg", "onBindViewHolder: " + obj);
//                                        Iterator<String> keysItr = obj.keys();
//                                        while (keysItr.hasNext()) {
//                                            String key = keysItr.next();
//                                            Object value = obj.get(key);
//                                            int count = (Integer) value;
//                                            Log.d("ggg_key", "onBindViewHolder: " + key + "\n" + count);
//                                            resultPojos.add(new PollingResultPojo(key, count, total_votes));
//
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    pollingResultMainPojos.get(position).setNumberOfParticipants(total_votes);
//                                    pollingResultMainPojos.get(position).setResultPojos(resultPojos);
//                                    notifyDataSetChanged();
//                                    pollingResultAdapter.notifyDataSetChanged();
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//
//                }
//            });

  //      } catch (Exception e) {
    //    }
//        holder.total_answer_count.setText("Total Number of Answers : " + pollingResultMainPojos.get(position).getNumberOfParticipants());
//        holder.question.setText(pollingResultMainPojos.get(position).getQuestionName());
//        pollingResultAdapter = new QuizResultAdapter(context, pollingResultMainPojos.get(position).getResultPojos());
//        holder.poll_result_recycler.setAdapter(pollingResultAdapter);
//    }

//    @Override
//    public int getItemCount() {
//        return pollingResultMainPojos.size();
//    }

//    public class myViewHolder extends RecyclerView.ViewHolder {
//        RecyclerView poll_result_recycler;
//        TextView question, total_answer_count;
//
//        public myViewHolder(@NonNull View itemView) {
//            super(itemView);
//            poll_result_recycler = itemView.findViewById(R.id.poll_result_recycler);
//            question = itemView.findViewById(R.id.question);
//            total_answer_count = itemView.findViewById(R.id.total_answer_count);
//            poll_result_recycler.setLayoutManager(new LinearLayoutManager(context));
//        }
//
//    }
//}
