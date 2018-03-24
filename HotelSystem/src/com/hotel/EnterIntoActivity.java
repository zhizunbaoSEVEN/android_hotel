package com.hotel;

import java.security.PublicKey;

import android.app.TabActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class EnterIntoActivity extends TabActivity {

	private SQLiteDatabase db;
	private EditText nameEditText;
	private RadioButton radioMan;
	private RadioButton radioWoman;
	private EditText idCardEditText;
	private EditText timeInEditText;
	private EditText timeSumEditText;
	private EditText roomNumEditText;
	private EditText moneyEditText;
	private Button btn01;
	private Button btn02;
	private Button btn03;
	private String result="";
	private String SearchResult="";
	private Button search;
	private TextView resultTextView;
	private TextView show;
	
	private EditText searchEditText;
	
	private Button dropBtn;
	private EditText usernameEditText;
	private EditText roomNum01EditText;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final MySqliteHelper mySqliteHelper = new MySqliteHelper(this, "hotelSys.db", null, 1);
		db=mySqliteHelper.getWritableDatabase();
		
		//set tab
		TabHost tabHost = getTabHost();
		LayoutInflater.from(this).inflate(R.layout.manage, tabHost.getTabContentView(), true);
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("入住",null).setContent(R.id.tab1));
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("查询",null).setContent(R.id.tab2));
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("退房",null).setContent(R.id.tab3));
		tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("入住情况",null).setContent(R.id.tab4));
		
		btn01 = (Button) findViewById(R.id.btn01);
		btn01.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) { 
				try {
					nameEditText = (EditText) findViewById(R.id.nameEditText); 
					radioMan = (RadioButton) findViewById(R.id.man);
					radioWoman = (RadioButton) findViewById(R.id.woman);
					idCardEditText = (EditText)findViewById(R.id.idCardEditText);
					timeInEditText = (EditText)findViewById(R.id.timeInEditText);
					timeSumEditText = (EditText)findViewById(R.id.timeSumEditText);
					roomNumEditText = (EditText)findViewById(R.id.roomNumEditText);
					moneyEditText = (EditText)findViewById(R.id.moneyEditText);
					String name = nameEditText.getText().toString();
					String sex = "";
					if(radioMan.isChecked()){
						sex="man";
					}else if(radioWoman.isChecked()){
						sex="woman";
					}
					String idCard = idCardEditText.getText().toString();
					String timeIn = timeInEditText.getText().toString();
					String timeSum = timeSumEditText.getText().toString();
					String roomNum = roomNumEditText.getText().toString();
					String money = moneyEditText.getText().toString();
					
					db=mySqliteHelper.getReadableDatabase();
					Cursor query = db.query("hotel", new String[]{"roomNum","name","sex","idCard","timeIn","timeSum","money"}, "roomNum=?", new String[]{roomNum}, null, null, "_id asc");
					if(!query.moveToFirst()){ 
						db.execSQL("insert into hotel(name,sex,idCard,timeIn,timeSum,roomNum,money) values(?,?,?,?,?,?,?)",new String[]{name,sex,idCard,timeIn,timeSum,roomNum,money});
						Toast.makeText(getApplicationContext(),"入住成功", Toast.LENGTH_SHORT).show();
						clean();
					}else{
						Toast.makeText(getApplicationContext(),"每一项不能为空", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				db = mySqliteHelper.getReadableDatabase();
				Cursor query = db.query("hotel", new String[]{"name"}, null, null, null, null, "_id asc");
				for (query.moveToFirst();!(query.isAfterLast()); query.moveToNext()) {
					result = result+query.getString(query.getColumnIndex("name"));
				}
				query.close();
			}
			
		});
		
		
		//search 
		
		search = (Button) findViewById(R.id.searchBtn);
		search.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				resultTextView = (TextView) findViewById(R.id.resultTestView);
				searchEditText = (EditText) findViewById(R.id.searchEditText);
				String roomNum = searchEditText.getText().toString();
				db=mySqliteHelper.getReadableDatabase();
				Cursor query01 = db.query("hotel", new String[]{"roomNum","name","sex","idCard","timeIn","timeSum","money"}, "roomNum=?", new String[]{roomNum}, null, null, "_id asc");
				if(query01.moveToFirst()){
					for (query01.moveToFirst(); !query01.isAfterLast(); query01.moveToNext()) {
						SearchResult +="房间号"+ query01.getString(query01.getColumnIndex("roomNum"))+"\n";
						SearchResult +="入住人姓名"+ query01.getString(query01.getColumnIndex("name"))+"\n";
						SearchResult +="入住人性别"+ query01.getString(query01.getColumnIndex("sex"))+"\n";
						SearchResult +="入住人身份证"+ query01.getString(query01.getColumnIndex("idCard"))+"\n";
						SearchResult +="入住时间"+ query01.getString(query01.getColumnIndex("timeIn"))+"\n";
						SearchResult +="入住人数"+ query01.getString(query01.getColumnIndex("timeSum"))+"\n";
						SearchResult +="已付金额"+ query01.getString(query01.getColumnIndex("money"));
					}
				}else{
					Toast.makeText(getApplicationContext(),"没有相关信息", Toast.LENGTH_SHORT).show();
				}
				
				query01.close();
				resultTextView.setText(SearchResult);
				SearchResult="";
			}
			
		});
		
		
		dropBtn = (Button) findViewById(R.id.dropBtn);
		dropBtn.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				usernameEditText = (EditText) findViewById(R.id.dropUsernameEditText);
				roomNum01EditText = (EditText) findViewById(R.id.dropRoomNumEditText);
				String username = usernameEditText.getText().toString();
				String roomNum = roomNum01EditText.getText().toString();
				Cursor query=null;
					try {
						 query = db.query("hotel", new String[]{"_id"},"name=? and roomNum=?" , new String[]{username,roomNum}, null, null, null);
						if(query.moveToFirst()){
							db.delete("hotel", " name=? and roomNum=?", new String[]{username,roomNum});
							Toast.makeText(getApplicationContext(),"退房成功", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getApplicationContext(),"没有相关信息，请核对再重试", Toast.LENGTH_SHORT).show();
						}
						
					} catch (Exception e) {
						query.close();
						e.printStackTrace();
					}
			
			}
			
		});
		btn02 = (Button) findViewById(R.id.btn02);
		btn02.setOnClickListener(new Button.OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				clean();
			}
			
		});
		
		
		  btn03 = (Button) findViewById(R.id.refreshbtn);
	      btn03.setOnClickListener(new Button.OnClickListener(){
	    	  public void onClick(View v) {
					show = (TextView) findViewById(R.id.show);
					
					db=mySqliteHelper.getReadableDatabase();
					Cursor cursor = db.query("hotel",null,null,null,null,null,null);

					if(cursor.moveToFirst()){

						while (cursor.moveToNext()){

					

					  String roomnum = cursor.getString(6);

					  String name = cursor.getString(1);

					  String timein = cursor.getString(4);
					  
					  String timesum = cursor.getString(5);

					  SearchResult += "  房间号：  " + roomnum + "  姓名： " + name + "   入住时间：  " + timein + "    入住人数：   "+ timesum + "\n";
					   
					
					  }
                      cursor.close();
					 
		              show.setText(SearchResult);
					  SearchResult="";
					 
		             
					
					}
	    	  }
	      } );
		
	}
	
	public void clean(){
		nameEditText = (EditText) findViewById(R.id.nameEditText); 
		radioMan = (RadioButton) findViewById(R.id.man);
		radioWoman = (RadioButton) findViewById(R.id.woman);
		idCardEditText = (EditText)findViewById(R.id.idCardEditText);
		timeInEditText = (EditText)findViewById(R.id.timeInEditText);
		timeSumEditText = (EditText)findViewById(R.id.timeSumEditText);
		roomNumEditText = (EditText)findViewById(R.id.roomNumEditText);
		moneyEditText = (EditText)findViewById(R.id.moneyEditText);
		nameEditText.setText("");
		radioWoman.setChecked(true);
		idCardEditText.setText("");
		timeInEditText.setText("");
		timeSumEditText.setText("");
		roomNumEditText.setText("");
		moneyEditText.setText("");
		
		
	}
	
	
    
}
