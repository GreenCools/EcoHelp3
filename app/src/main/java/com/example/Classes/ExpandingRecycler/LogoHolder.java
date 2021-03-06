package com.example.Classes.ExpandingRecycler;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;

import com.example.ecohelp.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class LogoHolder extends GroupViewHolder {
    private ImageView logoCoupons;
    public LogoHolder(View itemView) {
        super(itemView);
        logoCoupons = itemView.findViewById(R.id.logo);
    }
    public void setLogoCoupons(ExpandableGroup group){
        logoCoupons.setImageResource(Integer.parseInt(group.getTitle()));
    }

}
