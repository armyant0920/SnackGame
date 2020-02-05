package com.practice.snackgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class GameOver extends AppCompatActivity {
    Bundle bundle;
    Button btn_back,btn_continue;
    String Name,Score,Snack,Coin,Direction,BS;
//    ActivityManager manager=(ActivityManager) context
//            .getSystemService(Context.ACTIVITY_SERVICE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        btn_back=(Button)findViewById(R.id.btn_back);
        btn_continue=(Button)findViewById(R.id.btn_continue);
        btn_back.setOnClickListener(listener);
        btn_continue.setOnClickListener(listener);

        bundle=getIntent().getExtras();
        if(bundle!=null)
        {   BS=bundle.getString("BS");

            Name=bundle.getString("Name");
            Score=bundle.getString("Score");
            Coin=bundle.getString("Coin");
            Snack=bundle.getString("Snack");
            Direction=String.valueOf(bundle.getInt("Direction"));
        Log.d("DieOn","Name:"+Name+"/"+"Score"+Score+"/"+"Coin:"+Coin+"/"+"Snack:"+Snack+"/"+"方向:"+Direction);
        Log.d("getBS",BS);


        }
        JSONObject j;
        JSONArray jsonArray;
        String PlayerName="Kevin",Score="10",Coin="5/5";
        String Snack[]={"15/1","15/2","15/3","15/4","15/5"};
        int Direction=2;
        SnackJson s=new SnackJson();
        Log.d("JSONTEST",s.ConvertToJson(PlayerName,Score,Snack,Coin,Direction));

        try {
           String tmp = BS;
            s.ConvertToString(tmp);
//            String tmp = s.ConvertToJson(PlayerName,Score,Snack,Coin,Direction);
//            j = new JSONObject(tmp);
//            jsonArray=j.getJSONArray("Snack");
//
//            String name=j.getString("Name");
//            String score=j.getString("Score");
//            String snack=j.getString("Snack");
//            Log.d("Player",name+"/"+score+"/"+snack);
//            for(int i=0;i<jsonArray.length();i++){
//
//                Log.d("json",(String) jsonArray.get(i));
//            }


        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }
    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            Log.d("ApplicationContext",String.valueOf(getApplicationContext()));
            Log.d("Application",String.valueOf(getApplication()));
            switch (v.getId()){
                case R.id.btn_back:
                    intent.setClass(getApplicationContext(),MainActivity.class);
                    break;
                case R.id.btn_continue:
                    intent.setClass(getApplicationContext(),RecordActivity.class);
                    break;

            }
            startActivity(intent);
            finish();

        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(GameOver.this)
                    .setTitle("確定要離開嗎?")


                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"bye",Toast.LENGTH_SHORT).show();
                        finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"stay",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();


        }return true;
    }
}
