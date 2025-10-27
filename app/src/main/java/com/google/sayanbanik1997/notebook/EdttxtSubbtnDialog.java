package com.google.sayanbanik1997.notebook;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

import java.util.HashMap;

public abstract class EdttxtSubbtnDialog {
    Context context;
    //TextView idTxt;
    EditText ed;
    RecyclerView review;
    Button submitBtn;
    String id;
    DbHelper dbHelper;
    EdttxtSubbtnDialog(Context context, String id, String tblName, TextView tv){
        this.context=context;
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.edttxt_subbtn_lut);
        dialog.show();

        dbHelper = new DbHelper(context);

        //idTxt =  dialog.findViewById(R.id.idTxt);
        ed = dialog.findViewById(R.id.ed);
        submitBtn =dialog.findViewById(R.id.subBtn);

        ed.setText(tv.getText().toString());
       
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbHelper.upd(tblName, new String[]{ed.getText().toString()}, "id=?", new String[]{id})>0) {
                    doAfterSubbtnClicked();
                    //tv.setText(ed.getText().toString());
                }else{
                    Toast.makeText(context, "Error while updating", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }
    abstract  void doAfterSubbtnClicked();
    
}
