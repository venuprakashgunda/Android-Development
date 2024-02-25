package com.example.androidnotes;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder>
{

    private final List<Notes> nList;
    private final MainActivity mActivity;

    NotesAdapter(List<Notes> nList, MainActivity mActivity)
    {
        this.nList = nList;
        this.mActivity = mActivity;
    }

    private  String truncateDesc(String desc)
    {
        if (desc.length() > 80)
        {
            String tNotes;
            tNotes = desc.substring(0,80);
            tNotes = tNotes + "...";
            return  tNotes;
        }
        else
            return desc;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_entry,parent,false);

        itemView.setOnClickListener(mActivity);
        itemView.setOnLongClickListener(mActivity);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position)
    {

        Notes n = nList.get(position);
        holder.titleViewHolder.setText(n.getNotesTitle());
        holder.DescViewHolder.setText(truncateDesc(n.getNotesBody()));
        holder.dateViewHolder.setText(n.getTimeStamp());

    }

    @Override
    public int getItemCount()
    {
        return nList.size();
    }
}
