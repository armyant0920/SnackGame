package com.practice.snackgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {
    ArrayList<Record> datas=new ArrayList<>();
    RecyclerView mRecyclerView;
    Button btn_back,btn_save,btn_load;
    Bundle bundle;
    private String Name,Score,Snack,Coin;
    private int Direction;
    private TableLayout table_pick;
    private int pickPosition;
//    private SharedPreferences SP;//控制要存取的SP
    SharedPreferences SP[]=new SharedPreferences[3];
    private RecordAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        bundle=getIntent().getExtras();
        Name=bundle.getString("Name");
        Score=bundle.getString("Score");
        Snack=bundle.getString("Snack");
        Coin=bundle.getString("Coin");
        Direction=bundle.getInt("Direction");


        for(int i=0;i<SP.length;i++){
            SP[i]=getSharedPreferences("GR"+i,MODE_PRIVATE);
            String name = SP[i].getString("Name","null");
            String record=SP[i].getString("Record","0");
            String snack=SP[i].getString("Snack","0/1;1/1;2/1;3/1;4/1;5/1");
            String coin=SP[i].getString("Coin","15/15");
            int direction=SP[i].getInt("Direction",0);
            datas.add(new Record(name,record,snack,coin,direction));
        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new RecordAdapter(this, datas);
        mRecyclerView.setAdapter(adapter);
       // mRecyclerView.setOnClickListener(btnListener);
        btn_back=(Button)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(btnListener);
        btn_save=(Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(btnListener);
        btn_load=(Button)findViewById(R.id.btn_load);
        btn_load.setOnClickListener(btnListener);


        }

        View.OnClickListener btnListener=new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Log.d("pick",String.valueOf(adapter.getPosition()));
                table_pick=adapter.getTable();
                pickPosition=adapter.getPosition();

                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                switch (v.getId()){
                    case R.id.btn_back:
                        startActivity(i);
                        //finish();
                        break;
                    case R.id.btn_save:
                        if(table_pick==null){
                         Toast.makeText(getApplicationContext(),"尚未選取記錄",Toast.LENGTH_SHORT).show();
                        }else
                        {save();
                            Toast.makeText(getApplicationContext(),"選取記錄"+String.valueOf(pickPosition+1),Toast.LENGTH_SHORT).show();}
                        break;
                    case R.id.btn_load:
                        if(table_pick==null){
                            Toast.makeText(getApplicationContext(),"尚未選取記錄",Toast.LENGTH_SHORT).show();}
                        else{Toast.makeText(getApplicationContext(),"選取記錄"+String.valueOf(pickPosition+1),Toast.LENGTH_SHORT).show();
                            load();
                            MainActivity.view.invalidate();
                            startActivity(i);
                            //finish();
                        }
                            break;
//                    case R.id.RecyclerView:
//                        adapter.getItemCount();
//                        adapter.getItemId();
//                        mRecyclerView.findViewHolderForAdapterPosition(adapter.getItemId());
//                        break;

                }

            }
        };

    private void save(){
        Record r=datas.get(pickPosition);
        r.setPlayerName(Name);
        r.setPlayerRecord(Score);
        r.setCoinPoint(Coin);
        r.setSnack(Snack);
        r.setFaceDirection(Direction);

        String name,record,coinString,snackString;
        int direction;
        name=r.getPlayerName();
        record=r.getPlayerScore();
        direction=r.getFaceDirection();
        coinString=r.getCoinPoint();
        snackString=r.getSnack();

        SP[pickPosition].edit()
                .putString("Name", name)//紀錄玩家名稱
                .putString("Record", record)//紀錄目前分數
                .putString("Coin", coinString)//紀錄目前金幣位置
                .putString("Snack", snackString)//紀錄蛇身
                .putInt("Direction",direction)
                .commit();
        Log.d("SNCAKCHECK",SP[pickPosition].getString(Snack,""));
        adapter.notifyDataSetChanged();

     }
    private void load(){
        Record r=datas.get(pickPosition);
        MainActivity.Name=r.getPlayerName();
        MainActivity.Score=r.getPlayerScore();
        String coin[]=r.getCoinPoint().split("/");
        //載入金幣
        MainActivity.map[MainActivity.MoneyPoint.getPointY()][MainActivity.MoneyPoint.getPointX()]=0;
        MainActivity.MoneyPoint.setPointX(Integer.parseInt(coin[0]));
        MainActivity.MoneyPoint.setPointX(Integer.parseInt(coin[1]));
        MainActivity.map[MainActivity.MoneyPoint.getPointY()][MainActivity.MoneyPoint.getPointX()]=2;
        //載入方向
        MainActivity.Direction=r.getFaceDirection();
        //載入蛇
        String snack[]=r.getSnack().split(";");
        MainActivity.SB.clear();
        for(String s1:snack){
            String s2[]=s1.split("/");
            SnackBody snackBody=new SnackBody(Integer.parseInt(s2[0]),Integer.parseInt(s2[1]));
            MainActivity.SB.add(snackBody);}
        if(MainActivity.PressNow!=null) {MainActivity.PressNow.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);}
            switch (MainActivity.DIRECTION){
                case 0:MainActivity.PressNow=MainActivity.PressUP;
                    MainActivity.PressNow.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                    break;
                case 1:MainActivity.PressNow=MainActivity.PressRIGHT;
                    MainActivity.PressNow.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                    break;
                case 2:MainActivity.PressNow=MainActivity.PressDOWN;
                    MainActivity.PressNow.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                    break;
                case 3:MainActivity.PressNow=MainActivity.PressLEFT;
                    MainActivity.PressNow.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                    break;
        }


    }
}

