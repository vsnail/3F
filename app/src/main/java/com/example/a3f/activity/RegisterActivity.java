package com.example.a3f.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a3f.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class RegisterActivity extends AppCompatActivity {

    private EditText add_userid;
    private EditText add_userpwd;
    private EditText add2_userpwd;
    private ImageView add_showpass;
    private ImageView add2_showpass;
    private int count=0;
    private int count2=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        add_userid=findViewById(R.id.add_userid);
        add_userpwd=findViewById(R.id.add_userpwd);
        add2_userpwd=findViewById(R.id.add2_userpwd);
        add_showpass=findViewById(R.id.add_showpass);
        add2_showpass=findViewById(R.id.add2_showpass);

        add_showpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count%2==0)
                    add_userpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                else
                    add_userpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        add2_showpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count2++;
                if(count2%2==0)
                    add2_userpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                else
                    add2_userpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });


        Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userid= add_userid.getText().toString().trim();
                final String userpwd=add_userpwd.getText().toString().trim();
                final String userpwd2=add2_userpwd.getText().toString().trim();
                register(userid,userpwd,userpwd2);
                try {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try  {
                                insert(userid,userpwd);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();

                } catch (Exception e) {
                    Toast.makeText(RegisterActivity.this,"註冊失敗",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void register(String id,String pwd, String pwd2) {
        if(id ==null||id.length()<=0){
            Toast.makeText(this,"請輸入用戶名",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pwd ==null||pwd.length()<=0){
            Toast.makeText(this,"請輸入密碼",Toast.LENGTH_SHORT).show();
            return;
        }
        if(pwd2==null||pwd2.length()<=0){
            Toast.makeText(this,"請輸入確認密碼",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!pwd.equals(pwd2)) {
            Toast.makeText(this, "兩次密碼不一樣", Toast.LENGTH_SHORT).show();
            return;
        }
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "註冊成功" + id + "請登入!", Toast.LENGTH_SHORT).show();
    }

    private void insert(String a, String b) throws Exception {
        String sql = "INSERT INTO dbo.會員資料(Id,UserPwd) VALUES(?,?)" ;
        PreparedStatement pstmt = null;
        Class.forName("net.sourceforge.jtds.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection("jdbc:jtds:sqlserver://140.137.61.130:1433/景點", "sa", "student@109")) {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, a);
            pstmt.setString(2, b);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e1) {
            throw new Exception("操作中出現錯誤！！！");
        }
    }

}
