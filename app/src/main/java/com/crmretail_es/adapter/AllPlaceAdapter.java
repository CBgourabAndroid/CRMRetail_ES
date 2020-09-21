package com.crmretail_es.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.crmretail_es.R;
import com.crmretail_es.modelClass.Location;
import com.crmretail_es.shared.ConnectionDetector;
import com.crmretail_es.shared.UserShared;

import org.apache.http.entity.mime.MultipartEntity;

import java.util.ArrayList;

public class AllPlaceAdapter extends RecyclerView.Adapter<AllPlaceAdapter.ViewHolder> {

    private ArrayList<Location> items = new ArrayList<>();
    Activity context;
    public static  ArrayList<String> xyz=new ArrayList<>();
    ArrayList<String> abc=new ArrayList<>();

    SparseBooleanArray itemStateArray= new SparseBooleanArray();
    ConnectionDetector connection;
    UserShared psh;
    ProgressDialog progressDialog;
    String responseString=null;
    private MultipartEntity reqEntity;
    private static String TAG;
    private TextView doneTxt;
    private PopupWindow popupWindow;
    private EditText locationTxt;



    public AllPlaceAdapter(Activity context, ArrayList<Location> items, TextView doneTxt, PopupWindow popupWindow, EditText location_edt) {
        this.context=context;
        this.items=items;
        this.doneTxt=doneTxt;
        this.popupWindow=popupWindow;
        this.locationTxt=location_edt;



    }



    @Override
    public AllPlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // context = parent.getContext();
        int layoutForItem =  R.layout.all_places;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutForItem, parent, false);
        return new AllPlaceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllPlaceAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public void loadItems(ArrayList<Location> tournaments) {
        this.items = tournaments;
        notifyDataSetChanged();
    }




    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckedTextView mCheckedTextView;
        TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            mCheckedTextView = (CheckedTextView) itemView.findViewById(R.id.checked_text_view);
            name=itemView.findViewById(R.id.name);


            itemView.setOnClickListener(this);
        }

        void bind(int position) {

            connection=new ConnectionDetector(context);
            psh=new UserShared(context);

            // use the sparse boolean array to check
            if (!itemStateArray.get(position, false)) {
                mCheckedTextView.setChecked(false);}
            else {
                mCheckedTextView.setChecked(true);
            }

            // mCheckedTextView.setText(String.valueOf(items.get(position).getPosition()));


            name.setText(items.get(position).getZoneName());
            doneTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (xyz.size()!=0){
                  //      replaceFragment(new NearByMapFragment(tabLayout,Latitude,Longtitude,xyz));
                        popupWindow.dismiss();


                    }
                    else{
                        showToastLong("Please Select Atlist One Place");
                    }
                }
            });





        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            if (!itemStateArray.get(adapterPosition, false)) {
                mCheckedTextView.setChecked(true);
                itemStateArray.put(adapterPosition, true);
                xyz.add(String.valueOf(items.get(adapterPosition).getZoneId()));
                abc.add(String.valueOf(items.get(adapterPosition).getZoneName()));
               // Toast.makeText(context, String.valueOf(xyz), Toast.LENGTH_SHORT).show();
                locationTxt.setText(abc.toString().substring(1, abc.toString().length()-1));
                popupWindow.dismiss();
            }
            else  {
                mCheckedTextView.setChecked(false);
                itemStateArray.put(adapterPosition, false);
                xyz.remove(String.valueOf(items.get(adapterPosition).getZoneId()));
                abc.add(String.valueOf(items.get(adapterPosition).getZoneName()));
                //Toast.makeText(context, String.valueOf(xyz), Toast.LENGTH_SHORT).show();
                locationTxt.setText(abc.toString().substring(1, abc.toString().length()-1));
            }


        }

    }
    private void showToastLong(String s) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show();
    }



    /*private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit();
    }

*/



}
