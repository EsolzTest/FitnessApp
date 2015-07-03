package com.esolz.fitnessapp.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esolz.fitnessapp.R;
import com.esolz.fitnessapp.datatype.SearchResults;

import java.util.LinkedList;


/**
 * Created by su on 22/6/15.
 */
public class appoint_listview_adapter extends ArrayAdapter<SearchResults> {

    Context con;
    //ArrayList<HashMap<String, String>> data;
    LayoutInflater lf;
    ViewHolder holder;
    RelativeLayout listitemContainer;
    LinkedList<SearchResults> all_feed_list;
    //FragmentTransaction fragmentTransaction;
    //FragmentManager fragmentManager;
    SearchResults obj;

    boolean[] state;
    public appoint_listview_adapter(Context context, int resource,LinkedList<SearchResults> objects)
    {
        super(context, resource,objects);
        con = context;
        all_feed_list=objects;
        lf=(LayoutInflater)con.getSystemService(con.LAYOUT_INFLATER_SERVICE);
        state=new boolean[all_feed_list.size()];
    }

    @Override
    public int getCount()

    {
        return all_feed_list.size();
    }

    public View getView(  final int position, View convertView, ViewGroup parent)



    {

        //final int pos=position;
        holder=new ViewHolder();
        //final int pos=position;
        //LayoutInflater lf=(LayoutInflater)con.getSystemService(con.LAYOUT_INFLATER_SERVICE);

        if(convertView==null)

        {



            convertView = lf.inflate(R.layout.book_app_list, parent, false);

            holder.timing1 = (TextView)convertView.findViewById(R.id.timing);
            holder.apt1 = (TextView)convertView.findViewById(R.id.apt);
            holder.status23 = (Button)convertView.findViewById(R.id.status);

            convertView.setTag(holder);


        }
        else
        {

            holder =(ViewHolder)convertView.getTag();


        }




        obj=all_feed_list.get(position);

        holder.timing1.setText(obj.getSlotstart()+"-"+obj.getSlotend());
        holder.apt1.setText(obj.getCounter());
        holder.status23.setText(obj.getStatus());


        holder.status23.setClickable(state[position]);

        holder.status23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((Button)v).isClickable()) {
                    state[position] = true;
                    holder.status23.setText("Booked");
                }

            }
        });




        return convertView;
    }

    protected class ViewHolder
    {


        TextView timing1;
        TextView apt1;
        Button status23;

    }




}



