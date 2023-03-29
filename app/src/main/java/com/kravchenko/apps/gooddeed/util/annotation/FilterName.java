package com.kravchenko.apps.gooddeed.util.annotation;

import androidx.annotation.IntDef;

import com.kravchenko.apps.gooddeed.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        FilterName.CATEGORY,
        FilterName.RADIUS,
        FilterName.PERFORMER_INITIATIVE,
        FilterName.PERIOD_REALIZATION
})
@Retention(RetentionPolicy.SOURCE)
public @interface FilterName {
    int CATEGORY = R.string.category;
    int RADIUS = R.string.radius;
    int PERIOD_REALIZATION = R.string.period_realization;
    int PERFORMER_INITIATIVE = R.string.performer_initiative;
}
