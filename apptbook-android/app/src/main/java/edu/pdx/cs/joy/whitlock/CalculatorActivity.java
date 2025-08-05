package edu.pdx.cs.joy.whitlock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculatorActivity extends AppCompatActivity {

    static final String SUM_VALUE = "SUM";
    private int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void backToMain(View view) {
        Intent intent = new Intent();
        intent.putExtra(SUM_VALUE, this.sum);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void computeSum(View view) {
        EditText leftOperandView = findViewById(R.id.leftOperand);
        EditText rightOperandView = findViewById(R.id.rightOperand);

        String leftOperandText = leftOperandView.getText().toString();
        String rightOperandText = rightOperandView.getText().toString();

        int leftOperand;
        try {
            leftOperand = Integer.parseInt(leftOperandText);

        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Invalid left operand: " + leftOperandText, Toast.LENGTH_LONG).show();
            return;
        }

        int rightOperand;
        try {
            rightOperand = Integer.parseInt(rightOperandText);

        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Invalid right operand: " + rightOperandText, Toast.LENGTH_LONG).show();
            return;
        }

        this.sum = leftOperand + rightOperand;

        TextView sumView = findViewById(R.id.sum);
        sumView.setText(String.valueOf(sum));
    }
}