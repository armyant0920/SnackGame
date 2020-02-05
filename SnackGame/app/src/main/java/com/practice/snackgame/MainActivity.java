package com.practice.snackgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    ActionBar actionBar;

    final int GS=50,row=17,column=17;//撇除牆壁,實際可動範圍15*15=225
    public static int count=0,speed=500;
    SeekBar seekBar;
    SharedPreferences SP;
    Bitmap wilsion,money;
    Random rnd=new Random();
    String PN;
    Paint p=new Paint();
    Boolean run=false,alive=true,allowChange=true;

    private String SL;
    //為了方便控制記錄檔,宣告以下靜態變數(static)
    //又為了讓另一個Activity可以直接存取以下變數,必須宣告為public,
        public static TextView countText,PlayerName;
        public static DrawView view;
        public static String Name,Coin, Snack;

        public static SnackBody MoneyPoint;
        public static ArrayList<SnackBody> SB;
        public static Button PressNow;
        public static Button PressUP, PressRIGHT, PressDOWN, PressLEFT, PressPause, btn_name, btn_save, btn_load, btn_test;
        public static int[][] map = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };

    //音樂
    public MediaPlayer mMediaPlayer = null;
    int state;
    int ready=0,play=1,pause=2,stop=3,error=4;
    private Boolean mbIsInitialised = true;
    private Uri uri;
    //聲音
    SoundPool soundPool;
    HashMap<String, Integer> musicId=new HashMap<String, Integer>();
    private Thread threadAnime;
    private Handler handler;
    //方向
    public static int UP=0,RIGHT=1,DOWN=2,LEFT=3,DIRECTION;
    //紀錄
    private String CoinString,SnackString;
    //地圖

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mbIsInitialised=false;
        mediaPlayer.seekTo(0);
        mediaPlayer.start();
        state=play;
//        Toast.makeText(MainActivity.this, "開始播放", Toast.LENGTH_LONG)
//                .show();
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
    //如果不是循環撥放,再想這要用甚麼好了
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume","onResume");
        seekBar.setProgress((1000-speed)/100);
        alive=true;

        Log.d("SPEED",String.valueOf(1000-speed));
        SetSound();
        view.invalidate();

    }
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mMediaPlayer.release();
        state=error;

        Toast.makeText(MainActivity.this, "發生錯誤，停止播放", Toast.LENGTH_LONG)
                .show();
        return true;
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onStop","onStop");
        Pause();
        SoundRelease();
//        state=stop;
//        //mMediaPlayer.reset();
//        mMediaPlayer.release();
//        mbIsInitialised = true;
//        mMediaPlayer = null;
    }

    public class DrawView extends View{
        public DrawView(Context context) {
            super(context);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            p.setColor(Color.GRAY);
            canvas.drawRect(0, 0, column*GS, row*GS, p);// 正方形
            for (int i=0;i<column;i++) {

                for (int j = 0; j <row; j++) {
                    p.setColor(Color.BLACK);
                    p.setStrokeWidth(5.0f);
                    canvas.drawLine(i*GS, j*GS, (i+1)*GS, (j)*GS, p);//劃出方格線-橫線
                    canvas.drawLine(i*GS, j*GS, (i)*GS, (j+1)*GS, p);//劃出方格線-直線

                    switch(map[j][i])
                    {
                        case 1:
                            p.setColor(Color.RED);
                            canvas.drawRect(i*GS, j*GS, (i+1)*GS, (j+1)*GS, p);
                            break;
                        case 2:
                            canvas.drawBitmap(money,i*GS,j*GS,p);

                            //canvas.drawBitmap(money,(i-1)*GS,(j-1)*GS,p);
                            break;
                    }

                }
            }
//            for(SnackBody sb:SB){
//                canvas.drawBitmap(wilsion,sb.getPointX()*GS,sb.getPointY()*GS,p);
//            }
            for(int i=0;i<SB.size();i++){
                SnackBody sb=SB.get(i);
                if(i==SB.size()-1){
                    p.setColor(Color.YELLOW);
                    canvas.drawRect(sb.getPointX()*GS, sb.getPointY()*GS, (sb.getPointX()+1)*GS, (sb.getPointY()+1)*GS, p);
                }
                canvas.drawBitmap(wilsion,sb.getPointX()*GS,sb.getPointY()*GS,p);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        actionBar=getSupportActionBar();
        actionBar.hide();
    }
    private class AnimationThread extends Thread {
        public void run(){
            while(state!=stop) {
                if(run==true){
                SnackReset();
                allowChange=true;
                view.invalidate();}
                try{
                    Thread.sleep(speed); }
                catch (Exception e) {
                    e.printStackTrace(); }
            }
        }
    }
    //輸入名稱
    private void InputName(){
        final  View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.input_name,null);
        final EditText ET=item.findViewById(R.id.editText);
        ET.setText(PN);
        ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ET.setText("");
            }
         });
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("請輸入名子(點下方清空)")
                .setView(item)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText=item.findViewById(R.id.editText);
                        PN = editText.getText().toString();
                        if(TextUtils.isEmpty(PN)){
                            Toast.makeText(getApplicationContext(), "尚未輸入名稱", Toast.LENGTH_SHORT).show();
                        } else {
                            PlayerName.setText(PN);
                        }
                    }
                })
                .show();
    }
    private void Record(){
        final View recordlist=LayoutInflater.from(MainActivity.this).inflate(R.layout.recordlist,null);
        final TextView txt_name,txt_record;
        txt_name=(TextView)recordlist.findViewById(R.id.txt_name);
        txt_record=(TextView)recordlist.findViewById(R.id.txt_score);

        //從SharedPreferences抓出上一次存過的資料
        txt_name.setText(getSharedPreferences("GR",MODE_PRIVATE).getString("Name","PlayerName"));
        txt_record.setText(getSharedPreferences("GR",MODE_PRIVATE).getString("Score","0"));

        recordlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SL=="save") {//如果目前是存檔動作,就把這個紀錄檔的文字覆蓋掉
                    txt_name.setText(PN);
                    txt_record.setText(String.valueOf(count));
                    CoinString = MoneyPoint.getPointX() + "/" +MoneyPoint.getPointY();
                    Log.d("CoinPoint", CoinString);
                    String test[] = CoinString.split(";");
                    for (String s : test) {
                        Log.d("Spilt", s);
                    }
                    SnackString = "";
                    for (SnackBody s : SB) {
                        SnackString += s.getPointX() + "/" + s.getPointY() + ";";

                    }
                    Log.d("SNACK", SnackString);
                }
                else if(SL=="load"){//

                }
            }
        });

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("選擇紀錄")
                .setView(recordlist)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String name = txt_name.getText().toString();
                        String score=txt_record.getText().toString();

                        if(SL=="save") {
                            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(score)) {
                                Toast.makeText(getApplicationContext(), "資料不全", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "已將資料存入SP" + "\t" + name + "/" + score, Toast.LENGTH_SHORT).show();

                                SP.edit()
                                        .putString("Name", name)//紀錄玩家名稱
                                        .putString("Score", score)//紀錄目前分數
                                        .putString("Coin", CoinString)//紀錄目前金幣位置
                                        .putString("Snack", SnackString)//紀錄蛇身
                                        .putInt("Direction",DIRECTION)
                                        .commit();

                            }
                        }else if(SL=="load"){
                            PN=SP.getString("Name","PlayerName");

                            PlayerName.setText(PN);
                            count=Integer.parseInt(SP.getString("Score","0"));
                            String CoinPoint[]=CoinString.split("/");

                            countText.setText(String.valueOf(count));
                            map[MoneyPoint.getPointY()][MoneyPoint.getPointX()]=0;
                            MoneyPoint.setPointX(Integer.parseInt(CoinPoint[0]));
                            MoneyPoint.setPointY(Integer.parseInt(CoinPoint[1]));
                            Log.d("CoinX",String.valueOf(MoneyPoint.getPointX()));
                            Log.d("CoinY",String.valueOf(MoneyPoint.getPointY()));
                            map[MoneyPoint.getPointY()][MoneyPoint.getPointX()]=2;
                            SnackString = SP.getString("Snack","1/1;2/1;3/1;4/1;5/1");
                            String snack[]=SnackString.split(";");
                            DIRECTION=SP.getInt("Direction",1);
                            if(PressNow!=null){PressNow.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);}
                                switch (DIRECTION){
                                    case 0:PressNow=PressUP;
                                        PressNow.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                                        break;
                                    case 1:PressNow=PressRIGHT;
                                        PressNow.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                                        break;
                                    case 2:PressNow=PressDOWN;
                                        PressNow.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                                        break;
                                    case 3:PressNow=PressLEFT;
                                        PressNow.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                                        break;
                            }
                            SB.clear();
                            for(String s1:snack){
                                String s2[]=s1.split("/");
                                SnackBody snackBody=new SnackBody(Integer.parseInt(s2[0]),Integer.parseInt(s2[1]));
                                SB.add(snackBody);
                            }
                            view.invalidate();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
//        Dialog.getButton(DialogInterface.BUTTON_POSITIVE).setVisibility(View.GONE);

//        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
//        dialog.setTitle("Test");
//        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });

//        dialog.setPositiveButton(new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
    }

    View.OnClickListener btnListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_name:
                    Pause();
                    InputName();
                    break;
                case R.id.btn_save:
//                    String name=PlayerName.getText().toString();
//                    Toast.makeText(getApplicationContext(), "已將" + name+"存入SP", Toast.LENGTH_SHORT).show();
//                    SharedPreferences SP=getSharedPreferences("GR",MODE_PRIVATE);
//                    SP.edit().putString("Name",name)
//                            .commit();
                    Pause();
                    SL = "save";
                    Record();
                    break;
                case R.id.btn_load:
                    Pause();
                    SL = "load";
                    Record();
                    break;
                case R.id.TEST:
                     Pause();
//                     mMediaPlayer.stop();
//                    mMediaPlayer.release();
//                    mbIsInitialised = true;

                    Intent i = new Intent(getApplicationContext(), RecordActivity.class);
                    SaveExtra(i);
                    //mMediaPlayer.stop();
                    startActivity(i);

                    break;
                case R.id.btn_pause:
                    if(run==false){
                        PressPause.setBackgroundResource(android.R.drawable.ic_media_pause);
                        run=true;
                        state=play;
                        if(mbIsInitialised) {
                            mMediaPlayer.prepareAsync();

                        }else{
                            mMediaPlayer.start();}
                    } else{ Pause();

                    }
                    break;
            }
        }
    };
    private void SaveExtra(Intent i){


        PN=PlayerName.getText().toString();
        Name = PN;
        Log.d("Name",Name);

        Coin = MoneyPoint.getPointX() + "/" + MoneyPoint.getPointY();
        Snack = "";

        for (SnackBody s : SB) {
            Snack += s.getPointX() + "/" + s.getPointY() + ";";
        }
        String Snack2[]=new String[SB.size()];
        for(int j=0;j<SB.size();j++){
            Snack2[j]=SB.get(j).getPointX()+"/"+SB.get(j).getPointY();
        }
        SnackJson snackJson=new SnackJson();

        String BS=snackJson.ConvertToJson(Name,String.valueOf(count),Snack2,Coin,DIRECTION);
        i.putExtra("BS",BS);
        i.putExtra("Name", Name);
        i.putExtra("Score", String.valueOf(count));
        i.putExtra("Coin", Coin);
        i.putExtra("Snack", Snack);
        i.putExtra("Direction", DIRECTION);

    }
    private void Pause(){
            PressPause.setBackgroundResource(android.R.drawable.ic_media_play);
            run=false;
            if(mMediaPlayer.isPlaying())
            {state=pause;
            mMediaPlayer.pause();}

    }
    //方向與按鈕事件設定
    View.OnClickListener ArrowListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(PressNow!=null) {PressNow.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);}
            switch (v.getId()){

                case R.id.btn_up:
                    if(DIRECTION!=DOWN && DIRECTION!=UP && allowChange==true)
                    DIRECTION=UP;
                    PressNow=PressUP;
                    PressNow.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                    allowChange=false;
                    break;
                case R.id.btn_left:
                    if(DIRECTION!=RIGHT && DIRECTION!=LEFT && allowChange==true)
                        DIRECTION=LEFT;
                    allowChange=false;
                    PressNow=PressLEFT;
                    PressNow.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                    break;
                case R.id.btn_right:
                    if(DIRECTION!=LEFT && DIRECTION!=RIGHT && allowChange==true)
                    DIRECTION=RIGHT;
                    allowChange=false;
                    PressNow=PressRIGHT;
                    PressNow.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);//
                    break;
                case R.id.btn_down:
                    if(DIRECTION!=UP && DIRECTION!=DOWN && allowChange==true)
                    DIRECTION=DOWN;
                    allowChange=false;
                    PressNow=PressDOWN;
                    PressNow.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);//OVERALY或XOR也不錯
                    break;
            }

            Log.d("DIRECTION",String.valueOf(DIRECTION));
        }
    };
    //初始化
    private void init() {
        PlayerName = (TextView) findViewById(R.id.PlayerName);
        SP=getSharedPreferences("GR",MODE_PRIVATE);
        int x,y;
        x=1+rnd.nextInt(15);
        y=1+rnd.nextInt(15);
        CoinString = SP.getString("Coin", x+"/"+y);
        PN=SP.getString("Name","PlayerName");
        PlayerName.setText(PN);
        countText = (TextView) findViewById(R.id.countText);
        count=0;
        countText.setText(String.valueOf(count));

        //按鍵初始化
        {   btn_test=(Button)findViewById(R.id.TEST);
            btn_test.setOnClickListener(btnListener);
            btn_name = (Button) findViewById(R.id.btn_name);
            btn_name.setOnClickListener(btnListener);
            btn_save = (Button) findViewById(R.id.btn_save);
            btn_save.setOnClickListener(btnListener);
            btn_load = (Button) findViewById(R.id.btn_load);
            btn_load.setOnClickListener(btnListener);
            PressUP = (Button) findViewById(R.id.btn_up);
            PressLEFT = (Button) findViewById(R.id.btn_left);
            PressRIGHT = (Button) findViewById(R.id.btn_right);
            PressDOWN = (Button) findViewById(R.id.btn_down);
            PressPause = (Button) findViewById(R.id.btn_pause);
            PressUP.setOnClickListener(ArrowListener);
            PressLEFT.setOnClickListener(ArrowListener);
            PressRIGHT.setOnClickListener(ArrowListener);
            PressDOWN.setOnClickListener(ArrowListener);
            PressPause.setOnClickListener(btnListener);
        }
        //調整速度seejbar
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = 1000 - progress * 100;
                Log.d("SPEED",String.valueOf((progress+1)*100));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        //圖片設定初始化
        {
            wilsion = BitmapFactory.decodeResource(getResources(), R.drawable.wilson);
            //設定角色圖片
            Log.d("height", String.valueOf(wilsion.getHeight()));
            Log.d("height", String.valueOf(wilsion.getWidth()));
            //計算長寬邊要縮放的比例
            float scaleWidth = ((float) GS / wilsion.getWidth());
            float scaleHeight = ((float) GS / wilsion.getHeight());
            //計算縮放matrix參數
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            //重製圖片
            wilsion = Bitmap.createBitmap(wilsion, 0, 0, wilsion.getWidth(), wilsion.getHeight(), matrix, true);
            //$的圖片比照辦理,但matrix好像不能重複使用?

            money = BitmapFactory.decodeResource(getResources(), R.drawable.money);

            //計算長寬邊要縮放的比例
            float scaleWidth2 = ((float) GS / money.getWidth());
            float scaleHeight2 = ((float) GS / money.getHeight());
            //計算縮放matrix參數
            //matrix = new Matrix();
            Matrix matrix2 = new Matrix();
            matrix2.postScale(scaleWidth2, scaleHeight2);
            //重製圖片
            money = Bitmap.createBitmap(money, 0, 0, money.getWidth(), money.getHeight(), matrix2, true);

            //本來覺得奇怪 為何wilson會有背景白色 後來才發現是自己蠢了,因為我沒有畫預設地板的底色阿XDDD
        }

        //添加一個view到Layout中
        {LinearLayout layout = (LinearLayout) findViewById(R.id.root);
        view = new MainActivity.DrawView(this);// final GameMap.DrawView
        //這是畫布大小設定,先大概抓的,手機實際寬高單位不知道是甚麼,寬要到差不多2000才滿屏
        view.setMinimumHeight(2000);
        view.setMinimumWidth(600);
        //通知view组件重绘
        view.invalidate();
        layout.addView(view);}

        //初始化蛇身,放金幣
        {DIRECTION = RIGHT;
            SB = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                SnackBody snackBody = new SnackBody(i, 1);
                Log.d("SNACK", "X=" + String.valueOf(snackBody.getPointX()) + "Y=" + String.valueOf(snackBody.getPointY()));
                SB.add(snackBody); }
            CreateMoney();
        }

        //處理音效&音樂
        {
            //音樂播放
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.canon);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setLooping(true);

//            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer.setLooping(true);

            try {
                mMediaPlayer.setDataSource(this, uri);
                state=ready;
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "指定的音樂檔錯誤！", Toast.LENGTH_LONG)
                        .show();
            }

            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            threadAnime=new Thread(new AnimationThread());
            threadAnime.start();
        }
        //Handler處理
        {handler=new Handler(){
            @Override
            public void handleMessage(Message msg){
                countText.setText(String.valueOf(count));
            }
        };}
    }
    //SoundPool資源載入
    private void SetSound(){
        //soundPool=new SoundPool(12, 0,5);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();
        musicId.put("money", soundPool.load(this, R.raw.coin05, 1));//吃到錢
        musicId.put("bump", soundPool.load(this, R.raw.surprise, 1));//撞牆
        musicId.put("walk", soundPool.load(this, R.raw.button04a, 1));//走路聲音
        musicId.put("die", soundPool.load(this, R.raw.powerdown07, 1));//吃到自己
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d("status", sampleId + String.valueOf(status));
                if (status == 0) {
                    Log.d("load", musicId.get("money") + "讀取完成");
                }
            }
        });
    }
    private void SoundRelease(){
        soundPool.release();
        soundPool=null;
    }
    private void SnackReset() {

        SnackBody TempPoint=SB.get(SB.size()-1);
        if(DIRECTION==UP){
            TempPoint=new SnackBody(TempPoint.getPointX(),TempPoint.getPointY()-1);
        }else if(DIRECTION==DOWN){
            TempPoint=new SnackBody(TempPoint.getPointX(),TempPoint.getPointY()+1);
        }
        else if (DIRECTION==LEFT){
            TempPoint=new SnackBody(TempPoint.getPointX()-1,TempPoint.getPointY());
        }
        else if (DIRECTION==RIGHT){
            TempPoint=new SnackBody(TempPoint.getPointX()+1,TempPoint.getPointY());
        }
        Log.d("TempPoint","X:"+String.valueOf(TempPoint.getPointX())+"Y:"+String.valueOf(TempPoint.getPointY()));

        for(SnackBody s:SB){//如果下一動會吃到自己,死掉
            if(s.getPointX()==TempPoint.getPointX() && s.getPointY()==TempPoint.getPointY()){

                alive=false;
                Log.d("死因","吃到自己"+"目前方向"+DIRECTION);
                break;
            }
        }
        //判斷下一格是甚麼
        if(map[TempPoint.getPointY()][TempPoint.getPointX()]==1){//碰到牆壁,死掉
            alive=false;
            soundPool.play(2,1,1,1,0,1);
            Log.d("死因","撞牆");

        }
        //下一格是普通地板,那就去尾
        else if (map[TempPoint.getPointY()][TempPoint.getPointX()]==0){
            SB.remove(0);
            SB.add(TempPoint);
            for(int i=0;i<SB.size();i++){
                Log.d("SNACK","X="+String.valueOf(SB.get(i).getPointX())+"Y="+String.valueOf(SB.get(i).getPointY()));
            }


        }
        //如果下一格是$,撥放音效並且不去尾
        else if(map[TempPoint.getPointY()][TempPoint.getPointX()]==2){
            map[MoneyPoint.getPointY()][MoneyPoint.getPointX()]=0;

            count++;
            soundPool.play(1,1,1,1,0,1);

            handler.sendEmptyMessage(count);
            SB.add(TempPoint);
            for(int i=0;i<SB.size();i++){
                Log.d("SNACK","X="+String.valueOf(SB.get(i).getPointX())+"Y="+String.valueOf(SB.get(i).getPointY()));
            }
            CreateMoney();
        }

        if(alive==true){//如果活著,就執行:
            soundPool.play(3,1,1,1,0,1);
        }else{run=false;//死掉,就執行
           Log.d("DEAD","GameOver");
            soundPool.play(4,1,1,1,0,1);
           jump();
        }
    }

    private void CreateMoney(){
        if(MoneyPoint!=null)
        {map[MoneyPoint.getPointY()][MoneyPoint.getPointX()]=0;}
        MoneyPoint=new SnackBody((1+rnd.nextInt(15)),(1+rnd.nextInt(15)));
        Log.d("MoneyPoint","X:"+String.valueOf(MoneyPoint.getPointX())+"Y:"+String.valueOf(MoneyPoint.getPointY()));
        for(SnackBody s:SB){
            if(s.getPointX()==MoneyPoint.getPointX() && s.getPointY()==MoneyPoint.getPointY()){
                CreateMoney();
                Log.d("Money","Retry");
                break;
            }
            else{


            }
        }
        Log.d("MoneyOri",String.valueOf(map[MoneyPoint.getPointY()][MoneyPoint.getPointX()]));
        map[MoneyPoint.getPointY()][MoneyPoint.getPointX()]=2;
        Log.d("Money",String.valueOf(map[MoneyPoint.getPointY()][MoneyPoint.getPointX()]));
        Log.d("Money","Created");


    }
    public void jump(){
        Intent intent=new Intent(MainActivity.this,GameOver.class);
        SaveExtra(intent);
        startActivity(intent);
        finish();


    }

}

