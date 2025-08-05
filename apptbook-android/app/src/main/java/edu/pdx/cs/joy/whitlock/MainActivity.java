package edu.pdx.cs.joy.whitlock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int GET_SUM = 42;
    static final String CURRENT_TIME = "CURRENT TIME";

    private ArrayAdapter<Integer> sums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.sums = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        ListView sumsView = findViewById(R.id.sums);
        sumsView.setAdapter(this.sums);

        try {
            readSumsFromFile();
        } catch (IOException e) {
            Toast.makeText(this, "While reading sums file " + e, Toast.LENGTH_LONG).show();
        }
    }

    public void showCalculator(View view) {
        Intent intent = new Intent(this, CalculatorActivity.class);
        intent.putExtra(CURRENT_TIME, new Date());
        startActivityForResult(intent, GET_SUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_SUM) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    int sum = data.getIntExtra(CalculatorActivity.SUM_VALUE, 0);
                    this.sums.add(sum);
                    try {
                        writeSumsToFile();
                    } catch (IOException e) {
                        Toast.makeText(this, "While writing sums file " + e, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private void writeSumsToFile() throws IOException {
        File sumsFile = getSumsFile();
        try (PrintWriter pw = new PrintWriter(new FileWriter(sumsFile))) {
            for (int i = 0; i < this.sums.getCount(); i++) {
                int sum = this.sums.getItem(i);
                pw.println(sum);
            }
        }
    }

    @NonNull
    private File getSumsFile() {
        File dataDir = this.getDataDir();
        return new File(dataDir, "sums.txt");
    }

    private void readSumsFromFile() throws IOException {
        File sumsFile = getSumsFile();
        if (!sumsFile.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(sumsFile))) {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                int sum = Integer.parseInt(line);
                this.sums.add(sum);
            }
        }
    }


}