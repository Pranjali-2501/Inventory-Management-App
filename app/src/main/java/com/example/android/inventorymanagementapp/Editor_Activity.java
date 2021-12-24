package com.example.android.inventorymanagementapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventorymanagementapp.Data.ProductReaderDbHandler;
import com.example.android.inventorymanagementapp.Data.Product_Contract;

import org.w3c.dom.Text;
import com.example.android.inventorymanagementapp.Data.Product_Contract.ProductEntry;

public class Editor_Activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = Editor_Activity.class.getName();

    private boolean detailsHasChanged = false;

    private EditText p_name;

    private EditText p_company;

    private EditText s_name;

    private EditText s_mail;

    private EditText s_number;

    public static TextView barcode_number;

    private EditText p_price;

    private EditText p_amount;

    Button scantBtn;
    private Uri mCurrentProductUri ;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            detailsHasChanged = true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        if(mCurrentProductUri == null)
        {
            setTitle("Add Product");
            invalidateOptionsMenu();
        }
        else
        {
            setTitle("Edit Product Details");
            getSupportLoaderManager().initLoader(0,null,this);
        }

        p_name = findViewById(R.id.item_name);
        p_company = findViewById(R.id.company_name);
        barcode_number = findViewById(R.id.barcode);
        scantBtn = findViewById(R.id.scan_button);
        s_name = findViewById(R.id.s_name);
        s_mail = findViewById(R.id.mail);
        s_number = findViewById(R.id.number);
        p_price = findViewById(R.id.item_price);
        p_amount = findViewById(R.id.item_amount);

        p_name.setOnTouchListener(mTouchListener);
        p_company.setOnTouchListener(mTouchListener);
        scantBtn.setOnTouchListener(mTouchListener);
        s_name.setOnTouchListener(mTouchListener);
        s_mail.setOnTouchListener(mTouchListener);
        s_number.setOnTouchListener(mTouchListener);
        p_price.setOnTouchListener(mTouchListener);
        p_amount.setOnTouchListener(mTouchListener);

        scantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , scannerView.class));
            }
        });

    }

    private void insertPet()
    {
        ProductReaderDbHandler productDbHandler = new ProductReaderDbHandler(this);
        SQLiteDatabase db = productDbHandler.getWritableDatabase();

        String name = p_name.getText().toString().trim();
        String company = p_company.getText().toString().trim();
        String number = barcode_number.getText().toString().trim();
        Log.e(LOG_TAG,number);
        String sname = s_name.getText().toString().trim();
        String smail = s_mail.getText().toString().trim();
        String snumber = s_number.getText().toString().trim();
        String price = p_price.getText().toString().trim();
        String amount = p_amount.getText().toString().trim();

//        if(mCurrentProductUri == null && name == null && company == null && number == null &&
//        sname == null && smail == null && snumber == null && price == null && amount == null)
//        {
//            return;
//        }

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_NAME,name);
        values.put(ProductEntry.COLUMN_COMPANY,company);
        values.put(ProductEntry.COLUMN_SNAME,sname);
        values.put(ProductEntry.COLUMN_SMAIL,smail);
        values.put(ProductEntry.COLUMN_SNUMBER,(snumber));
        //values.put(ProductEntry.COLUMN_CODE,number);
        values.put(ProductEntry.COLUMN_CODE,(number));
        values.put(ProductEntry.COLUMN_PRICE,Integer.parseInt(price));
        values.put(ProductEntry.COLUMN_AMOUNT,Integer.parseInt(amount));

        if(mCurrentProductUri == null)
        {
            Log.e(LOG_TAG, String.valueOf(ProductEntry.ContentUri));
            Uri uri = getContentResolver().insert(ProductEntry.ContentUri,values);
            Log.e(LOG_TAG, String.valueOf(ProductEntry.ContentUri));
            if(uri == null)
                Toast.makeText(this , "Error with saving text" , Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this , "Product saved" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            int no_update = getContentResolver().update(mCurrentProductUri,values,null,null);
            if(no_update == 0)
            {
                Toast.makeText(this,"Error in update",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this,"update complete",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insertPet();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                if (!detailsHasChanged) {
                    NavUtils.navigateUpFromSameTask(Editor_Activity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(Editor_Activity.this);
                            }
                        };


                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);

        if(mCurrentProductUri == null)
        {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!detailsHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String projection[] = {
        ProductEntry.COLUMN_NAME,
        ProductEntry.COLUMN_COMPANY,
        ProductEntry.COLUMN_SNAME,
        ProductEntry.COLUMN_SMAIL,
        ProductEntry.COLUMN_SNUMBER, ProductEntry.COLUMN_CODE,
        ProductEntry.COLUMN_PRICE,
        ProductEntry.COLUMN_AMOUNT};
        return new CursorLoader(this,
                mCurrentProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data == null || data.getCount() < 1)
            return;

        if(data.moveToFirst())
        {
            p_name.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_NAME)));
            p_company.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_COMPANY)));
            s_name.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_SNAME)));
            s_mail.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_SMAIL)));
            s_number.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_SNUMBER)));
            barcode_number.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_CODE)));
            p_price.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_PRICE)));
            p_amount.setText(data.getString(data.getColumnIndex(ProductEntry.COLUMN_AMOUNT)));

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        p_name.setText("");
        p_company.setText("");
        s_name.setText("");
        s_mail.setText("");
        s_number.setText("");
        barcode_number.setText("0");
        p_price.setText("");
        p_amount.setText("0");
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing");
        builder.setPositiveButton("Discard",discardButtonClickListener);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null)
                {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct()
    {
        int no_of_deleted_rows = getContentResolver().delete(mCurrentProductUri,null,null);
        if(no_of_deleted_rows != 0)
        {
            NavUtils.navigateUpFromSameTask(Editor_Activity.this);
            Toast.makeText(this,"Product data deleted successfully",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this , " Error with deletig product" , Toast.LENGTH_SHORT).show();
        }
    }
    private void showDeleteConfirmationDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this pet?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null)
                {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}