package com.aymensoft.firebasemessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.aymensoft.firebasemessaging.adapters.MessageAdapter;
import com.aymensoft.firebasemessaging.model.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    public static final String TAG = "HomeScreen";

    private static final int RC_PHOTO_PICKER =  2;

    ListView lvMessages;
    EditText etMessage;
    Button btnSend;
    ImageButton btnpicture;

    private MessageAdapter adapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotoStorageReference;

    //SharedPreferences
    SharedPreferences sharedpreferences;
    public static final String UserPREFERENCES = "UserSession" ;
    public static final String shareduserid = "shareduserid";
    public static String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        //get UserId
        sharedpreferences = getSharedPreferences(UserPREFERENCES, MODE_PRIVATE);
        UserID=sharedpreferences.getString(shareduserid, "0");

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mMessageDatabaseReference=mFirebaseDatabase.getReference().child("messages");
        mFirebaseStorage=FirebaseStorage.getInstance();
        mChatPhotoStorageReference=mFirebaseStorage.getReference().child("chat_photos");

        lvMessages=(ListView)findViewById(R.id.lv_messages);
        etMessage=(EditText)findViewById(R.id.et_message);
        btnSend=(Button)findViewById(R.id.btn_send);
        btnpicture=(ImageButton)findViewById(R.id.btn_image);

        btnpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        // Initialize message ListView and its adapter
        List<Message> messages = new ArrayList<>();
        adapter = new MessageAdapter(this, R.layout.listview_messages, messages);
        lvMessages.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message=new Message(UserID,etMessage.getText().toString(),null);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            UploadTask uploadTask;
            Uri selectedimage = data.getData();
            StorageReference photoRef=
                    mChatPhotoStorageReference.child(selectedimage.getLastPathSegment());
            uploadTask=photoRef.putFile(selectedimage);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    @SuppressWarnings("VisibleForTests")
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Message message=new Message(UserID,null,downloadUrl.toString());
                    mMessageDatabaseReference.push().setValue(message);
                    etMessage.setText("");
                }
            });
        }
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
