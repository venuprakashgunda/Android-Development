package com.example.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNotes extends AppCompatActivity {

    private EditText title, desc;
    private Notes n;
    private String bT = "",bD = "", alterTitle ="", alterDesc ="";
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        setupComps();

        title.setTextIsSelectable(true);
        desc.setMovementMethod(new ScrollingMovementMethod());
        desc.setTextIsSelectable(true);

        Intent data = getIntent();
        if (data.hasExtra("noteData"))
        {
            n = (Notes) data.getSerializableExtra("noteData");
            if (n != null)
            {
                title.setText(n.getNotesTitle());
                desc.setText(n.getNotesBody());
                bT = n.getNotesTitle();
                bD = n.getNotesBody();
            }
        }

        if (data.hasExtra("position"))
        {
            position = data.getIntExtra("position",-1);
        }
        desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void setupComps()
    {
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.svBtn:
                if (title.getText().toString().equals("") && desc.getText().toString().equals(""))
                {
                    Toast.makeText(this,getString(R.string.noNotes),Toast.LENGTH_LONG).show();
                    finish();

                }
                else if (title.getText().toString().equals(""))
                {
                    Toast.makeText(this,getString(R.string.noTitle),Toast.LENGTH_LONG).show();
                    finish();
                }
                else if (desc.getText().toString().equals(""))
                {
                    Toast.makeText(this,getString(R.string.noDesc),Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    if(detectChange())
                        saveClicked();
                    else
                        finish();
                }
                break;
            default:
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean detectChange()
    {
        alterTitle = title.getText().toString();
        alterDesc = desc.getText().toString();

        if(bT.equals("") && bD.equals("") && alterTitle.equals("") && alterDesc.equals(""))
            return false;
        else return !bT.equals(alterTitle) || !bD.equals(alterDesc);
    }

    @Override
    public void onBackPressed()
    {
        if (!title.getText().toString().equals("") && !desc.getText().toString().equals("") && detectChange())
        {
            createAlert();
        }
        else
            super.onBackPressed();

    }

    private void createAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(detectChange())
                    saveClicked();
                else
                    finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        });

        builder.setTitle(getString(R.string.sv_dialog_title));
        builder.setMessage("Save Note '"+ title.getText()+"' ?");
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void saveClicked()
    {
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("E MMM dd',' YYYY hh:mm a ");

        Intent data = new Intent();
        data.putExtra("title", title.getText().toString());
        data.putExtra("desc",desc.getText().toString());
        data.putExtra("date",ft.format(d));
        if(position !=-1)
            data.putExtra("position", position);
        setResult(RESULT_OK,data);
        Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
        finish();
    }
}