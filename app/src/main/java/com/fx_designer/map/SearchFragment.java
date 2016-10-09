package com.fx_designer.map;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.fx_designer.map.database.DatabaseController;
import com.fx_designer.map.models.Position;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener,OnPositionsFound {


    private OnFragmentInteractionListener mListener;
    private EditText serchTextField;
    private Button searchButton;
    private ListView listView;
    private Button showAllOnMap;
    private Spinner spinner;
    private ArrayList<Position> positionsList;
    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_search, container, false);
        searchButton= (Button) v.findViewById(R.id.searchButton);
        serchTextField= (EditText) v.findViewById(R.id.editText4);
        showAllOnMap= (Button) v.findViewById(R.id.showAllOnMap);
        showAllOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowAllOnMap();
            }
        });
        showAllOnMap.setVisibility(View.INVISIBLE);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setSelection(0);
                onSearch();
            }
        });
        listView= (ListView) v.findViewById(R.id.listView);
        spinner= (Spinner) v.findViewById(R.id.spinner);
        String array[]={"Select Category","Food","Hotel","Sights","WiFi","Transport","Gas","Parking","Shop","ATM"};

        Integer intArray[]={1,2};
//        CategoryAdapter s=new CategoryAdapter(getContext(),new ArrayList<String>(Arrays.asList(array)),new ArrayList<Integer>(Arrays.asList(intArray)));
        ArrayAdapter<String> s=new ArrayAdapter<String>(getContext(),R.layout.spinner_item,array);
        spinner.setAdapter(s);
        spinner.setOnItemSelectedListener(this);
        return  v;
    }

    //search has done
    private void onSearch(){

            String keyword=serchTextField.getText().toString();
            FetchData f=new FetchData(keyword,this);
            f.execute();
            //search(keyword);

    }

    public void search(String keywords){
        positionsList= DatabaseController.getInstance(this.getActivity()).readFromDatabase(keywords);

        if(positionsList.size()!=0){
            showAllOnMap.setVisibility(View.VISIBLE);
            View view = this.getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        else{
            showAllOnMap.setVisibility(View.INVISIBLE);
            Toast.makeText(this.getContext(),"Nothing Found",Toast.LENGTH_LONG).show();
        }
        ItemsAdapter itemsAdapter=new ItemsAdapter(getActivity(),positionsList);
        listView.setAdapter(itemsAdapter);
    }

    private void onShowAllOnMap(){
        if(mListener!=null && positionsList!=null){
            mListener.onSearchSubmit(positionsList);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        ((MapsActivity)getActivity()).setToolBarVisible();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String keyword=spinner.getSelectedItem().toString().toLowerCase();
        if(keyword.equalsIgnoreCase("Select Category"))return;
        FetchData f=new FetchData(keyword,this);
        f.execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void addPositions(ArrayList<Position> positionsArrayList) {
        positionsList= positionsArrayList;

        if(positionsList.size()!=0){
            showAllOnMap.setVisibility(View.VISIBLE);
            View view = this.getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        else{
            showAllOnMap.setVisibility(View.INVISIBLE);
            Toast.makeText(this.getContext(),"Nothing Found",Toast.LENGTH_LONG).show();
        }
        ItemsAdapter itemsAdapter=new ItemsAdapter(getActivity(),positionsList);
        listView.setAdapter(itemsAdapter);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSearchSubmit(ArrayList<Position> positions);
    }


}


class FetchData extends AsyncTask<Void,Void,String>{
    String key="";
    OnPositionsFound f;
    FetchData(String key,OnPositionsFound f){
        this.key=key;
        this.f=f;
    }
    @Override
    protected String doInBackground(Void... voids) {


        Log.e("Download","I am downloading data now");
        String urlStr="http://45.55.205.150/RestServer/readlocation.php?key="+key;
        URL url;
        String output="";
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urlStr);

            urlConnection = (HttpURLConnection) url
                    .openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                output+=current;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }


        return output;
    }


    @Override
    protected void onPostExecute(String s) {

        if(s!=null){
            try {
                ArrayList<Position> arrayList=new ArrayList<>();
                JSONArray ar=new JSONArray(s);
                for(int i=0;i<ar.length();i++){
                    JSONObject j=ar.getJSONObject(i);
                    Log.e("Data",j.getString("name"));

                    String name=j.getString("name");
                    String category=j.getString("category");
                    String phone=j.getString("phone");
                    String address=j.getString("address");
                    double longitude=j.getDouble("long");
                    double lat=j.getDouble("lat");
                    Position p=new Position(lat,longitude,name,address,phone,category);
                    arrayList.add(p);
                }
                f.addPositions(arrayList);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        super.onPostExecute(s);
    }
}

interface OnPositionsFound{
    public void addPositions(ArrayList<Position> positionsList);
}