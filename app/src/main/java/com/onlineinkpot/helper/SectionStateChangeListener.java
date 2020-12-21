package com.onlineinkpot.helper;


import com.onlineinkpot.models.ModSubject;

/**
 * Created by USER on 8/28/2017.
 */

public interface SectionStateChangeListener {
    void onSectionStateChanged(ModSubject modSubject, boolean isOpen);

}