package com.techmania.notetakewithtwotable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.techmania.notetakewithtwotable.Adapters.NoteAdapter;
import com.techmania.notetakewithtwotable.RoomDatabase.Entities.CompletedNotes;
import com.techmania.notetakewithtwotable.RoomDatabase.Entities.Note;
import com.techmania.notetakewithtwotable.RoomDatabase.ViewModel.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoteAdapter adapter = new NoteAdapter(this);
        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                String completedTitle = adapter.getNotes(viewHolder.getAdapterPosition()).getTitle();
                String completedDesc = adapter.getNotes(viewHolder.getAdapterPosition()).getDescription();

                CompletedNotes completedTasks = new CompletedNotes(completedTitle,completedDesc);
                noteViewModel.insertCompletedNote(completedTasks);

                noteViewModel.delete(adapter.getNotes(viewHolder.getAdapterPosition()));

                Toast.makeText(getApplicationContext(), "Note moved to Completed Tasks", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.new_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.top_menu:
                Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
                startActivityForResult(intent,1);
                return true;
            case R.id.goCompleteds:
                Intent completedIntent = new Intent(MainActivity.this,CompletedNotesActivity.class);
                startActivity(completedIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            String title = data.getStringExtra("noteTitle");
            String description = data.getStringExtra("noteDescription");

            Note note = new Note(title,description);
            noteViewModel.insert(note);

        }
        else if (requestCode == 2 && resultCode == RESULT_OK)
        {

            String title = data.getStringExtra("titleLast");
            String description = data.getStringExtra("descriptionLast");
            int id = data.getIntExtra("noteId",-1);

            Note note = new Note(title,description);
            note.setId(id);
            noteViewModel.update(note);

        }

    }

}