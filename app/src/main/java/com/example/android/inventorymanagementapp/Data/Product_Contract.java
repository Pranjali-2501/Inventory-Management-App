package com.example.android.inventorymanagementapp.Data;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

public final class Product_Contract {
    private Product_Contract(){}

    public static final String ContentAuthority = "com.example.android.inventorymanagementapp";
    public static final Uri Base_Uri = Uri.parse("content://"+ContentAuthority);
    public static final String path_product = "items";

    public static final class ProductEntry implements BaseColumns{
        public static final String TABLE_NAME = "items";
        public  static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COMPANY = "company";
        public static final String COLUMN_SNAME = "sname";
        public static final String COLUMN_SMAIL = "mail";
        public static final String COLUMN_SNUMBER = "number";
        public static final String COLUMN_CODE = "barcode";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_AMOUNT = "amount";

        public static final Uri ContentUri = Uri.withAppendedPath(Base_Uri , path_product);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ContentAuthority + "/" + path_product;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ContentAuthority + "/" + path_product;
    }
}
