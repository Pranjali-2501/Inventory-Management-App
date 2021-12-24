package com.example.android.inventorymanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.inventorymanagementapp.Data.ProductReaderDbHandler;
import com.example.android.inventorymanagementapp.Data.Product_Contract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Manage_Actitvity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ProductReaderDbHandler productDb;
    public static final int PRODUCT_LOADER = 0;
    public  static final String LOG_TAG = Manage_Actitvity.class.getName();
    ProductCursorAdapter mCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage__actitvity);

        //set floating action button for editor actitvity
        FloatingActionButton fb = findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manage_Actitvity.this , Editor_Activity.class);
                startActivity(intent);
            }
        });
        ListView item_list = findViewById(R.id.list_view);

        View  emptyview = findViewById(R.id.empty_view);
        item_list.setEmptyView(emptyview);

        mCursorAdapter = new ProductCursorAdapter(this , null);
        item_list.setAdapter(mCursorAdapter);

        //kick off the loader
        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Manage_Actitvity.this,Editor_Activity.class);
                Uri currentProduct = ContentUris.withAppendedId(Product_Contract.ProductEntry.ContentUri,id);
                intent.setData(currentProduct);
                startActivity(intent);
            }
        });
        getSupportLoaderManager().initLoader(PRODUCT_LOADER,null,this);
    }

    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manage,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_enteries:
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllPets()
    {
        int deleted_products = getContentResolver().delete(Product_Contract.ProductEntry.ContentUri,null,null);

    }
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.e(LOG_TAG,"in creareLoder before projection");
        String projection[] = {
                Product_Contract.ProductEntry.COLUMN_ID,
                Product_Contract.ProductEntry.COLUMN_NAME,
                Product_Contract.ProductEntry.COLUMN_COMPANY,
                Product_Contract.ProductEntry.COLUMN_SNAME,
                Product_Contract.ProductEntry.COLUMN_SMAIL,
                Product_Contract.ProductEntry.COLUMN_SNUMBER,
                Product_Contract.ProductEntry.COLUMN_CODE,
                Product_Contract.ProductEntry.COLUMN_PRICE,
                Product_Contract.ProductEntry.COLUMN_AMOUNT};
        Log.e(LOG_TAG,"in creareLoder after projection");
        return new CursorLoader(getApplication(),
                Product_Contract.ProductEntry.ContentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
//        mCursorAdapter.changeCursor(data);
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}