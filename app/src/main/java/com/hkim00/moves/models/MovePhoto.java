package com.hkim00.moves.models;


import android.content.Context;

import com.hkim00.moves.R;

import org.parceler.Parcel;

@Parcel
public class MovePhoto {

    public String maxWidth;
    public String photoInfo;
    public String photoMoveType;

    public MovePhoto(){}

    public String getPhotoURL(Context context) {
        String finalPhotoWidth = "300";

        if (this.maxWidth != null) {
            if (Integer.valueOf(this.maxWidth) < 300) {
                finalPhotoWidth = this.maxWidth;
            }
        }

        String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=" + finalPhotoWidth + "&photoreference=" + photoInfo + "&key=" + context.getString(R.string.api_key);
        return photoUrl;
    }
}
