package com.example.a3f.travel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.a3f.R;
import com.example.a3f.activity.DetailActivity;
import com.example.a3f.adapter.TravelAdapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TravelFragment extends Fragment implements AdapterView.OnItemClickListener {

    private String title;
    private static String ip = "140.137.61.130";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "TravelJsonDB";
    private static String username = "sa";
    private static String password = "student@109";
    private static String url = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + database;

    private ListView lv_travel;
    private ResultSet resultSet;
//    private boolean flagCollect;

    private Connection connection = null;

    public TravelFragment() {

    }

    public static TravelFragment newInstance(String title) {
        TravelFragment fragment = new TravelFragment();
        fragment.title = title;
        return fragment;
    }

//    public boolean isFlagCollect() {
//        return flagCollect;
//    }
//
//    public void setFlagCollect(boolean flagCollect) {
//        this.flagCollect = flagCollect;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_travel, container, false);
        lv_travel = v.findViewById(R.id.lv_travel);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Statement statement = null;


        List<Map<String, Object>> list = null;

        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
            switch (title) {
                case "推薦景點":
                    resultSet = statement.executeQuery("Select top 10 Id,Name,Description,region,town,Picture1 from tbTrip where Picture1 IS NOT NULL AND Picture1 !='';");
                    break;
                case "北部景點":
                    resultSet = statement.executeQuery("Select top 10 Id,Name,Description,region,town,Picture1 from tbTrip where Picture1 !='' AND Picture1 IS NOT NULL AND Description != '' AND region IN (N'臺北市',N'新北市',N'基隆市',N'新竹市',N'桃園市',N'新竹縣',N'宜蘭縣');");
                    break;
                case "中部景點":
                    resultSet = statement.executeQuery("Select top 10 Id,Name,Description,region,town,Picture1 from tbTrip where Picture1 !='' AND Description != '' AND region IN (N'臺中市',N'苗栗縣',N'彰化縣',N'南投縣',N'雲林縣');");
                    break;
                case "南部景點":
                    resultSet = statement.executeQuery("Select top 10 Id,Name,Description,region,town,Picture1 from tbTrip where Picture1 !='' AND Description != '' AND region IN (N'高雄市',N'臺南市',N'嘉義市',N'嘉義縣',N'屏東縣',N'澎湖縣');");
                    break;
                case "東部景點":
                    resultSet = statement.executeQuery("Select top 10 Id,Name,Description,region,town,Picture1 from tbTrip where Picture1 !='' AND Description != '' AND region IN (N'花蓮縣',N'臺東縣');");
                    break;
                default:
                    resultSet = statement.executeQuery("Select top 20 Id,Name,Description,region,town,Picture1 from tbTrip where Picture1 IS NOT NULL AND Picture1 !='';");
            }

            //resultSet = statement.executeQuery("Select top 10 Id,Name,Description,region,town,Picture1 from tbTrip where Picture1 IS NOT NULL AND Picture1 !='' AND Name LIKE N'%" + search + "%';");

//            resultSet = statement.executeQuery("Select top 10 Id,Name,Description,region,town,Picture1 from tbTrip where Picture1 IS NOT NULL AND Picture1 !='';");
            list = new ArrayList<Map<String, Object>>();
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("Id", resultSet.getString(1));
                map.put("title", resultSet.getString(2).toString());
                map.put("content", resultSet.getString(3).toString());
                map.put("region", resultSet.getString(4).toString());
                map.put("town", resultSet.getString(5).toString());
                map.put("pic", resultSet.getString(6));
//                map.put("boolean",flagCollect);
                //map.put("region", resultSet.getString().toString());
                //map.put("town", resultSet.getString().toString());
                list.add(map);

            }
            TravelAdapter adapter = new TravelAdapter(getActivity());
            adapter.setList(list);
//             adapter = new SimpleAdapter(getActivity().getApplicationContext(), list, R.layout.travel_item, new String[]{"title", "content"}, new int[]{R.id.title, R.id.content}); //適配器在這
            lv_travel.setAdapter(adapter);
            lv_travel.setOnItemClickListener(this);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return v;
    }

    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        Intent intent = new Intent();
        intent.setClass(getActivity(), DetailActivity.class);
        Map<String, Object> map
                = (HashMap<String, Object>) parent.getItemAtPosition(position);

        intent.putExtra("index", "" + position);
        intent.putExtra("title", "" + map.get("title"));
        intent.putExtra("content", "" + map.get("content"));
        intent.putExtra("pic", "" + map.get("pic"));
        startActivity(intent);
    }

//    private View.OnClickListener mycollectClickListener = new View.OnClickListener() {
//       @Override
//       public void onClick(View v) {
//           Toast.makeText(getActivity(),"測試收藏", Toast.LENGTH_LONG).show();
//       }
//    };
}

