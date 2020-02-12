package com.example.android.easyshopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter {
    public ItemAdapter(@NonNull Context context, ArrayList<Item> resource) {
        super(context,0,resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View ListitemView = convertView;
        Item currentItem = (Item) getItem(getCount()-position-1);

        if(ListitemView == null)
        {
            ListitemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.singlecartitem, parent, false);
        }


        TextView priceview = ListitemView.findViewById(R.id.singlecartitemprice);
        TextView nameview = ListitemView.findViewById(R.id.singlecartitemname);
        TextView ratingview = ListitemView.findViewById(R.id.singlecartitemrating);
        ImageView imageView = ListitemView.findViewById(R.id.singlecartitemimage);

        String img = currentItem.getImg();
        String name  = currentItem.getName();
        String rating = currentItem.getRating();
        String price = currentItem.getPrice();


        nameview.setText(name);
        ratingview.setText(rating);
        priceview.setText("₹ "+price);
        Picasso.get().load(img).into(imageView);


        return ListitemView;
    }
}
