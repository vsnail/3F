package com.example.a3f.adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3f.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import static com.example.a3f.activity.LoginActivity.userid;

public class SearchAdapter extends BaseAdapter {

    List<Map<String, Object>> shlist;
    LayoutInflater inflater;

    ClipboardManager clipboard = null;//複製貼上
    ClipData clipData = null;//複製貼上


    private Connection connection = null;
    private CollectAdapter adapter;

    private Context context;

    public SearchAdapter (Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(List<Map<String, Object>> shlist) {
        this.shlist = shlist;
    }

    @Override
    public int getCount() {
        return shlist.size();
    }

    @Override
    public Object getItem(int position) {
        return shlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_item,null);
        }

        TextView shtitle = convertView.findViewById(R.id.sh_title);
        TextView shcontent = convertView.findViewById(R.id.sh_content);
        TextView shregion=convertView.findViewById(R.id.sh_region);
        TextView shtown=convertView.findViewById(R.id.sh_town);

        EditText et_search=convertView.findViewById(R.id.et_search);
        Button search_btn=convertView.findViewById(R.id.search_btn);

        final ImageView shpic=convertView.findViewById(R.id.sh_pic);
        final ImageButton shcollect = convertView.findViewById(R.id.sh_collect);
        Button sh_plan = convertView.findViewById(R.id.sh_plan);



        final Map shmap = shlist.get(position);
        shtitle.setText((String) shmap.get("shtitle"));
        shcontent.setText((String) shmap.get("shcontent"));
        shregion.setText((String) shmap.get("shregion"));
        shtown.setText((String) shmap.get("shtown"));

        final String shimg=((String) shmap.get("shpic"));
        final String sid = ((String) shmap.get("shId"));
        final String stitle = ((String) shmap.get("shtitle"));
        final String scontent = ((String) shmap.get("shcontent"));
        final String sregion = ((String) shmap.get("shregion"));
        final String stown = ((String) shmap.get("shtown"));

        //shcollect.setTag(shmap.get("title"));

        shcollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        if (insert(sid, stitle, scontent, sregion, stown, shimg, userid)) {
                            Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
                            shcollect.setImageResource(R.mipmap.collect_selected);
                            adapter.notifyDataSetChanged();

                        } else
                        {
                            Toast.makeText(context, "取消收藏", Toast.LENGTH_SHORT).show();
                            shcollect.setImageResource(R.mipmap.collect_unselect);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });

        sh_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipData = ClipData.newPlainText("複製", (String) shmap.get("shtitle"));
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(context, "複製成功", Toast.LENGTH_SHORT).show();
            }
        });

        new AsyncTask<String, Void, Bitmap>()
        {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Bitmap doInBackground(String... params)
            {
                String url = shimg;
                return getBitmapFromURL(url);
            }
            @Override
            protected void onPostExecute(Bitmap result)
            {
                shpic.setImageBitmap(result);
                super.onPostExecute(result);
            }
        }.execute("圖片連結網址路徑");



        return convertView;
    }
    public boolean insert(String a, String b, String c, String d, String e, String f, String g) throws Exception {
        Boolean bool = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://140.137.61.130:1433/test", "sa", "student@109");
            PreparedStatement statement = connection.prepareStatement("UPDATE myCollect2 set Id=N'" + a + "',Name=N'" + b + "',Description=N'" + c + "',Region=N'" + d + "',Town=N'" + e + "',Picture1=N'" + f + "'where userId ='" + userid + "' AND Id='" + a + "';");
            final int updateValue = statement.executeUpdate();
            if (updateValue == 1) {
                String sql = "delete from myCollect2 where userId='"+userid+"' AND Id='"+a+"'";
                PreparedStatement pstmt = null;
                //Class.forName("net.sourceforge.jtds.jdbc.Driver");
                //connection = DriverManager.getConnection("jdbc:jtds:sqlserver://140.137.61.130:1433/test", "sa", "student@109");
                pstmt = connection.prepareStatement(sql);
                pstmt.executeUpdate();
                pstmt.close();
                bool = false;
            } else {
                try {
                    String sql = "INSERT INTO dbo.myCollect2(Id,Name,Description,Region,Town,Picture1,userId) VALUES(?,?,?,?,?,?,?)";
                    PreparedStatement pstmt = null;
                    //Class.forName("net.sourceforge.jtds.jdbc.Driver");
                    //connection = DriverManager.getConnection("jdbc:jtds:sqlserver://140.137.61.130:1433/test", "sa", "student@109");
                    pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1, a);
                    pstmt.setString(2, b);
                    pstmt.setString(3, c);
                    pstmt.setString(4, d);
                    pstmt.setString(5, e);
                    pstmt.setString(6, f);
                    pstmt.setString(7, String.valueOf(g));

                    pstmt.executeUpdate();
                    pstmt.close();
                    bool = true;

                } catch (Exception e1) {
                    throw new Exception("操作中出現錯誤！！！");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            connection.close();
        }
        return bool;
    }
    private static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
