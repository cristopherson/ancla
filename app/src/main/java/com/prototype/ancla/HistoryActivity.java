package com.prototype.ancla;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class HistoryActivity extends Activity {
    private FloodHistoryContainer floodHistoryContainer = new FloodHistoryContainer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TableLayout tableLayout = findViewById(R.id.history_table);
        int listSize = 0;
        int rowId = 0;

        floodHistoryContainer = getIntent().getParcelableExtra(FloodHistory.ANCLA_PARCELABLE_HISTORY_ID);
        List<FloodHistory> historyList =floodHistoryContainer.getHistoryList();
        Collections.reverse(historyList);

        listSize = historyList.size();

        TextView[] textArray = new TextView[listSize];
        TableRow[] tr_head = new TableRow[listSize];

        for(FloodHistory floodHistory:historyList) {
            Log.d(MainActivity.LOG_ANCLA_TAG, floodHistory.toString());

            //Create the tablerows
            tr_head[rowId] = new TableRow(this);
            tr_head[rowId].setId(rowId);
            tr_head[rowId].setBackgroundColor(FloodHistory.getColor(floodHistory.getEventType()));
            tr_head[rowId].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // Here create the TextView dynamically
            textArray[rowId] = new TextView(this);
            textArray[rowId].setId(rowId + listSize);
            textArray[rowId].setText(floodHistory.toString());
            textArray[rowId].setTextSize(TypedValue.COMPLEX_UNIT_SP,20 );
            textArray[rowId].setTextColor(Color.BLACK);
            textArray[rowId].setPadding(5, 5, 5, 5);
            tr_head[rowId].addView(textArray[rowId]);

            tableLayout.addView(tr_head[rowId], new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

        }




    }
}
