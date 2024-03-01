package com.example.baybayin.Utils;

import static com.example.baybayin.Fragment.Home.SHARED_PREF_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baybayin.R;

import org.bson.types.ObjectId;

import java.text.DateFormat;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyAdpter extends RecyclerView.Adapter<MyAdpter.MyViewHolder>{

    Context context;
    RealmResults<Note> noteList;




    public MyAdpter(Context context, RealmResults<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.note_item,parent, false));
    }
    private String limitText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text; // Return the original text if its length is less than or equal to the maxLength
        } else {
            return text.substring(0, maxLength - 3) + " •  •  • "; // Truncate the text and add "..." at the end
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.noteTitle.setText(note.getTitle());
        holder.noteContent.setText(limitText(note.description, 20));


        String formatedTime = DateFormat.getDateTimeInstance().format(note.createdTime);
        holder.timeOutput.setText(formatedTime);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                PopupMenu menu = new PopupMenu(context,view);
                menu.getMenu().add("DELETE");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getTitle().equals("DELETE")){
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            note.deleteFromRealm();
                            realm.commitTransaction();
                            Toast.makeText(context, "Note deleted!", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                menu.show();
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Update_Notes.class);
                ObjectId noteId = note.getNoteId(); // Assuming Note class has getId() method for the id
                String noteIdString = noteId != null ? noteId.toHexString(): null;
                intent.putExtra("noteId", noteIdString);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView noteTitle, noteContent, timeOutput;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            timeOutput = itemView.findViewById(R.id.timeOutput);

        }
    }
}
