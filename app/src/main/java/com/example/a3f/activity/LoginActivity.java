package com.example.a3f.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    private EditText et_account;
    private EditText et_pwd;
    private Button btn_login;
    private ImageView showpass;
    private int count = 0;
    private int count10 = 0;
    private Connection connection = null;
    public static String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_account = findViewById(R.id.et_account);
        et_pwd = findViewById(R.id.et_pwd);
        showpass = findViewById(R.id.showpass);
        showpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count % 2 == 0)
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                else
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count10++;
                if (count10 == 10) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                final String account = et_account.getText().toString().trim(); //trim去掉空格
                final String pwd = et_pwd.getText().toString().trim();
                try {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Looper.prepare();
                                login(account, pwd);
                                Looper.loop();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();

                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "查詢不到帳號", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });


    }

    private void login(String account, String pwd) throws Exception {
        if (account == null || account.length() <= 0) {
            Toast.makeText(this, "請輸入帳號", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd == null || pwd.length() <= 0) {
            Toast.makeText(this, "請輸入密碼", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd != null || account.length() > 0) {
            try {
                Statement statement = null;
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:jtds:sqlserver://140.137.61.130:1433/景點", "sa", "student@109");
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT Id,UserPwd FROM dbo.會員資料 where Id='" + account + "' And UserPwd='" + pwd + "';");
                while (resultSet.next()) {
                    if (account.equals(resultSet.getString("Id")) && pwd.equals(resultSet.getString("UserPwd"))) {
                        userid = resultSet.getString("Id");
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "登入成功" + account, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "帳號或密碼錯誤!!" + account, Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (ClassNotFoundException e) {
                Toast.makeText(LoginActivity.this, "帳號或密碼錯誤!!" + account, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (SQLException e) {
                Toast.makeText(LoginActivity.this, "帳號或密碼錯誤!!" + account, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            return;
        }
    }
}
