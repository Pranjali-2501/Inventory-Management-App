package com.example.android.inventorymanagementapp;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventorymanagementapp.Data.Product_Contract;

public class ProductCursorAdapter extends CursorAdapter {
    public ProductCursorAdapter(Context context , Cursor cursor)
    {
        super(context , cursor,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView p_name = view.findViewById(R.id.list_item_name);
        TextView p_company = view.findViewById(R.id.company_name);

        String name = cursor.getString(cursor.getColumnIndex(Product_Contract.ProductEntry.COLUMN_NAME));
        String company = cursor.getString(cursor.getColumnIndex(Product_Contract.ProductEntry.COLUMN_COMPANY));

        if(TextUtils.isEmpty(company))
        {
            company = "Unknown company";
        }
        p_name.setText(name);
        p_company.setText(company);
    }
}
