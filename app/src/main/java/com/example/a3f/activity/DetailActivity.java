package com.example.a3f.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.example.a3f.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailActivity extends Activity {
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final String pic=getIntent().getStringExtra("pic");
        String title=getIntent().getStringExtra("title");
        String content=getIntent().getStringExtra("content");

        final ImageView pic_2=findViewById(R.id.pic_2);
        TextView title_2=findViewById(R.id.title_2);
        TextView content_2=findViewById(R.id.content_2);
        content_2.setMovementMethod(ScrollingMovementMethod.getInstance());

        title_2.setText(title);
        content_2.setText(content);

        new AsyncTask<String, Void, Bitmap>()
        {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Bitmap doInBackground(String... params)
            {
                String url = pic;
                return getBitmapFromURL(url);
            }
            @Override
            protected void onPostExecute(Bitmap result)
            {
                pic_2.setImageBitmap(result);
                super.onPostExecute(result);
            }
        }.execute("圖片連結網址路徑");
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

