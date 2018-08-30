package com.multi.hokim.dreamproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.multi.hokim.dreamproject.dbManager.DBOpenHelper;
import com.multi.hokim.dreamproject.dbManager.HashtagDBCtrct;
import com.multi.hokim.dreamproject.dbManager.TagDiaryRelationsDBCtrct;
import com.multi.hokim.dreamproject.diaryList.DiaryVO;
import com.multi.hokim.dreamproject.diaryList.MyAdapter;
import com.multi.hokim.dreamproject.tagCloud.Tag;
import com.multi.hokim.dreamproject.tagCloud.TagCloudView;

import java.util.ArrayList;
import java.util.List;

public class TagCloudActivity extends AppCompatActivity {
    //TODO 해시태그 이름, 개수 를 반환하는 쿼리문
    public static final String SQL_HASHCOUNT =
            "SELECT " + HashtagDBCtrct.TBL_HASHTAG + "." + HashtagDBCtrct.COL_NAME + " , COUNT ( " + TagDiaryRelationsDBCtrct.TBL_RELATIONS + "." + TagDiaryRelationsDBCtrct.COL_HASHID + " ) " +
                    " FROM " + TagDiaryRelationsDBCtrct.TBL_RELATIONS +
                    " INNER JOIN " + HashtagDBCtrct.TBL_HASHTAG +
                    " ON " + HashtagDBCtrct.TBL_HASHTAG + "." + HashtagDBCtrct.COL_ID + " = " + TagDiaryRelationsDBCtrct.TBL_RELATIONS + "." + TagDiaryRelationsDBCtrct.COL_HASHID +
                    " GROUP BY " + HashtagDBCtrct.TBL_HASHTAG + "." + HashtagDBCtrct.COL_NAME;

    //TODO 화면 구성 요소 직접 만드는데 필요한 변수
    LinearLayout layout;
    ListView listView;
    public ArrayList<DiaryVO> tempDiarys;
    MyAdapter adapter;

    //TODO 온크리에이트 함수안 코드 변경
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        listView = new ListView(this);
        tempDiarys = new ArrayList<>();

        adapter = new MyAdapter(
                this,
                R.layout.dlist,
                tempDiarys
        );
        listView.setAdapter(adapter);

        //Step0: to get a full-screen View:
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Step1: get screen resolution:
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        //Step2: create the required TagList:
        //notice: All tags must have unique text field
        //if not, only the first occurrence will be added and the rest will be ignored
        List<Tag> myTagList = createTags();

        //Step3: create our TagCloudview and set it as the content of our MainActivity
        mTagCloudView = new TagCloudView(this, width, height, myTagList, 6, 34, 4); //passing current context

//        mTagCloudView.requestFocus();
//        mTagCloudView.setFocusableInTouchMode(true);

        layout.addView(mTagCloudView);
        layout.addView(listView);

        ViewGroup.LayoutParams params = mTagCloudView.getLayoutParams();
        params.height = 280;
        mTagCloudView.setLayoutParams(params);

        setContentView(layout);
        //layout.setBackgroundColor(Color.rgb(101,47,	116));
        layout.setBackgroundResource(R.drawable.background02);

        //Step4: (Optional) adding a new tag and resetting the whole 3D TagCloud
        //you can also add individual tags later:
        //mTagCloudView.addTag(new Tag("AAA", 5, "http://www.aaa.com"));
        // .... (several other tasg can be added similarly )
        //indivual tags will be placed along with the previous tags without moving
        //old ones around. Thus, after adding many individual tags, the TagCloud
        //might not be evenly distributed anymore. reset() re-positions all the tags:
        //mTagCloudView.reset();

        //Step5: (Optional) Replacing one of the previous tags with a new tag
        //you have to create a newTag and pass it in together
        //with the Text of the existing Tag that you want to replace
        //Tag newTag=new Tag("Illinois", 9, "http://www.illinois.com");
        //in order to replace previous tag with text "Google" with this new one:
        //boolean result=mTagCloudView.Replace(newTag, "google");
        //result will be true if "google" was found and replaced. else result is false
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    //TODO 태그 만드는 함수 코드 변경
    private List<Tag> createTags() {
        //create the list of tags with popularity values and related url
        List<Tag> tempList = new ArrayList<Tag>();

        DBOpenHelper dbOpenHelper = new DBOpenHelper(this);
        SQLiteDatabase hb = dbOpenHelper.getReadableDatabase();

        Cursor cursor = hb.rawQuery(SQL_HASHCOUNT, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                tempList.add(new Tag(cursor.getString(0), Integer.parseInt(cursor.getString(1)), ""));
            }
        }
        return tempList;
    }

    private TagCloudView mTagCloudView;

    //TODO 어뎁터 건내주는 함수를 만듬
    public MyAdapter getAdapter() {
        return adapter;
    }
}
