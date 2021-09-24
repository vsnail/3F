package com.example.a3f.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.a3f.R;
import com.example.a3f.adapter.CollectAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.a3f.activity.LoginActivity.userid;

public class CollectFragment extends Fragment implements AdapterView.OnItemClickListener {

    private Connection connection = null;
    private ImageButton refresh;

    public CollectFragment() {
    }

    public static CollectFragment newInstance() {
        CollectFragment fragment = new CollectFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_collect, container, false);
        final ListView mycollect_lv = v.findViewById(R.id.mycollect_lv);
        refresh = v.findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Statement statement = null;

                ArrayList<Map<String, Object>> clist = null;

                try {
                    Class.forName("net.sourceforge.jtds.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:jtds:sqlserver://140.137.61.130:1433/test", "sa", "student@109");
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("Select top 10 Id,Name,Description,Region,Town,Picture1 from myCollect2  where userId ='" + userid + "';");
                    clist = new ArrayList<Map<String, Object>>();
                    while (resultSet.next()) {

                        Map<String, Object> cmap = new HashMap<String, Object>();
                        cmap.put("Id", resultSet.getString(1));
                        cmap.put("title", resultSet.getString(2));
                        cmap.put("content", resultSet.getString(3));
                        cmap.put("region", resultSet.getString(4));
                        cmap.put("town", resultSet.getString(5));
                        cmap.put("pic", resultSet.getString(6));
                        clist.add(cmap);
                    }
                    CollectAdapter adapter = new CollectAdapter(clist, getActivity());
                    adapter.refresh(clist);
                    adapter.setList(clist);
                    //mycollectSadpter = new SimpleAdapter(getActivity(), clist, R.layout.mycollect_item, new String[]{"title", "content", "region"}, new int[]{R.id.mycollect_title, R.id.mycollect_content, R.id.mycollect_region});
                    mycollect_lv.setAdapter(adapter);
                    //mycollect_lv.setOnItemClickListener(this);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });


        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
