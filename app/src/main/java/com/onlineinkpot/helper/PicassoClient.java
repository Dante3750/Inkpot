package com.onlineinkpot.helper;

import android.content.Context;
import android.widget.ImageView;


import com.onlineinkpot.R;
import com.squareup.picasso.Picasso;


public class PicassoClient {
    public static void downloadImage(Context c, String url, ImageView img) {

        if (url != null && url.length() > 0) {
            Picasso.with(c).load(url).placeholder(R.drawable.circle_profile_pic_new).into(img);
        } else {
            Picasso.with(c).load(R.drawable.circle_profile_pic_new);
        }

    }
}
