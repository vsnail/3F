package com.example.a3f.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a3f.R;
import com.example.a3f.adapter.HomeAdapter;
import com.example.a3f.travel.TravelFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private ArrayList<Fragment> mFragments=new ArrayList<>();
    private final String[] mTitles = {
            "推薦景點", "北部景點", "中部景點"
            , "南部景點", "東部景點"
    };
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
//    private EditText et_search;
//    public static String search;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home, container, false);
        viewPager= v.findViewById(R.id.FHomeViewPager);
        slidingTabLayout=v.findViewById(R.id.slidingTabLayout);
//        et_search=v.findViewById(R.id.et_search);
//        search=et_search.getText().toString().trim();
//        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            private Object TravelFragment;
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
//                    //search=et_search.getText().toString().trim();
//                    Toast.makeText(getActivity(),"輸入完成"+search, Toast.LENGTH_LONG).show();
//                    return true;
//                }
//                return false;
//            }
//        });

        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        for (String title : mTitles) {
            mFragments.add(TravelFragment.newInstance(title));
        }
//        mFragments = new ArrayList<>();
//        mFragments.add(new TravelFragment());
//        mFragments.add(new North_TravelFragment());
//        mFragments.add(new Central_TravelFragment());
//        mFragments.add(new South_TravelFragment());
//        mFragments.add(new East_TravelFragment());

        viewPager.setOffscreenPageLimit(mFragments.size());
        viewPager.setAdapter(new HomeAdapter(getFragmentManager(),mTitles,mFragments));
        slidingTabLayout.setViewPager(viewPager);
    }
}
