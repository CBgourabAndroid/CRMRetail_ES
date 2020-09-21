package com.crmretail_es.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.crmretail_es.R;
import com.crmretail_es.modelClass.EditModel;

import java.util.ArrayList;

/**
 * Created by Parsania Hardik on 17-Apr-18.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    public static ArrayList<EditModel> editModelArrayList;


    public CustomAdapter(Context ctx, ArrayList<EditModel> editModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.editModelArrayList = editModelArrayList;
    }

    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.product_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.MyViewHolder holder, final int position) {


        holder.editText.setText(editModelArrayList.get(position).getEditTextValue());
        holder.produc_tName.setText(editModelArrayList.get(position).getProductName());

        Log.d("print","yes");

    }

    @Override
    public int getItemCount() {
        return editModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        protected EditText editText;
        protected TextView produc_tName;

        public MyViewHolder(View itemView) {
            super(itemView);

            editText = (EditText) itemView.findViewById(R.id.prduct_qty);
            produc_tName=(TextView)itemView.findViewById(R.id.productName);


            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                      editModelArrayList.get(getAdapterPosition()).setEditTextValue(editText.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

    }
}
