package com.example.emotioncomprehension;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Transfer_pg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_pg);

        Spinner spnFrom = findViewById(R.id.FromAccount);
        Spinner spnTo = findViewById(R.id.ToAccount);
        Button btnContinue = findViewById(R.id.btnContinue);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Account_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFrom.setAdapter(adapter);

        spnFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                Resources res1 = getResources();
                String[] myArray = res1.getStringArray(R.array.Account_types);
                List<String> secondSpinnerItems = new ArrayList<>(Arrays.asList(myArray));
                secondSpinnerItems.remove(selectedItem);
                ArrayAdapter<String> secondAdapter = new ArrayAdapter<>(Transfer_pg.this, android.R.layout.simple_spinner_dropdown_item, secondSpinnerItems);
                spnTo.setAdapter(secondAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the verification activity
                Intent intent = new Intent(Transfer_pg.this, verification.class);
                startActivity(intent);
            }
        });
    }
}
