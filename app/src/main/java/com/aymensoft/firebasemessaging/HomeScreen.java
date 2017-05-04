package com.aymensoft.firebasemessaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.aymensoft.firebasemessaging.adapters.MessageAdapter;
import com.aymensoft.firebasemessaging.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    public static final String TAG = "HomeScreen";

    ListView lvMessages;
    EditText etMessage;
    Button btnSend;

    private MessageAdapter adapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private ChildEventListener mChildEventListener;
    public String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        username= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mMessageDatabaseReference=mFirebaseDatabase.getReference().child("messages");

        lvMessages=(ListView)findViewById(R.id.lv_messages);
        etMessage=(EditText)findViewById(R.id.et_message);
        btnSend=(Button)findViewById(R.id.btn_send);

        // Initialize message ListView and its adapter
        List<Message> messages = new ArrayList<>();
        adapter = new MessageAdapter(this, R.layout.listview_messages, messages);
        lvMessages.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message=new Message(etMessage.getText().toString(),username,null);
                mMessageDatabaseReference.push().setValue(message);
                etMessage.setText("");
            }
        });

        mChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message=dataSnapshot.getValue(Message.class);
                adapter.add(message);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mMessageDatabaseReference.addChildEventListener(mChildEventListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_homescreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeScreen.this, Singing.class));
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
