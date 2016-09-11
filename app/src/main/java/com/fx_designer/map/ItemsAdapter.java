package com.fx_designer.map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fx_designer.map.models.Position;

import java.util.ArrayList;

/**
 * Created by Chamath Abeysinghe on 9/11/2016.
 * This is the adapter for search results list view
 */
public class ItemsAdapter extends ArrayAdapter<Position> {
    ArrayList<Position> positions;
    public ItemsAdapter(Context context,ArrayList<Position> positions){
        super(context,0,positions);
        this.positions=positions;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.e("Position",""+position);
        Position location=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        TextView name= (TextView) convertView.findViewById(R.id.nameTextView);
        TextView address= (TextView) convertView.findViewById(R.id.addressTextView);
        TextView phone= (TextView) convertView.findViewById(R.id.telephoneTextView);

        name.setText(location.getName());
        address.setText(location.getAddress());
        phone.setText(location.getTelephoneNumber());
        convertView.setTag(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p= (int) view.getTag();
                Position position1=getItem(p);
                itemClick(position1);
            }
        });
        return convertView;
    }

    public void itemClick(Position position){
        ArrayList<Position> p=new ArrayList<Position>();
        p.add(position);
        ((MapsActivity)this.getContext()).onSearchSubmit(p);
    }

}
