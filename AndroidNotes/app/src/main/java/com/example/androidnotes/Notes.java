package com.example.androidnotes;

import java.io.Serializable;

class Notes implements Serializable
{
    private String notesTitle,notesText, notesDate;

    public Notes() {
        this.notesTitle = "";
        this.notesText = "";
        this.notesDate = "";
    }

    String getNotesTitle()
    {
        return notesTitle;
    }

    void setNotesTitle(String title)
    {
        this.notesTitle = title;
    }

    String getNotesBody()
    {
        return notesText;
    }

    void setNotesBody(String notesText)
    {
        this.notesText = notesText;
    }

    String getTimeStamp()
    {
        return notesDate;
    }

    void setTimeStamp(String date)
    {
        this.notesDate = date;
    }

}
