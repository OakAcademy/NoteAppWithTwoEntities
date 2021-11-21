package com.techmania.notetakewithtwotable.RoomDatabase.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.techmania.notetakewithtwotable.RoomDatabase.DAOs.CompletedNoteDao;
import com.techmania.notetakewithtwotable.RoomDatabase.DAOs.NoteDao;
import com.techmania.notetakewithtwotable.RoomDatabase.Entities.CompletedNotes;
import com.techmania.notetakewithtwotable.RoomDatabase.Entities.Note;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class, CompletedNotes.class},version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;
    public abstract NoteDao noteDao();
    public abstract CompletedNoteDao completedNoteDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {

            instance = Room.databaseBuilder(context.getApplicationContext()
                    , NoteDatabase.class, "my_note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();

        }

        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            NoteDao noteDao = instance.noteDao();

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    noteDao.insert(new Note("Title 1","Description 1"));
                    noteDao.insert(new Note("Title 2","Description 2"));
                    noteDao.insert(new Note("Title 3","Description 3"));
                    noteDao.insert(new Note("Title 4","Description 4"));
                    noteDao.insert(new Note("Title 5","Description 5"));
                }
            });

        }
    };
}
