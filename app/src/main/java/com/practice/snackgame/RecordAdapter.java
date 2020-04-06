package com.practice.snackgame;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecordAdapter extends  RecyclerView.Adapter<RecordAdapter.ViewHolder>{
    LayoutInflater inflater;
    public ArrayList<Record>Recordlist;
    private Context mCtx;
    private TableLayout select_table;
    private int pickposition=0;
    public RecordAdapter(Context context,ArrayList<Record>RecordArrayList){
        this.Recordlist=RecordArrayList;
        this.mCtx=context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }
    @NonNull
    @Override
    //插入頁面
    public RecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recordlist, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.Player_Name=(TextView) v.findViewById(R.id.txt_name);
        vh.Player_Score=(TextView) v.findViewById(R.id.txt_score);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.ViewHolder holder, int position) {
        Record r=Recordlist.get(position);
//        if(position==pickposition)
//        {holder.table.setBackgroundColor(Color.RED);}
        holder.Player_Name.setText(r.getPlayerName());
        holder.Player_Score.setText(r.getPlayerScore());
        holder.record_id.setText("記錄"+(position+1));


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    public int getPosition(){
        return  pickposition;
    };
    public TableLayout getTable(){
        return select_table;
    }
//    private View.OnClickListener listener=new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//
//        }
//    };

    @Override
    public int getItemCount() {
        return Recordlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView Player_Name;
        private TextView Player_Score;
        private TextView record_id;
        private TableLayout table;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Player_Name=itemView.findViewById(R.id.txt_name);
            Player_Score=itemView.findViewById(R.id.txt_score);
            table=itemView.findViewById(R.id.table);
            record_id=itemView.findViewById(R.id.Record_ID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(select_table!=null){
                        select_table.setBackgroundColor(Color.BLACK);
                    }
                    select_table=table;
                    select_table.setBackgroundColor(Color.RED);
                    pickposition=getAdapterPosition();
                    Toast.makeText(mCtx,"選取的是"+getAdapterPosition(),Toast.LENGTH_SHORT).show();


                }
            });

        }
    }
}
