package com.codepath.chefster.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.chefster.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingInredientView extends LinearLayout {
    @BindView(R.id.text_view_amount) TextView amountTextView;
    @BindView(R.id.text_view_description) TextView descriptionTextView;
    @BindView(R.id.text_view_price) TextView priceTextView;

    public ShoppingInredientView(Context context) {
        super(context);
        init(context);
    }

    public ShoppingInredientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShoppingInredientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.widget_ingredient_row, this);
        ButterKnife.bind(this);
    }

    public void setTextViews(String amount, String description, double price) {
        amountTextView.setText(amount);
        descriptionTextView.setText(description);
        priceTextView.setText("$" + String.valueOf(price));
    }
}
