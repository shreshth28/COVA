package com.shreshth.cova;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import static android.view.Gravity.END;
import static android.view.Gravity.START;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note,NoteAdapter.NoteHolder>{


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {
        holder.textViewName.setText(model.getName());
        holder.textViewMessage.setText(model.getMessage());
        if(model.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            holder.linearLayout.setGravity(END);
        }
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder{

        TextView textViewMessage;
        TextView textViewName;
        LinearLayout linearLayout;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage=itemView.findViewById(R.id.message_text_view);
            textViewName=itemView.findViewById(R.id.name_text_view);
            linearLayout=itemView.findViewById(R.id.linear_layout);
        }
    }
}
