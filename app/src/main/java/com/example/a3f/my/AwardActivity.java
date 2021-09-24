package com.example.a3f.my;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a3f.R;
import com.example.a3f.activity.HomeActivity;
import com.example.a3f.activity.LoginActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AwardActivity extends AppCompatActivity {

    private Button award_btn;
    private TextView myaward_title;
    private TextView myaward_region;
    private TextView myaward_town;
    private TextView myaward_content;
    private ImageView myaward_pic;
    private Connection connection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        award_btn=findViewById(R.id.award_btn);
        myaward_title=findViewById(R.id. myaward_title);
        myaward_region=findViewById(R.id.myaward_region);
        myaward_town=findViewById(R.id.myaward_town);
        myaward_content=findViewById(R.id. myaward_content);
        myaward_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        myaward_pic=findViewById(R.id.myaward_pic);

        award_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {

                Statement statement = null;
                try {
                    Class.forName("net.sourceforge.jtds.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:jtds:sqlserver://140.137.61.130:1433/TravelJsonDB","sa", "student@109");
                    statement = connection.createStatement();
                    final ResultSet resultSet = statement.executeQuery("select Top 1 *, NewID() as random from tbTrip  where Picture1 IS NOT NULL AND Picture1 !='' order by random;");
                    while(resultSet.next()){
                        myaward_title.setText(resultSet.getString("Name"));
                        myaward_region.setText(resultSet.getString("region"));
                        myaward_town.setText(resultSet.getString("town"));
                        myaward_content.setText(resultSet.getString("Description"));
                        final String img=resultSet.getString("Picture1");

                        new AsyncTask<String, Void, Bitmap>()
                        {
                            @Override
                            protected Bitmap doInBackground(String... params)
                            {
                                String url = img;
                                return getBitmapFromURL(url);
                            }
                            @Override
                            protected void onPostExecute(Bitmap result)
                            {
                                myaward_pic.setImageBitmap(result);
                                super.onPostExecute(result);
                            }
                        }.execute("圖片連結網址路徑");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
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
