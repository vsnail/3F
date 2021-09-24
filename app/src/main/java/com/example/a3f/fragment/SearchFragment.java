package com.example.a3f.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.a3f.R;
import com.example.a3f.adapter.EventAdapter;
import com.example.a3f.adapter.SearchAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener{

    private Connection connection = null;
    private Button search_btn;
    private EditText et_search;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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
        View v= inflater.inflate(R.layout.fragment_search, container, false);
        final ListView search_lv=v.findViewById(R.id.search_lv);
        et_search=v.findViewById(R.id.et_search);
        search_btn=v.findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search=et_search.getText().toString().trim();

                Statement statement = null;
                List<Map<String, Object>> shlist = null;
                try {
                    Class.forName("net.sourceforge.jtds.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:jtds:sqlserver://140.137.61.130:1433/TravelJsonDB","sa", "student@109");
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("Select top 10 Id,Name,Description,region,town,Picture1 from tbTrip where Picture1 IS NOT NULL AND Picture1 !='' AND Name LIKE N'%"+search+"%';");
                    shlist = new ArrayList<Map<String, Object>>();
                    while (resultSet.next()) {
                        Map<String, Object> shmap = new HashMap<String, Object>();
                        shmap.put("shId",resultSet.getString(1));
                        shmap.put("shtitle", resultSet.getString(2));
                        shmap.put("shcontent", resultSet.getString(3));
                        shmap.put("shregion", resultSet.getString(4));
                        shmap.put("shtown", resultSet.getString(5));
                        shmap.put("shpic", resultSet.getString(6));
                        shlist.add(shmap);
                    }
                    SearchAdapter adapter=new SearchAdapter(getActivity());
                    adapter.setList(shlist);
                    search_lv.setAdapter(adapter);
                    //search_lv.setOnItemClickListener(this);

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
