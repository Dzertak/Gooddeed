package com.kravchenko.apps.gooddeed.util.annotation;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import com.kravchenko.apps.gooddeed.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        InitiativeType.SINGLE,
        InitiativeType.GROUP,
        InitiativeType.UNLIMITED,
})
@Retention(RetentionPolicy.SOURCE)
public @interface InitiativeType {
    String SINGLE = "SINGLE";//R.string.single;
    String GROUP = "GROUP";//R.string.group;
    String UNLIMITED = "UNLIMITED";//R.string.unlimited;
}
