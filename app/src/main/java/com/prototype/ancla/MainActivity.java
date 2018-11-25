package com.prototype.ancla;

import android.content.Intent;
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
    public static String LOG_ANCLA_TAG = "LogAncla";

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference messageRef = ref.child("messages");
    private FloodEvent floodEvent = new FloodEvent();

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
                    if (floodEvent.parseDataSnapshot(dataSnapshot) == false) {
                        Log.d("LogAncla", "DataSnapshot parsing failed");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Log.d(MainActivity.LOG_ANCLA_TAG, "messageRef is null");
        }
    }

    public void openMap() {
        String message = editText.getText().toString();
        //messageRef.setValue(message);
        //editText.setText("");

        Bundle args = new Bundle();
        args.putString(FloodEvent.DATASNAPSHOT_CHILD_STATUS, floodEvent.getStatus());
        args.putString(FloodEvent.DATASNAPSHOT_CHILD_LATITUDE, floodEvent.getLatitude());
        args.putString(FloodEvent.DATASNAPSHOT_CHILD_LONGITUDE, floodEvent.getLongitude());

        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtras(args);
        startActivity(mapIntent);
    }
}
