package com.jipjung.hucomin.sinderella.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jipjung.hucomin.sinderella.Adapters.Filterarrayadapter;
import com.jipjung.hucomin.sinderella.Adapters.RecyclerAdapter;
import com.jipjung.hucomin.sinderella.Classes.Post;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.HomeActivities.HomeFeed;
import com.jipjung.hucomin.sinderella.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class FSearchResult extends Fragment {

    private FirebaseFirestore fs;
    private TextView applytext;
    private User user;
    private RelativeLayout filterscreen;
    private ImageView filterbtn;
    private ListView filterListView;
    private CheckBox small_foot_checkbox;
    private CheckBox normal_foot_checkbox;
    private CheckBox bigger_foot_checkbox;
    private ProgressBar pgsBar;
    private String search_keyword;
    private RecyclerView recyclerView;
    int currentItems, totalItems, scrollOutItems;

    boolean isScrolling = false;
    private RecyclerAdapter mAdapter;
    private ArrayList<Post> mArrayList;
    private List<Post> types;

    public FSearchResult() {

    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contaniner, 
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.search_fragments, contaniner, false);
        FirebaseFirestore.setLoggingEnabled(true);
        fs = FirebaseFirestore.getInstance();
        
        pgsBar =(ProgressBar) v.findViewById(R.id.progress_bar);

        mArrayList = new ArrayList<>();
        Bundle bundle = getArguments();
        user = (User)bundle.getSerializable("user");
        search_keyword = (String)bundle.getString("search_keyword");

        recyclerView = (RecyclerView) v.findViewById(R.id.feeds);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerAdapter(getContext(), mArrayList, R.layout.search_fragments, user);
        getListItems();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(-1) || !recyclerView.canScrollVertically(1)){
                    isScrolling = true;
                }else if(recyclerView.computeVerticalScrollOffset() == 0){
                    isScrolling = true;
                }

//                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                    isScrolling = true;
//                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems >= totalItems)){
                    isScrolling = false;
                    fetchData();
                }
            }
        });
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)v.findViewById(R.id.swipe_layout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListItems();
                swipeContainer.setRefreshing(false);
            }
        });

//        ((HomeFeed)HomeFeed.context).searchingText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String searchWord = ((HomeFeed)HomeFeed.context).searchingText.getText().toString()
//                        .toLowerCase(Locale.getDefault());
//                mAdapter.filter(searchWord);
//            }
//        });

        //Filter 기능 들
        applytext = v.findViewById(R.id.apply);
        filterscreen = v.findViewById(R.id.filter_screen);


        filterbtn = v.findViewById(R.id.btn_filter);
        filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (applytext.getVisibility() == View.GONE) {
                    applytext.setVisibility(View.VISIBLE);
                } else {
                    applytext.setVisibility(View.GONE);
                }

                if (filterscreen.getVisibility() == View.GONE) {
                    filterscreen.setVisibility(View.VISIBLE);
                } else {
                    filterscreen.setVisibility(View.GONE);
                }

            }
        });
        //ListView
        filterListView = v.findViewById(R.id.list_filter);


        //Spinner text 사이즈 줄이기
        //TODO: Spinner adapter 만들기

        Spinner foot_size_spinner = v.findViewById(R.id.start_foot_size);

        //array.foot_size
        String[] foot_sizes = getResources().getStringArray(R.array.foot_size);

        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(
                getContext(), R.layout.foot_size_spinner_items, foot_sizes
        );

        SpinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);


        foot_size_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                foot_size_spinner.getSelectedItem().toString();
                Log.v("foot_size_spinner", foot_size_spinner.getSelectedItem().toString());

//
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner foot_size_spinner2 = v.findViewById(R.id.end_foot_size);


        foot_size_spinner2.setAdapter(SpinnerAdapter);

        foot_size_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("foot_size_spinner2", foot_size_spinner2.getSelectedItem().toString() + "is selected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        foot_size_spinner.setAdapter(SpinnerAdapter);
//         Spinner

        //ToDo:checkbox 선택될 시 값 전달

        small_foot_checkbox = v.findViewById(R.id.small_foot);
        normal_foot_checkbox = v.findViewById(R.id.normal_foot);
        bigger_foot_checkbox = v.findViewById(R.id.bigger_foot);


//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                getActivity(), android.R.layout.i
//        );


        applytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterscreen.setVisibility(View.GONE);
                applytext.setVisibility(View.GONE);

                ArrayList<String> filter_arrayList = new ArrayList<String>();

                Filterarrayadapter filterarrayadapter = new Filterarrayadapter(getActivity(), filter_arrayList);

                if (small_foot_checkbox.isChecked()) {
                    filter_arrayList.add(small_foot_checkbox.getText().toString());
//                    Toast.makeText(getActivity(),filter_arrayList.get(0),Toast.LENGTH_SHORT).show();
                    Log.d("foot_size", "small_foot");
                }
                if (normal_foot_checkbox.isChecked()) {
                    filter_arrayList.add(normal_foot_checkbox.getText().toString());
//                    Toast.makeText(getActivity(),filter_arrayList.get(1),Toast.LENGTH_SHORT).show();
                    Log.d("foot_size", "normal_foot");
                }
                if (bigger_foot_checkbox.isChecked()) {
                    filter_arrayList.add(bigger_foot_checkbox.getText().toString());
//                    Toast.makeText(getActivity(),filter_arrayList.get(2),Toast.LENGTH_SHORT).show();
                    Log.d("foot_size", "bigger_foot");
                }
                //TODO: 값은 나오는데 전달은 어디로 하는지?
                //체크된 값을 어디로 넘겨야된다.


//                filterListView.setAdapter(filterarrayadapter);


                filterarrayadapter.notifyDataSetChanged();
            }
        });



        return v;
    }

    @Override
    public void onPause(){
        super.onPause();
        if(pgsBar != null)
            pgsBar.setVisibility(ProgressBar.GONE);
    }
    private void fetchData() {
        pgsBar.setVisibility(ProgressBar.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int k = mArrayList.size();
                for (int i =0 ; i < 5; i++){
                    if(k + i < types.size()){
                        mArrayList.add(types.get(k + i));
                        mAdapter.notifyDataSetChanged();
                        pgsBar.setVisibility(ProgressBar.GONE);
                    }
                    else{
                        pgsBar.setVisibility(ProgressBar.GONE);
                        return;
                    }
                }
            }
        }, 1000);
    }
    public void getListItems() {
        pgsBar.setVisibility(ProgressBar.VISIBLE);
        if (!mArrayList.isEmpty()){
            mArrayList.clear();
            mAdapter.arrayList.clear();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fs.collection("posts").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    return;
                                } else {
                                    types = queryDocumentSnapshots.toObjects(Post.class);
//                                    for(int x = 0; x<types.size(); x++){
//                                        String post_id = queryDocumentSnapshots.getDocuments().get(x).getId();
//                                        types.get(x).withId(post_id);
//                                    }
//                                    types.sort(new FSearchResult.CustomComparator().reversed());
//                                    if (types.size() < 10) {
////                                        for (int i = 0; i < types.size(); i++) {
////                                            mArrayList.add(types.get(i));
////                                        }
                                        mArrayList.addAll(types);
//                                    } else {
//                                        for (int j = 0; j < 10; j++)
//                                            mArrayList.add(types.get(j));
//                                    }

                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.arrayList.addAll(mArrayList);
//                                    mAdapter.filter(search_keyword);
                                    pgsBar.setVisibility(ProgressBar.GONE);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pgsBar.setVisibility(ProgressBar.GONE);
                            }
                        });
            }
        }, 500);
    }

    public class sortCreatedAt implements Comparator<Post> {
        @Override
        public int compare(Post o1, Post o2) {
            return o1.getCreated_at().compareTo(o2.getCreated_at());
        }
    }
    public class sortRating implements Comparator<Post> {
        @Override
        public int compare(Post o1, Post o2) {
            return Float.compare(o1.getRating(),o2.getRating());
//            return o1.getCreated_at().compareTo(o2.getCreated_at());
        }
    }

}
