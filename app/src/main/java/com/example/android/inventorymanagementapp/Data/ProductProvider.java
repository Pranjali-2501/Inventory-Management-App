package com.example.android.inventorymanagementapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.example.android.inventorymanagementapp.Data.Product_Contract.ProductEntry.COLUMN_AMOUNT;
import static com.example.android.inventorymanagementapp.Data.Product_Contract.ProductEntry.COLUMN_CODE;
import static com.example.android.inventorymanagementapp.Data.Product_Contract.ProductEntry.COLUMN_NAME;
import static com.example.android.inventorymanagementapp.Data.Product_Contract.ProductEntry.COLUMN_PRICE;

public class ProductProvider extends ContentProvider {

    public static final String LOG_TAG = ProductProvider.class.getName();
    public static final int PRODUCT = 100;
    public static final int PRODUCT_CODE = 101;
    public static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(Product_Contract.ContentAuthority, Product_Contract.path_product, PRODUCT);
        mUriMatcher.addURI(Product_Contract.ContentAuthority, Product_Contract.path_product + "/#", PRODUCT_CODE);
    }

    ProductReaderDbHandler itemDbHandler;

    @Override
    public boolean onCreate() {
        itemDbHandler = new ProductReaderDbHandler(getContext());
        SQLiteDatabase db = itemDbHandler.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = itemDbHandler.getReadableDatabase();
        Cursor cursor;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                Log.e(LOG_TAG,"in provider before projection 11");
                cursor = db.query(Product_Contract.ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                Log.e(LOG_TAG,"in provider after projection 33");
                break;
            case PRODUCT_CODE:
                selection = Product_Contract.ProductEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(Product_Contract.ProductEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown Uri" + uri);
        }
        Log.e(LOG_TAG,"agter al");
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.e(LOG_TAG,"set Notification");
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return Product_Contract.ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_CODE:
                return Product_Contract.ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri + " With match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                Log.e(LOG_TAG,"in insert");
                return insertPet(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for uri " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = itemDbHandler.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        int rows_delete;
        switch (match) {
            case PRODUCT:
                rows_delete = db.delete(Product_Contract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_CODE:
                selection = Product_Contract.ProductEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rows_delete = db.delete(Product_Contract.ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for uri " + uri);
        }
        if (rows_delete != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rows_delete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                return updatePet(uri, values, selection, selectionArgs);
            case PRODUCT_CODE:
                selection = Product_Contract.ProductEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    private Uri insertPet(Uri uri, ContentValues values) {
        String name = values.getAsString(COLUMN_NAME);
        Log.e(LOG_TAG,"In insertProduct");
        if (name == null || name.isEmpty()) {
            Log.e(LOG_TAG, "no name ");
            throw new IllegalArgumentException("Product Requires a Name");
        }

//        Integer barcode = values.getAsInteger(Product_Contract.ProductEntry.COLUMN_CODE);
//        if (barcode == null || barcode <= 0) {
//            Log.e(LOG_TAG, "no code ");
//            throw new IllegalArgumentException("Product Requires a Barcode");
//        }

        Integer price = values.getAsInteger(Product_Contract.ProductEntry.COLUMN_PRICE);
        if (price == null || price <= 0) {
            Log.e(LOG_TAG, "no price ");
            throw new IllegalArgumentException("Product Requires a price");
        }

        Integer amount = values.getAsInteger(Product_Contract.ProductEntry.COLUMN_AMOUNT);
        if (amount == null || amount <= 0) {
            Log.e(LOG_TAG, "no amount ");
            throw new IllegalArgumentException("Product Requires a amount");
        }
        SQLiteDatabase db = itemDbHandler.getWritableDatabase();
        long insert_row = db.insert(Product_Contract.ProductEntry.TABLE_NAME, null, values);
        if (insert_row == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, insert_row);
    }


    private int updatePet(Uri uri, ContentValues values, String selection, String selectionArgs[]) {

        if(values.containsKey(COLUMN_NAME))
        {
            String name  = values.getAsString(COLUMN_NAME);
            if(name.equals(null) || name.isEmpty())
            {
                throw new IllegalArgumentException("Update name should not be null");
            }
        }

        if(values.containsKey(COLUMN_PRICE))
        {
            Integer G  = values.getAsInteger(COLUMN_PRICE);
            if(G.equals(null) || G <= 0)
            {
                throw new IllegalArgumentException("Update code should not be null");
            }
        }
        if(values.containsKey(COLUMN_AMOUNT))
        {
            Integer w  = values.getAsInteger(COLUMN_AMOUNT);
            if(!w.equals(null) && w < 0)
            {
                throw new IllegalArgumentException("Update code should not be null");
            }
        }
//        if(values.containsKey(COLUMN_CODE))
//        {
//            Integer b  = values.getAsInteger(COLUMN_CODE);
//            if(!b.equals(null) && b <= 0)
//            {
//                throw new IllegalArgumentException("Update code should not be null");
//            }
//        }


        if(values.size() == 0)
            return 0;
        SQLiteDatabase db = itemDbHandler.getWritableDatabase();
        int no_of_ros = db.update(Product_Contract.ProductEntry.TABLE_NAME,values,selection,selectionArgs);
        if(no_of_ros != 0)
            getContext().getContentResolver().notifyChange(uri,null);
        return no_of_ros;
    }
}