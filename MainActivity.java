package com.knoxpo.customedittext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtn, btnSubmit, btnAmt;
    private SpannableEditText mSpanET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = (Button) findViewById(R.id.btn_name);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnAmt = (Button) findViewById(R.id.btn_amount);
        mSpanET = (SpannableEditText) findViewById(R.id.et_span);
        mBtn.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnAmt.setOnClickListener(this);

        final String s = "hi " + "{name} " + "hdfgdg " + "{amount}";


       /* Matcher m = Pattern.compile("\\{([^}]+)\\}").matcher(s);
        while (m.find()){
            Log.d("pattern :" , m.groupCount() + " " + m.group());
        }*/

        ViewTreeObserver vto = mSpanET.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mSpanET.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mSpanET.setText(s);
                mSpanET.setSpannableText(s);
                mSpanET.setSelection(mSpanET.getText().length());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_name:
                mSpanET.setSpanText("name");
                Log.d("original Length :", mSpanET.getText().length() + " ");
                break;
            case R.id.btn_amount:
                mSpanET.setSpanText("amount");
                break;
            case R.id.btn_submit:
                mSpanET.getSpannableText();
                Log.d("replace Length :", mSpanET.getText().length() + " ");

        }

    }
}
