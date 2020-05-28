package com.shreshth.cova.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.shreshth.cova.R;
import com.shreshth.cova.adapter.NoteAdapter;
import com.shreshth.cova.models.Note;

public class CommunityChatActivity extends AppCompatActivity {

    Button sendButton;
    EditText messageEditText;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messageRef = db.collection("messages");
    NoteAdapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_chat);
        setupRecyclerView();
        messageEditText=findViewById(R.id.message_input_edit_text);
        sendButton=findViewById(R.id.button_send);
        Toolbar toolbar=findViewById(R.id.community_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Community");
        toolbar.setBackgroundColor((getResources().getColor(android.R.color.white)));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryText));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=messageEditText.getText().toString();
                message=message.trim();
                if(!message.equals("")) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();
                    String name = user.getDisplayName();
                    long timestamp = System.currentTimeMillis();
                    messageRef.add(new Note(message, name, String.valueOf(timestamp), uid));
                    try {
                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                }
                messageEditText.setText("");
            }
        });

    }

    private void setupRecyclerView() {
        Query query=messageRef.orderBy("timestamp", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Note> options=new FirestoreRecyclerOptions.Builder<Note>().setQuery(query,Note.class).build();
noteAdapter=new NoteAdapter(options);
recyclerView=findViewById(R.id.chat_list_rv);
recyclerView.setHasFixedSize(true);
LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
linearLayoutManager.setStackFromEnd(true);
recyclerView.setLayoutManager(linearLayoutManager);
recyclerView.setAdapter(noteAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
