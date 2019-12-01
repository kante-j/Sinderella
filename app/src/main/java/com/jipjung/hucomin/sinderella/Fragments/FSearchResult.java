package com.jipjung.hucomin.sinderella.Fragments;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
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

import com.google.firebase.firestore.FirebaseFirestore;
import com.jipjung.hucomin.sinderella.Adapters.Filterarrayadapter;
import com.jipjung.hucomin.sinderella.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FSearchResult extends Fragment {

    private FirebaseFirestore fs;
    private TextView applytext;
    private RelativeLayout filterscreen;
    private ImageView filterbtn;
    private ListView filterListView;
    private CheckBox small_foot_checkbox;
    private CheckBox normal_foot_checkbox;
    private CheckBox bigger_foot_checkbox;
    private ProgressBar pgsBar;

    public FSearchResult() {

    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contaniner, 
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.search_fragments, contaniner, false);
        FirebaseFirestore.setLoggingEnabled(true);
        fs = FirebaseFirestore.getInstance();
        
        pgsBar =(ProgressBar) v.findViewById(R.id.mypage_home_fragments_progress_bar);


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
}
