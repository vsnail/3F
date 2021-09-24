package com.example.a3f.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.a3f.activity.LoginActivity.userid;

public class CollectAdapter extends BaseAdapter {

    private ArrayList<Map<String, Object>> clist;
    private Connection connection = null;
    private CollectAdapter adapter;

    LayoutInflater inflater;

    private Context context;

    public CollectAdapter(ArrayList<Map<String, Object>> list, Context context) {
        clist = list;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void refresh(ArrayList<Map<String, Object>> list) {
        clist = list;
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (clist != null) {
            clist.remove(position);
        }
        notifyDataSetChanged();
    }

    public void clear() {

        if (clist != null) {
            clist.clear();
        }
        notifyDataSetChanged();
    }


    public void setList(ArrayList<Map<String, Object>> list) {
        this.clist = list;
    }

    @Override
    public int getCount() {
        return clist.size();
    }

    @Override
    public Object getItem(int position) {
        return clist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mycollect_item, null);
        }

        TextView ctitle = convertView.findViewById(R.id.mycollect_title);
        TextView cregion = convertView.findViewById(R.id.mycollect_region);
        TextView ctown = convertView.findViewById(R.id.mycollect_town);
        TextView ccontent = convertView.findViewById(R.id.mycollect_content);
        ImageButton cdelete = convertView.findViewById(R.id.mycollect_delete);


        final ImageView cpic = convertView.findViewById(R.id.mycollect_pic);

        Map cmap = clist.get(position);
        ctitle.setText((String) cmap.get("title"));
        cregion.setText((String) cmap.get("region"));
        ccontent.setText((String) cmap.get("content"));
        ctown.setText((String) cmap.get("town"));

        final String cimg = ((String) cmap.get("pic"));
        final String cid = ((String) cmap.get("Id"));


        cdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    delete(cid,userid);
                    clist.remove(position);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Toast.makeText(context,"取消收藏"+v.getTag().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        new AsyncTask<String, Void, Bitmap>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Bitmap doInBackground(String... params) {
                String url = cimg;
                return getBitmapFromURL(url);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                cpic.setImageBitmap(result);
                super.onPostExecute(result);
            }
        }.execute("圖片連結網址路徑");

        return convertView;
    }

    private static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(String x, String y) throws Exception {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:jtds:sqlserver://140.137.61.130:1433/test", "sa", "student@109");
            String sql = "delete from myCollect2 where userId='" + y + "' AND Id='" + x + "'";
            PreparedStatement pstmt = null;
            pstmt = connection.prepareStatement(sql);
            pstmt.executeUpdate();
            pstmt.close();
            Toast.makeText(context, "取消收藏成功", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            throw new Exception("操作中出現錯誤！！！");
        }
    }
}
