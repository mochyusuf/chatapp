package tex.com.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chat_room extends AppCompatActivity {

    Button sendBtn;
    TextView receivedMsg;
    EditText sendMsg;

    DatabaseReference rootRoomName;

    String roomName;
    String userName;
    private String chatUserName;
    private String chatMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        sendBtn = (Button) findViewById(R.id.sendMsgBtn);
        receivedMsg = (TextView) findViewById(R.id.receivedMsg);
        sendMsg = (EditText) findViewById(R.id.sendMsgEdit);

        roomName = getIntent().getExtras().get("Room_name").toString();
        userName = getIntent().getExtras().get("User_name").toString();

        setTitle(roomName);

        rootRoomName = FirebaseDatabase.getInstance().getReference().getRoot().child(roomName);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference childRoot = rootRoomName.push();

                Map<String, Object> map = new HashMap<String, Object>();

                map.put("name", userName);
                map.put("message", sendMsg.getText().toString());

                childRoot.updateChildren(map);
            }
        });

        rootRoomName.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                update_Message(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                update_Message(dataSnapshot);
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
        });
    }

    private void update_Message(DataSnapshot dataSnapshot) {
        chatUserName = (String) dataSnapshot.child("name").getValue();
        chatMessage = (String) dataSnapshot.child("message").getValue();
        receivedMsg.append(chatUserName + ":" + chatMessage + "\n\n");
    }

}
