package com.prototype.ancla;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.*;


public class MainActivity extends AppCompatActivity {
    private Button button = null;
    private TextView textView = null;
    private EditText editText = null;

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference messageRef = ref.child("messages/WelcomeMessage");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap();
            }
        });

        if(messageRef != null) {
            messageRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getKey().compareTo("WelcomeMessage") == 0) {
                        Log.d("LogAncla","" + dataSnapshot.getValue().toString());
                        String value = dataSnapshot.getValue().toString();
                        editText.setText(value);
                    } else {
                        Log.d("LogAncla", "Invalid key: " + dataSnapshot.getKey());
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Log.d("LogAncla", "messageRef is null");
        }
    }

    public void openMap() {
        String message = editText.getText().toString();
        messageRef.setValue(message);
        editText.setText("");

        //Intent mapIntent = new Intent(this, MapsActivity.class);
        //startActivity(mapIntent);
    }
}
