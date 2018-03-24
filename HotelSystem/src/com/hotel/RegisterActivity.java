package com.hotel;



import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private EditText username;
	private EditText pwd;
	private Button register;
	private Button cancel;
	private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		MySqliteHelper mySqliteHelper = new MySqliteHelper(this, "hotelSys.db", null, 1);
		db = mySqliteHelper.getWritableDatabase();
		
		register = (Button) findViewById(R.id.registerBtn);
		register.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View arg0) {
				username = (EditText) findViewById(R.id.usernameEditText);
				pwd = (EditText) findViewById(R.id.pwdEditText);
				String name  = username.getText().toString();
				String password = pwd.getText().toString();
				Cursor query = db.query("user", new String[]{"username","pwd"},"username=?", new String[]{name}, null, null, null);
				if (!query.moveToFirst()) {
					try {
						db.execSQL("insert into user(username,pwd) values(?,?)",new String[]{name,password});
						Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}else{
					Toast.makeText(getApplicationContext(), "用户名已存在", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		cancel = (Button) findViewById(R.id.cancelBtn);
		cancel.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				username = (EditText) findViewById(R.id.usernameEditText);
				pwd = (EditText) findViewById(R.id.pwdEditText);
				username.setText("");
				pwd.setText("");
			}
			
		});
	}

	
}
