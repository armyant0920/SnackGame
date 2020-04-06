package com.practice.snackgame;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SnackJson {

    private String PlayerName,Score,Coin;
    private String[]Snack;
    private int Direction;


    public String ConvertToJson(String PlayerName, String Score, String[] Snack, String Coin, int Direction) {
        String s1="[";
        for(int i=0;i<Snack.length;i++){
            s1+="\""+Snack[i]+"\"";
            if(i<Snack.length-1){s1+=",";}
            else{s1+="]";}
        }

        String s2 = "{\"Name\":\""+PlayerName+"\",\"Score\":\""+Score+"\",\"Snack\":"+s1+",\"Coin\":\""+Coin+"\",\"Direction\":\""+Direction+"\"}";
       // "{\"Name\":\"PlayerName\",\"Record\":\"10\",\"Snack\":[\"789\",\"123\",\"456\"],\"Coin\":\"10/10\",\"Direction\":\"1\",\"id\":5}";
        return s2;
    }

    public void ConvertToString(String s){
        JSONObject j = null;
        JSONArray SnackArray=null;

        try {
            j = new JSONObject(s);
            //SnackArray=j.getJSONArray("Snack");//因為Snack(蛇身)是多個xy,故要設一個array節點
            String name=j.getString("Name");
            String score=j.getString("Score");
            String snack=j.getString("Snack");
            String coin=j.getString("Coin");
            String direction=j.getString("Direction");

            Log.d("Player",name+"/"+score+"/"+snack+"/"+coin+"/"+direction);
            for(int i=0;i<SnackArray.length();i++){

                Log.d("snackarray",(String) SnackArray.get(i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void Save(SharedPreferences sp,String PlayerName,String Score,String SnackString,String CoinString,int Direction){
        sp.edit()
                .putString("Name", PlayerName)//紀錄玩家名稱
                .putString("Record", Score)//紀錄目前分數
                .putString("Coin", CoinString)//紀錄目前金幣位置
                .putString("Snack", SnackString)//紀錄蛇身
                .putInt("Direction",Direction)
                .commit();

    }
}
