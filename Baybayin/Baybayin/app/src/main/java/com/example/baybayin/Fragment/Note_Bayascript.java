package com.example.baybayin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baybayin.Utils.Add_Notes;
import com.example.baybayin.Utils.MyAdpter;
import com.example.baybayin.Utils.Note;
import com.example.baybayin.databinding.FragmentNoteBinding;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class Note_Bayascript extends Fragment {

    FragmentNoteBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(inflater, container, false);

        Button addNote = binding.addNote;

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), Add_Notes.class);
                startActivity(intent);

            }
        });

        Realm.init(requireContext());
        Realm realm =  Realm.getDefaultInstance();


        RealmResults<Note> noteList = realm.where(Note.class).findAll().sort("createdTime", Sort.DESCENDING);

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        MyAdpter myAdapter = new MyAdpter(requireContext(), noteList);
        recyclerView.setAdapter(myAdapter);


        noteList.addChangeListener(new RealmChangeListener<RealmResults<Note>>() {
            @Override
            public void onChange(RealmResults<Note> notes) {
                myAdapter.notifyDataSetChanged();
            }
        });

        return binding.getRoot();
    }

}