package com.hexaenna.avm.adapter;

/**
 * Created by admin on 10/21/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hexaenna.avm.R;
import com.hexaenna.avm.api.CustomVolleyRequest;

import java.util.ArrayList;

/**
 * Created by Belal on 12/22/2015.
 */
public class GridViewAdapter extends BaseAdapter {

    //Imageloader to load images
    private ImageLoader imageLoader;
    private static LayoutInflater inflater=null;
    //Context
    private Context context;

    //Array List that would contain the urls and the titles for the images
    private ArrayList<String> images;
    private ArrayList<String> names;

    public GridViewAdapter (Context context, ArrayList<String> images,ArrayList<String> name){
        //Getting all the values
        this.context = context;
        this.images = images;
        this.names = name;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Creating a linear layout
       /* LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //NetworkImageView
        NetworkImageView networkImageView = new NetworkImageView(context);

        //Initializing ImageLoader
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(images.get(position), ImageLoader.getImageListener(networkImageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        //Setting the image url to load
        networkImageView.setImageUrl(images.get(position),imageLoader);

        //Creating a textview to show the title
       *//* TextView textView = new TextView(context);
        textView.setText(names.get(position));*//*

        //Scaling the imageview
        networkImageView.setLayoutParams(new GridView.LayoutParams(210,210));
        linearLayout.setBackgroundResource(R.drawable.grid_items_border);
        //Adding views to the layout
        linearLayout.addView(networkImageView);

        //Returnint the layout
        return linearLayout;*/

        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.grid_view_item, null);
        holder.img=(NetworkImageView) rowView.findViewById(R.id.imgNetwork);
        holder.textView = (TextView) rowView.findViewById(R.id.txtname) ;

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(images.get(position), ImageLoader.getImageListener(holder.img, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        //Setting the image url to load
        holder.img.setImageUrl(images.get(position),imageLoader);
        holder.textView.setText(names.get(position));

        return rowView;
    }

    public class Holder
    {

        NetworkImageView img;
        TextView textView;
    }

}