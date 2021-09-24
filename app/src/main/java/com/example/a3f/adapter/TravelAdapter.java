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

public class TravelAdapter extends BaseAdapter {


    List<Map<String, Object>> list;
    LayoutInflater inflater; //布局反射器

    ClipboardManager clipboard = null;//複製貼上
    ClipData clipData = null;//複製貼上


    private Connection connection = null;
    private CollectAdapter adapter;
    private Context context;
    private Boolean flagCollect;

    public TravelAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }


    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.travel_item, null);
        }


        final ImageView pic = convertView.findViewById(R.id.pic);
        TextView title = convertView.findViewById(R.id.title);
        TextView region = convertView.findViewById(R.id.region);
        TextView town = convertView.findViewById(R.id.town);

        final TextView content = convertView.findViewById(R.id.content);
        final ImageButton addcollect = convertView.findViewById(R.id.add_collect);
        Button add_plan = convertView.findViewById(R.id.add_plan);


        final Map map = list.get(position);
        //pic.setImageResource((Integer)map.get("pic"));
        title.setText((String) map.get("title"));
        region.setText((String) map.get("region"));
        content.setText((String) map.get("content"));
        town.setText((String) map.get("town"));
        final String img = ((String) map.get("pic"));

//        flagCollect= (boolean) map.get("flagCollect");
//        if(flagCollect)
//            addcollect.setImageResource(R.mipmap.collect_selected);
//        else
//            addcollect.setImageResource(R.mipmap.collect_unselect);

        new AsyncTask<String, Void, Bitmap>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Bitmap doInBackground(String... params) {
                String url = img;
                return getBitmapFromURL(url);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                pic.setImageBitmap(result);
                super.onPostExecute(result);
            }
        }.execute("圖片連結網址路徑");

        final String cid = ((String) map.get("Id"));
        final String ctitle = ((String) map.get("title"));
        final String ccontent = ((String) map.get("content"));
        final String cregion = ((String) map.get("region"));
        final String ctown = ((String) map.get("town"));
        final String cpic = ((String) map.get("pic"));

        addcollect.setTag(map.get("title"));

//        convertView.findViewById(R.id.add_collect).setOnClickListener(new ItemButton_Click (mainFragment,position,"collect"));
//        addcollect.setOnClickListener(new ItemButton_Click(context, position,"collect"));
//        addplan.setOnClickListener(new ItemButton_Click(context, position,"plan"));
        addcollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    Thread thread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                insert(cid, ctitle, ccontent, cregion, ctown, cpic, userid);
//                                adapter.notifyDataSetChanged();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    thread.start();
                    try {
                        if (insert(cid, ctitle, ccontent, cregion, ctown, cpic, userid)) {
                            Toast.makeText(context, "收藏成功 "+v.getTag().toString(), Toast.LENGTH_SHORT).show();
                            addcollect.setImageResource(R.mipmap.collect_selected);
                            adapter.notifyDataSetChanged();

                        } else
                        {
                            Toast.makeText(context, "取消收藏 "+v.getTag().toString(), Toast.LENGTH_SHORT).show();
                            addcollect.setImageResource(R.mipmap.collect_unselect);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        add_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipData = ClipData.newPlainText("複製", (String) map.get("title"));
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(context, "複製成功", Toast.LENGTH_SHORT).show();
            }
        });

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

//    class ItemButton_Click implements Button.OnClickListener{
//        private  int position;
//        private  String whichButton;
//        private Context context;
//
//        ItemButton_Click(Context context,int pos,String which){
//            this.context=context;
//            position=pos;
//            whichButton=which;
//        }
//
//        @Override
//        public void onClick(View v) {
//            try {
//                if (whichButton.equals("collect")){
//                    Intent intent = new Intent();
//                    intent.setClass(context,CollectFragment.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("title",list.get(position).get("title").toString());
//                    bundle.putString("region",list.get(position).get("region").toString());
//                    bundle.putString("content",list.get(position).get("content").toString());
//                    intent.putExtras(bundle);
//
//                    Toast.makeText(context,"收藏成功", Toast.LENGTH_SHORT).show();
//                }
//                else if(whichButton.equals("plan")){
//                    Toast.makeText(context,"已加入行程", Toast.LENGTH_SHORT).show();
//                }
//            }catch (Exception ex){
//                Log.d("listView","ItemButton="+position+ex.toString());
//            }
//        }
//    }
}
