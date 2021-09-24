package com.example.a3f.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.a3f.R;
import com.example.a3f.adapter.CollectAdapter;
import com.example.a3f.adapter.EventAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventFragment extends Fragment implements AdapterView.OnItemClickListener{

    private Connection connection = null;

    public EventFragment() {
    }

    public static EventFragment newInstance() {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_event, container, false);
        ListView event_lv=v.findViewById(R.id.event_lv);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Statement statement = null;

        List<Map<String, Object>> evlist = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://140.137.61.130:1433/TravelJsonDB","sa", "student@109");
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select top 10 Id,Name,Description,Org,Picture1 from tbActive;");
            evlist = new ArrayList<Map<String, Object>>();
            while (resultSet.next()) {
                Map<String, Object> evmap = new HashMap<String, Object>();

                evmap.put("evId", resultSet.getString(1));
                evmap.put("evtitle", resultSet.getString(2));
                evmap.put("evcontent", resultSet.getString(3));
                evmap.put("evorg", resultSet.getString(4));
                evmap.put("evpic",resultSet.getString(5));

                evlist.add(evmap);

            }

            EventAdapter adapter=new EventAdapter(getActivity());
            adapter.setList(evlist);
            event_lv.setAdapter(adapter);
            event_lv.setOnItemClickListener(this);

        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
