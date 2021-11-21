package com.techmania.notetakewithtwotable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.techmania.notetakewithtwotable.Adapters.CompletedNotesAdapter;
import com.techmania.notetakewithtwotable.RoomDatabase.Entities.CompletedNotes;
import com.techmania.notetakewithtwotable.RoomDatabase.ViewModel.NoteViewModel;

import java.util.List;

public class CompletedNotesActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_notes);

        RecyclerView recyclerView = findViewById(R.id.rvCompleted);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CompletedNotesAdapter adapter = new CompletedNotesAdapter(getApplication());
        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(NoteViewModel.class);
        noteViewModel.getAllCompletedNotes().observe(this, new Observer<List<CompletedNotes>>() {
            @Override
            public void onChanged(List<CompletedNotes> notes) {

                //update Recycler View
                adapter.setNotes(notes);

            }
        });

    }
}