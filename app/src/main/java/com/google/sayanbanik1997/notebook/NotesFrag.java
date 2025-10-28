package com.google.sayanbanik1997.notebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class NotesFrag extends Fragment {

    RecyclerView cataReview;
    ArrayList<HashMap<String, String>> cataAl;
    DbHelper dbHelper ;
    Button subBtn;
    EditText cataEdt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        cataReview = view.findViewById(R.id.cataReview);
        subBtn = view.findViewById(R.id.subBtn);
        cataEdt = view.findViewById(R.id.cataEdt);

        cataReview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        dbHelper = new DbHelper(getContext());

        cataAl = dbHelper.rawQry("select * from cataTbl","cataTbl");

        cataEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cataAl = dbHelper.rawQry("select * from `cataTbl` where name like '%"+ cataEdt.getText().toString() +"%'","cataTbl");
                setDataToCataReview();
            }
        });

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cataEdt.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(dbHelper.insrt("cataTbl", new String[]{cataEdt.getText().toString()})>1){
                    cataAl = dbHelper.rawQry("select * from cataTbl","cataTbl");
                    setDataToCataReview();
                    cataEdt.setText("");
                }else{
                    Toast.makeText(getContext(), "Error adding", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setDataToCataReview();
        return view;
    }

    void setDataToCataReview(){
        cataReview.setAdapter( new RecyAdapter( R.layout.each_cata_lut, cataAl.size()){
            @Override
            void bind(Vh holder, int position) {
                TextView cataNameTxt = ((TextView) holder.viewHm.get("cataNameTxt"));
                cataNameTxt.setText(cataAl.get(position).get("name").toString());
                cataNameTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new EdttxtSubbtnDialog(getContext(), cataNameTxt.getText().toString()) {
                            @Override
                            void doAfterSubbtnClicked() {
                                if(dbHelper.upd("cataTbl", new String[]{ed.getText().toString()}, "id=?", new String[]{ cataAl.get(position).get("id")})>0) {
                                    cataNameTxt.setText(ed.getText().toString());
                                }else{
                                    Toast.makeText(context, "Error while updating", Toast.LENGTH_SHORT).show();
                                }

                            }
                        };
                    }
                });
                
                RecyclerView contReview = ((RecyclerView) holder.viewHm.get("contReview"));
                contReview.setLayoutManager(new LinearLayoutManager(getContext()));

                EditText contEdt = ((EditText) holder.viewHm.get("contEdt"));
                contEdt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        //Toast.makeText(getContext(), "enter", Toast.LENGTH_SHORT).show();
                        ArrayList<HashMap<String, String>> contAl = dbHelper.rawQry("select * from `contTbl` where cont like '%"+ contEdt.getText().toString() +"%' and cataId = "+cataAl.get(position).get("id").toString(),"contTbl");
                        setDataToContReview(contReview, contAl, cataAl.get(position).get("id").toString());
                    }
                });

                ((Button) holder.viewHm.get("subBtn")).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(contEdt.getText().toString().isEmpty()) return;
                        if(dbHelper.insrt("contTbl", new String[]{
                                contEdt.getText().toString(), cataAl.get(position).get("id").toString()
                        })>0){
                            setDataToContReview(contReview, dbHelper.rawQry("select * from contTbl where cataId = "+ cataAl.get(position).get("id").toString(), "contTbl"), cataAl.get(position).get("id").toString());
                            contEdt.setText("");
                        }else{
                            Toast.makeText(getContext(), "Error while adding", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                setDataToContReview(contReview, dbHelper.rawQry("select * from contTbl where cataId = "+ cataAl.get(position).get("id").toString(), "contTbl"), cataAl.get(position).get("id").toString());
                ((ImageView)holder.viewHm.get("delBtn")).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext()).setMessage(R.string.Do_you_want_to_delete)
                                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                       if(dbHelper.del("cataTbl", "id=?", new String[]{cataAl.get(position).get("id").toString()})>0){
                                           cataAl = dbHelper.rawQry("select * from cataTbl","cataTbl");
                                           setDataToCataReview();
                                       }
                                    }
                                })
                                .show();

                    }
                });
            }
            @Override
            Vh onCreate(View view) {
                return new Vh(view) {
                    @Override
                    void initiateInsideViewHolder(View itemView) {
                        viewHm.put("cataNameTxt", itemView.findViewById(R.id.cataNameTxt));
                        viewHm.put("contEdt", itemView.findViewById(R.id.contEdt));
                        viewHm.put("subBtn", itemView.findViewById(R.id.subBtn));
                        viewHm.put("contReview", itemView.findViewById(R.id.contReview));
                        viewHm.put("delBtn", itemView.findViewById(R.id.delBtn));
                    }
                };
            }
        });
    }
    void setDataToContReview(RecyclerView review, ArrayList<HashMap<String, String>> contAl, String cataId){
        //Toast.makeText(getContext(), contAl.size()+"", Toast.LENGTH_SHORT).show();
        review.setAdapter( new RecyAdapter( R.layout.each_cont_lut, contAl.size()){
            @Override
            void bind(Vh holder, int position) {
                TextView contTxt = ((TextView) holder.viewHm.get("contTxt"));
                contTxt.setText(contAl.get(position).get("cont"));
                contTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new EdttxtSubbtnDialog(getContext(), contTxt.getText().toString()) {
                            @Override
                            void doAfterSubbtnClicked() {
                                if(dbHelper.upd("contTbl", new String[]{ed.getText().toString()}, "id=?", new String[]{ contAl.get(position).get("id")})>0) {
                                    contAl.get(position).put("cont", ed.getText().toString());
                                    setDataToContReview(review, contAl, cataId);
                                }else{
                                    Toast.makeText(context, "Error while updating", Toast.LENGTH_SHORT).show();
                                }

                            }
                        };
                    }
                });
                ImageView delBtn = ((ImageView) holder.viewHm.get("delBtn"));
                delBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext()).setMessage(R.string.Do_you_want_to_delete)
                                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(dbHelper.del("contTbl", "id=?", new String[]{contAl.get(position).get("id").toString()})>0){
                                            setDataToContReview(review, dbHelper.rawQry("select * from contTbl where cataId = "+ cataId, "contTbl"), cataId);
                                        }
                                    }
                                })
                                .show();
                    }
                });
            }
            @Override
            Vh onCreate(View view) {
                return new Vh(view) {
                    @Override
                    void initiateInsideViewHolder(View itemView) {
                        viewHm.put("contTxt", itemView.findViewById(R.id.contTxt));
                        viewHm.put("delBtn", itemView.findViewById(R.id.delBtn));
                    }
                };
            }
        });
    }
}