package com.example.a3f.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a3f.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class EventAdapter extends BaseAdapter {
    List<Map<String, Object>> evlist;
    LayoutInflater inflater;

    private Context context;

    public EventAdapter (Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(List<Map<String, Object>> evlist) {
        this.evlist = evlist;
    }

    @Override
    public int getCount() {
        return evlist.size();
    }

    @Override
    public Object getItem(int position) {
        return evlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.event_item,null);
        }

        TextView evtitle = convertView.findViewById(R.id.event_title);
        TextView evcontent = convertView.findViewById(R.id.event_content);
        TextView evorg=convertView.findViewById(R.id.event_org);
        final ImageView evpic=convertView.findViewById(R.id.event_pic);



        Map evmap = evlist.get(position);
        evtitle.setText((String) evmap.get("evtitle"));
        evcontent.setText((String) evmap.get("evcontent"));
        evorg.setText((String) evmap.get("evorg"));
        final String evimg=((String) evmap.get("evpic"));

        new AsyncTask<String, Void, Bitmap>()
        {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Bitmap doInBackground(String... params)
            {
                String url = evimg;
                return getBitmapFromURL(url);
            }
            @Override
            protected void onPostExecute(Bitmap result)
            {
                evpic.setImageBitmap(result);
                super.onPostExecute(result);
            }
        }.execute("圖片連結網址路徑");



        return convertView;
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
