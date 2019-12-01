//package com.jipjung.hucomin.sinderella.MyMenuActivities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import android.support.v7.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.example.kante.live_alone.Adapters.MessageAdapter;
//import com.example.kante.live_alone.Classes.Message;
//import com.example.kante.live_alone.MessageActivities.DetailedMessage;
//import com.example.kante.live_alone.R;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//public class MyMessages extends AppCompatActivity {
//    private FirebaseFirestore firebaseFirestore;
//    private FirebaseAuth firebaseAuth;
//
//    private List<Message> messages;
//    private ListView messageListView;
//    private MessageAdapter adapter;
//    private TextView noMessage;
//    private List<Message> clickedMessages;
//    private List<Message> newlist;
//    private CardView cardView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_messages);
//
//        firebaseFirestore = FirebaseFirestore.getInstance();
//        firebaseAuth = FirebaseAuth.getInstance();
//        messageListView = findViewById(R.id.message_listview);
//        noMessage = findViewById(R.id.no_item_message);
//        cardView = findViewById(R.id.message_item);
//
//        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getMessages();
//                swipeContainer.setRefreshing(false);
//            }
//        });
//
//    }
//
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        getMessages();
//        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Message message = newlist.get(position);
//                Intent intent = new Intent(MyMessages.this, DetailedMessage.class);
//                intent.putExtra("messageObject",message);
//                startActivity(intent);
////                firebaseFirestore.collection("messages").whereEqualTo("receiver_id", firebaseAuth.getUid())
////                        .whereEqualTo("sender_id",sender_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
////                    @Override
////                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
////
////                        clickedMessages.addAll(queryDocumentSnapshots.toObjects(Message.class));
////                    }
////                });
////                firebaseFirestore.collection("messages").whereEqualTo("receiver_id", sender_id)
////                        .whereEqualTo("sender_id",firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
////                    @Override
////                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
////
////                        clickedMessages.addAll(queryDocumentSnapshots.toObjects(Message.class));
////                        Log.d("zxczxc",String.valueOf(clickedMessages.size()));
////                        clickedMessages.sort(new MessageComparator().reversed());
//////                Intent intent = new Intent(MyMessages.this, DetailedMessage.class);
//////                startActivity(intent);
////                        adapter = new MessageAdapter(MyMessages.this, clickedMessages);
////                        messageListView.setAdapter(adapter);
////                        setListViewHeightBasedOnChildren(messageListView);
////                    }
////                });
//
//            }
//        });
//
//    }
//
//    private List<Message> sendmessages;
//    public void getMessages(){
////        if (!comments.isEmpty())
//////            comments.clear();
//        firebaseFirestore.collection("messages").whereEqualTo("sender_id",firebaseAuth.getUid()).get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        sendmessages = queryDocumentSnapshots.toObjects(Message.class);
//                        sendmessages.sort(new MessageComparator().reversed());
//                        Log.d("qweqwe1",String.valueOf(sendmessages.size()));
//
//                        firebaseFirestore.collection("messages").whereEqualTo("receiver_id",firebaseAuth.getUid()).get()
//                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                        if(queryDocumentSnapshots.isEmpty()){
//                                            return;
//                                        } else{
//                                            messages = queryDocumentSnapshots.toObjects(Message.class);
//                                            messages.addAll(sendmessages);
//                                            Log.d("qweqwe2",String.valueOf(messages.size()));
//                                            messages.sort(new MessageComparator().reversed());
//                                            newlist = new ArrayList<>();
//                                            newlist.add(messages.get(0));
//                                            for(int i=1; i<messages.size(); i++){
//                                                boolean isSame = false;
//                                                for(int j =0; j<newlist.size(); j++){
//                                                    if(messages.get(i).getSender_id().equals(newlist.get(j).getSender_id()) ||
//                                                            messages.get(i).getSender_id().equals(newlist.get(j).getReceiver_id())){
//                                                        isSame = true;
//                                                    }
//                                                }
//                                                if(!isSame){
//                                                    newlist.add(messages.get(i));
//                                                }
//                                            }
//                                            adapter = new MessageAdapter(MyMessages.this, newlist);
//                                            messageListView.setAdapter(adapter);
//                                            setListViewHeightBasedOnChildren(messageListView);
//                                        }
//                                    }
//                                });
//                    }
//                });
//
//
//
//    }
//    public void setListViewHeightBasedOnChildren(ListView listView) {
//        MessageAdapter listAdapter = (MessageAdapter) listView.getAdapter();
//        if (listAdapter == null) {
//            messageListView.setVisibility(View.GONE);
//            noMessage.setVisibility(View.VISIBLE);
//            // pre-condition
//            return;
//        }
//
//        int totalHeight = 0;
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
//
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//        listView.requestLayout();
//    }
//
//    public class MessageComparator implements Comparator<Message>{
//        @Override
//        public int compare(Message o1, Message o2) {
//            return o1.getCreated_at().compareTo(o2.getCreated_at());
//        }
//    }
//
//}
