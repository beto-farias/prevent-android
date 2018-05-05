package mx.com.dgom.sercco.android.prevent.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import mx.com.dgom.sercco.android.prevent.Constantes;
import mx.com.dgom.sercco.android.prevent.act.HomeActivity;
import mx.com.dgom.sercco.android.prevent.act.R;
import mx.com.dgom.sercco.android.prevent.to.LeftDrawerItem;

/**
 * Created by beto on 10/26/15.
 */
public class LeftDrawerRecicleViewAdapter extends RecyclerView.Adapter<LeftDrawerRecicleViewAdapter.ViewHolder> {

    private static final String TAG = "LeftDrawer";

    HomeActivity ctx;

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private List<LeftDrawerItem> data = new ArrayList<LeftDrawerItem>();

    private String name;        //String Resource for header View Name
    private int profile;        //int Resource for header view profile picture
    private Bitmap imgProfile;


    public LeftDrawerRecicleViewAdapter(HomeActivity ctx, String name, int profile) {
        this.name = name;
        this.profile = profile;
        this.ctx = ctx;
    }


    public LeftDrawerRecicleViewAdapter(HomeActivity ctx, String name, Bitmap profile) {
        this.name = name;
        //this.profile = profile;
        this.ctx = ctx;
        this.imgProfile = profile;
    }

    public void addItem(LeftDrawerItem item) {
        data.add(item);
        this.notifyDataSetChanged();
    }

    public void addItem(List<LeftDrawerItem> items) {
        data.addAll(items);
        this.notifyDataSetChanged();
    }

    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public LeftDrawerRecicleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_drawer_item_text, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_drawer_header, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;

    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(LeftDrawerRecicleViewAdapter.ViewHolder viewHolder, int position) {

        //Listado
        if (viewHolder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image

            LeftDrawerItem item = data.get(position - 1);
            viewHolder.item = item;

            viewHolder.txtItemTitle.setText(item.getTitle()); // Setting the Text with the array of our Titles
            //holder.imageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons
            if(item.isSelected()){
                viewHolder.imgCheckDelito.setImageResource(R.drawable.ico_check_on);
            }else{
                viewHolder.imgCheckDelito.setImageResource(R.drawable.ico_check_off);
            }


            if (!item.isHasSelectableItem()) {
                viewHolder.imgCheckDelito.setVisibility(View.GONE);
                viewHolder.imgIcon.setVisibility(View.GONE);
            }else{
                viewHolder.imgCheckDelito.setVisibility(View.VISIBLE);
                viewHolder.imgIcon.setVisibility(View.VISIBLE);
                viewHolder.imgIcon.setImageBitmap(Constantes.getIcoBadgeByDelitoSm(item.getIdDelito()));

                //Log.d(TAG, item.getTitle());
                //Log.d(TAG, "id Delito: " + item.getIdDelito());

            }

        } else {//Encabezado
            if(imgProfile == null) {
                viewHolder.profile.setImageResource(profile);           // Similarly we set the resources for header view
            }else{
                viewHolder.profile.setImageBitmap(imgProfile);
            }
            viewHolder.Name.setText(URLDecoder.decode(name));
        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return data.size() + 1; // the number of items in the list will be +1 the titles including the header view.
    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;

        LeftDrawerItem item;

        TextView txtItemTitle;
        ImageView imgCheckDelito;
        public ImageView imgIcon;

        ImageView profile;
        TextView Name;

        // Creating ViewHolder Constructor with View and viewType As a parameter
        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            //this.item = itemView;


            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if (ViewType == TYPE_ITEM) {
                Holderid        = 1;                                                        // setting holder id as 1 as the object being populated are of type item row
                txtItemTitle    = (TextView) itemView.findViewById(R.id.txt_item_title);    // Creating TextView object with the id of textView from item_row.xml
                imgCheckDelito  = (ImageView) itemView.findViewById(R.id.img_check_delito); // Creating ImageView object with the id of ImageView from item_row.xml
                imgIcon         = (ImageView) itemView.findViewById(R.id.img_icon);
                itemView.setOnClickListener(this);

            } else {
                Name        = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                profile     = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid    = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }


        @Override
        public void onClick(View view) {
            ctx.onLeftDrawerClick(item);
        }

    }
}
