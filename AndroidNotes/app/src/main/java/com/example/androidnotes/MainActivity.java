package com.example.androidnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener
{
    private RecyclerView recycleView;
    private FloatingActionButton newNote;
    private static final int SV_RC = 1, ED_RC = 2;
    private final ArrayList<Notes> notesAL = new ArrayList<>();
    private  NotesAdapter nAdapter;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycleView = findViewById(R.id.recycler);

        nAdapter = new NotesAdapter(notesAL,this);

        recycleView.setAdapter(nAdapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        loadJsonData();
        updateNotesTitle();

    }


    public void updateNotesTitle()
    {
        int tNotes = notesAL.size();
        if (tNotes != 0)
            setTitle(getString(R.string.app_name)+ " ( " + tNotes + " )");
        else
            setTitle(getString(R.string.app_name));
    }

    @Override
    public void onClick(View view)
    {
        int pos = recycleView.getChildLayoutPosition(view);
        Notes note = notesAL.get(pos);
        Intent data = new Intent(this, EditNotes.class);
        data.putExtra("noteData", note);
        data.putExtra("position", pos);
        startActivityForResult(data,ED_RC);
    }

    @Override
    public boolean onLongClick(View view)
    {
        alertDialogueDelete(view);
        return true;
    }

    private void alertDialogueDelete(final View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                int pos = recycleView.getChildLayoutPosition(v);
                notesAL.remove(pos);
                nAdapter.notifyDataSetChanged();
                updateNotesTitle();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

            }
        });

        builder.setTitle("Delete this note?");
        builder.setMessage("This action cannot be undone.");
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void loadJsonData()
    {
        try
        {
            InputStream inputStream = getApplicationContext().openFileInput(getString(R.string.file_name));
            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            jsonReader.beginArray();
            while (jsonReader.hasNext())
            {
                Notes n = new Notes();
                jsonReader.beginObject();
                while (jsonReader.hasNext())
                {
                    String name = jsonReader.nextName();
                    switch (name)
                    {
                        case "title":
                            n.setNotesTitle(jsonReader.nextString());
                            break;
                        case "desc":
                            n.setNotesBody(jsonReader.nextString());
                            break;
                        case "date":
                            n.setTimeStamp(jsonReader.nextString());
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                jsonReader.endObject();
                notesAL.add(n);
            }
            jsonReader.endArray();
        }
        catch (FileNotFoundException ex)
        {
            Toast.makeText(this, "No Data Saved so far.",Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void newNoteClicked(View v)
    {
        Intent intent2 = new Intent(this, EditNotes.class);
        startActivityForResult(intent2,SV_RC);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.opt_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.aboutOption:
                Intent intent1 = new Intent(this,AboutActivity.class);
                startActivity(intent1);
                break;

            case R.id.newNote:
                Intent inten2 = new Intent(this, EditNotes.class);
                startActivityForResult(inten2,SV_RC);
                break;
            default:
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            switch (requestCode) {
                case SV_RC:
                    if (resultCode == RESULT_OK) {
                        Notes temp = new Notes();
                        assert data != null;
                        temp.setNotesTitle(data.getStringExtra("title"));
                        temp.setNotesBody(data.getStringExtra("desc"));
                        temp.setTimeStamp(data.getStringExtra("date"));
                        notesAL.add(0, temp);
                        nAdapter.notifyDataSetChanged();
                    }
                    break;
                case ED_RC:
                    if (resultCode == RESULT_OK) {
                        Notes temp = new Notes();
                        assert data != null;
                        temp.setNotesTitle(data.getStringExtra("title"));
                        temp.setNotesBody(data.getStringExtra("desc"));
                        temp.setTimeStamp(data.getStringExtra("date"));
                        notesAL.remove(data.getIntExtra("position", -1));
                        notesAL.add(0, temp);
                        nAdapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    Log.d(TAG, "onActivityResult: Request Code" + requestCode);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Null Pointer encountered.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume()
    {
        updateNotesTitle();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeJsonData();
        updateNotesTitle();
    }

    public void writeJsonData()
    {
        try
        {
            FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter jWriter = new JsonWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            jWriter.setIndent(" ");
            jWriter.beginArray();
            for (Notes n : notesAL)
            {
                jWriter.beginObject();
                jWriter.name("title").value(n.getNotesTitle());
                jWriter.name("desc").value(n.getNotesBody());
                jWriter.name("date").value(n.getTimeStamp());
                jWriter.endObject();
            }
            jWriter.endArray();
            jWriter.close();
        }
        catch (Exception ex)
        {
            ex.getStackTrace();
        }
    }
}