package com.multi.hokim.dreamproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.List;

public class DailyDiaryActivity extends AppCompatActivity implements HashTagHelper.OnHashTagClickListener, View.OnClickListener {
    private static final String TAG = DailyDiaryActivity.class.getSimpleName();

    private HashTagHelper mTextHashTagHelper;
    private HashTagHelper mEditTextHashTagHelper;

    private TextView mEditTextView;
    private TextView mHashTagText;
    private TextView mAllHashTag;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_diary);

        mHashTagText = (TextView)findViewById(R.id.text);
        mAllHashTag = (TextView)findViewById(R.id.all_hashtags);
        mEditTextView = (TextView)findViewById(R.id.edit_text_field);

        Button getEnteredText = (Button)findViewById(R.id.get_entered_text_btn);
        getEnteredText.setOnClickListener(this);
        Button getAllHashTagsBtn = (Button)findViewById(R.id.get_all_hashtags_btn);
        getAllHashTagsBtn.setOnClickListener(this);

        char[] additionalSymbols = new char[] {'_','$'};

        mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorAccent), this, additionalSymbols);
        mTextHashTagHelper.handle(mEditTextView);

    }

    @Override
    public void onHashTagClicked(String hashTag) {
        Log.v(TAG, "onHashTagClicked["+hashTag+"]");
        // TODO: 해몽 액티비티로 넘어가기

        if(mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(DailyDiaryActivity.this, hashTag, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_entered_text_btn:
                mHashTagText.setText(mEditTextView.getText());
                break;
            case R.id.get_all_hashtags_btn:
                List<String> allHashTags = mTextHashTagHelper.getAllHashTags();
                mAllHashTag.setText(allHashTags.toString());
                break;
        }
    }
}
