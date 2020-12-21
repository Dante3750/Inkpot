package com.onlineinkpot.helper;


import android.view.View;

import com.onlineinkpot.models.ModUnit;

/**
 * Created by USER on 8/28/2017.
 */

public interface ChildListener {
    void itemClicked(ModUnit unit);

    void onClick(View v);
}
