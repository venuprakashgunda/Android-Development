package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class NoteViewHolder extends RecyclerView.ViewHolder
{
    TextView titleViewHolder, dateViewHolder, DescViewHolder;

    NoteViewHolder(View itemView)
    {
        super(itemView);
        titleViewHolder = itemView.findViewById(R.id.noteTitleVH);
        dateViewHolder = itemView.findViewById(R.id.dateVH);
        DescViewHolder = itemView.findViewById(R.id.noteDescVH);
    }

}
