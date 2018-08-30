package com.multi.hokim.dreamproject.tagCloud;

import android.widget.RelativeLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multi.hokim.dreamproject.TagCloudActivity;
import com.multi.hokim.dreamproject.dbManager.DBOpenHelper;
import com.multi.hokim.dreamproject.dbManager.DiaryDBCtrct;
import com.multi.hokim.dreamproject.dbManager.HashtagDBCtrct;
import com.multi.hokim.dreamproject.dbManager.TagDiaryRelationsDBCtrct;
import com.multi.hokim.dreamproject.diaryList.DiaryVO;
import com.multi.hokim.dreamproject.diaryList.MyAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class TagCloudView extends RelativeLayout{
    //TODO 해시태그에 맞는 리스트를 찾는 쿼리문
    public static final String SQL_FINDLIST =
            "SELECT " + DiaryDBCtrct.TBL_DIARY + "." + DiaryDBCtrct.COL_DATE + " , " + DiaryDBCtrct.TBL_DIARY + "." + DiaryDBCtrct.COL_BODY +
                    " FROM " + TagDiaryRelationsDBCtrct.TBL_RELATIONS +
                    " INNER JOIN " + HashtagDBCtrct.TBL_HASHTAG +
                    " ON " + HashtagDBCtrct.TBL_HASHTAG + "." + HashtagDBCtrct.COL_ID + " = " + TagDiaryRelationsDBCtrct.TBL_RELATIONS + "." + TagDiaryRelationsDBCtrct.COL_HASHID +
                    " INNER JOIN " + DiaryDBCtrct.TBL_DIARY +
                    " ON " + DiaryDBCtrct.TBL_DIARY + "." + DiaryDBCtrct.COL_ID + " = " + TagDiaryRelationsDBCtrct.TBL_RELATIONS + "." + TagDiaryRelationsDBCtrct.COL_DIARYID +
                    " WHERE " + HashtagDBCtrct.TBL_HASHTAG + "." + HashtagDBCtrct.COL_NAME + " = ";
    ;

    //TODO 메인 액티비티꺼 사용하기 위한 변수들
//    RelativeLayout navigation_bar;
//    TextView mTextView1; 원래있던건데 안쓰더라
    MyAdapter adapter;
    DBOpenHelper dbOpenHelper;
    SQLiteDatabase hb;
    TagCloudActivity m;

    public TagCloudView(Context mContext, int width, int height, List<Tag> tagList) {
        this(mContext, width, height, tagList, 6, 34, 1); //default for min/max text size
    }

    public TagCloudView(Context mContext, int width, int height, List<Tag> tagList,
                        int textSizeMin, int textSizeMax, int scrollSpeed) {

        super(mContext);
        this.mContext = mContext;
        this.textSizeMin = textSizeMin;
        this.textSizeMax = textSizeMax;
        tspeed = scrollSpeed;

        m = (TagCloudActivity) mContext;
        dbOpenHelper = new DBOpenHelper(m);
        hb = dbOpenHelper.getReadableDatabase();
        adapter = m.getAdapter();

        //set the center of the sphere on center of our screen:
        centerX = width / 2;
        centerY = height / 4; //TODO: 여기서 위치랑 동그라미 크기 조절
        radius = Math.min(centerX * 0.6f, centerY * 0.6f); //use 95% of screen
        //since we set tag margins from left of screen, we shift the whole tags to left so that
        //it looks more realistic and symmetric relative to center of screen in X direction
        shiftLeft = (int) (Math.min(centerX * 0.15f, centerY * 0.15f));

        // initialize the TagCloud from a list of tags
        //Filter() func. screens tagList and ignores Tags with same text (Case Insensitive)
        mTagCloud = new TagCloud(Filter(tagList), (int) radius,
                textSizeMin,
                textSizeMax
        );
        float[] tempColor1 = {0.8431f, 0.8196f, 1.f, 1}; //rgb Alpha //TODO 택스트 컬러 선택
        //{1f,0f,0f,1}  red       {0.3882f,0.21568f,0.0f,1} orange
        //{0.9412f,0.7686f,0.2f,1} light orange
        float[] tempColor2 = {1.f, 0.8196f, 0.8745f, 1}; //rgb Alpha
        //{0f,0f,1f,1}  blue      {0.1294f,0.1294f,0.1294f,1} grey
        //{0.9412f,0.7686f,0.2f,1} light orange
        mTagCloud.setTagColor1(tempColor1);//higher color
        mTagCloud.setTagColor2(tempColor2);//lower color
        mTagCloud.setRadius((int) radius);
        mTagCloud.create(true); // to put each Tag at its correct initial location


        //update the transparency/scale of tags
        mTagCloud.setAngleX(mAngleX);
        mTagCloud.setAngleY(mAngleY);
        mTagCloud.update();

        mTextView = new ArrayList<TextView>();
        mParams = new ArrayList<LayoutParams>();
        //Now Draw the 3D objects: for all the tags in the TagCloud
        Iterator it = mTagCloud.iterator();
        Tag tempTag;
        int i = 0;

        while (it.hasNext()) {
            tempTag = (Tag) it.next();
            tempTag.setParamNo(i); //store the parameter No. related to this tag

            mTextView.add(new TextView(this.mContext));
            mTextView.get(i).setText(tempTag.getText());

            mParams.add(new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT
                    )
            );
            mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_TOP);
            mParams.get(i).setMargins(
                    (int) (centerX - shiftLeft + tempTag.getLoc2DX()),
                    (int) (centerY + tempTag.getLoc2DY()),
                    0,
                    0);
            mTextView.get(i).setLayoutParams(mParams.get(i));

            mTextView.get(i).setSingleLine(true);
            int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
                    (int) (tempTag.getColorR() * 255),
                    (int) (tempTag.getColorG() * 255),
                    (int) (tempTag.getColorB() * 255));
            mTextView.get(i).setTextColor(mergedColor);
            mTextView.get(i).setTextSize((int) (tempTag.getTextSize() * tempTag.getScale()));
            addView(mTextView.get(i));
            mTextView.get(i).setOnClickListener(OnTagClickListener(tempTag.getText()));
            i++;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void addTag(Tag newTag) {
        mTagCloud.add(newTag);

        int i = mTextView.size();
        newTag.setParamNo(i);

        mTextView.add(new TextView(this.mContext));
        mTextView.get(i).setText(newTag.getText());

        mParams.add(new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                )
        );
        mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mParams.get(i).setMargins(
                (int) (centerX - shiftLeft + newTag.getLoc2DX()),
                (int) (centerY + newTag.getLoc2DY()),
                0,
                0);
        mTextView.get(i).setLayoutParams(mParams.get(i));

        mTextView.get(i).setSingleLine(true);
        int mergedColor = Color.argb((int) (newTag.getAlpha() * 255),
                (int) (newTag.getColorR() * 255),
                (int) (newTag.getColorG() * 255),
                (int) (newTag.getColorB() * 255));
        mTextView.get(i).setTextColor(mergedColor);
        mTextView.get(i).setTextSize((int) (newTag.getTextSize() * newTag.getScale()));
        addView(mTextView.get(i));
        mTextView.get(i).setOnClickListener(OnTagClickListener(newTag.getText()));
    }

    public boolean Replace(Tag newTag, String oldTagText) {
        boolean result = false;
        int j = mTagCloud.Replace(newTag, oldTagText);
        if (j >= 0) { //then oldTagText was found and replaced with newTag data
            Iterator it = mTagCloud.iterator();
            Tag tempTag;
            while (it.hasNext()) {
                tempTag = (Tag) it.next();
                mParams.get(tempTag.getParamNo()).setMargins(
                        (int) (centerX - shiftLeft + tempTag.getLoc2DX()),
                        (int) (centerY + tempTag.getLoc2DY()),
                        0,
                        0);
                mTextView.get(tempTag.getParamNo()).setText(tempTag.getText());
                mTextView.get(tempTag.getParamNo()).setTextSize(
                        (int) (tempTag.getTextSize() * tempTag.getScale()));
                int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
                        (int) (tempTag.getColorR() * 255),
                        (int) (tempTag.getColorG() * 255),
                        (int) (tempTag.getColorB() * 255));
                mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
                mTextView.get(tempTag.getParamNo()).bringToFront();
            }
            result = true;
        }
        return result;
    }

    public void reset() {
        mTagCloud.reset();

        Iterator it = mTagCloud.iterator();
        Tag tempTag;
        while (it.hasNext()) {
            tempTag = (Tag) it.next();
            mParams.get(tempTag.getParamNo()).setMargins(
                    (int) (centerX - shiftLeft + tempTag.getLoc2DX()),
                    (int) (centerY + tempTag.getLoc2DY()),
                    0,
                    0);
            mTextView.get(tempTag.getParamNo()).setTextSize((int) (tempTag.getTextSize() * tempTag.getScale()));
            int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
                    (int) (tempTag.getColorR() * 255),
                    (int) (tempTag.getColorG() * 255),
                    (int) (tempTag.getColorB() * 255));
            mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
            mTextView.get(tempTag.getParamNo()).bringToFront();
        }
    }

    @Override
    public boolean onTrackballEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        mAngleX = (y) * tspeed * TRACKBALL_SCALE_FACTOR;
        mAngleY = (-x) * tspeed * TRACKBALL_SCALE_FACTOR;

        mTagCloud.setAngleX(mAngleX);
        mTagCloud.setAngleY(mAngleY);
        mTagCloud.update();

        Iterator it = mTagCloud.iterator();
        Tag tempTag;
        while (it.hasNext()) {
            tempTag = (Tag) it.next();
            mParams.get(tempTag.getParamNo()).setMargins(
                    (int) (centerX - shiftLeft + tempTag.getLoc2DX()),
                    (int) (centerY + tempTag.getLoc2DY()),
                    0,
                    0);
            mTextView.get(tempTag.getParamNo()).setTextSize((int) (tempTag.getTextSize() * tempTag.getScale()));
            int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
                    (int) (tempTag.getColorR() * 255),
                    (int) (tempTag.getColorG() * 255),
                    (int) (tempTag.getColorB() * 255));
            mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
            mTextView.get(tempTag.getParamNo()).bringToFront();
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //rotate elements depending on how far the selection point is from center of cloud
                float dx = x - centerX;
                float dy = y - centerY;
                mAngleX = (dy / radius) * tspeed * TOUCH_SCALE_FACTOR;
                mAngleY = (-dx / radius) * tspeed * TOUCH_SCALE_FACTOR;

                mTagCloud.setAngleX(mAngleX);
                mTagCloud.setAngleY(mAngleY);
                mTagCloud.update();

                Iterator it = mTagCloud.iterator();
                Tag tempTag;
                while (it.hasNext()) {
                    tempTag = (Tag) it.next();
                    mParams.get(tempTag.getParamNo()).setMargins(
                            (int) (centerX - shiftLeft + tempTag.getLoc2DX()),
                            (int) (centerY + tempTag.getLoc2DY()),
                            0,
                            0);
                    mTextView.get(tempTag.getParamNo()).setTextSize((int) (tempTag.getTextSize() * tempTag.getScale()));
                    int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
                            (int) (tempTag.getColorR() * 255),
                            (int) (tempTag.getColorG() * 255),
                            (int) (tempTag.getColorB() * 255));
                    mTextView.get(tempTag.getParamNo()).setTextColor(mergedColor);
                    mTextView.get(tempTag.getParamNo()).bringToFront();
                }

                break;
		/*case MotionEvent.ACTION_UP:  //now it is clicked!!!!
			dx = x - centerX;
			dy = y - centerY;
			break;*/
        }

        return true;
    }

    //the filter function makes sure that there all elements are having unique Text field:
    List<Tag> Filter(List<Tag> tagList) {
        //current implementation is O(n^2) but since the number of tags are not that many,
        //it is acceptable.
        List<Tag> tempTagList = new ArrayList();
        Iterator itr = tagList.iterator();
        Iterator itrInternal;
        Tag tempTag1, tempTag2;
        //for all elements of TagList
        while (itr.hasNext()) {
            tempTag1 = (Tag) (itr.next());
            boolean found = false;
            itrInternal = tempTagList.iterator();
            while (itrInternal.hasNext()) {
                tempTag2 = (Tag) (itrInternal.next());
                if (tempTag2.getText().equalsIgnoreCase(tempTag1.getText())) {
                    found = true;
                    break;
                }
            }
            if (found == false)
                tempTagList.add(tempTag1);
        }
        return tempTagList;
    }

    //for handling the click on the tags
    //onclick open the tag url in a new window. Back button will bring you back to TagCloud
    OnClickListener OnTagClickListener(final String hash) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 해시태그 눌렸을 때 리스트뷰를 띄움
                m.tempDiarys.clear();
                Cursor cursor = hb.rawQuery(SQL_FINDLIST + "'" + hash + "'"+
                        " ORDER BY "+DiaryDBCtrct.TBL_DIARY+"."+DiaryDBCtrct.COL_DATE, null);

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String date = cursor.getString(0);
                        CalendarDay calendarDay=null;
                        try {
                            calendarDay = CalendarDay.from(DiaryVO.DATE_FORMAT.parse(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String body = cursor.getString(1);
                        DiaryVO temp = new DiaryVO(calendarDay, body);
                        m.tempDiarys.add(temp);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        };
    }

    private final float TOUCH_SCALE_FACTOR = .8f;
    private final float TRACKBALL_SCALE_FACTOR = 10;
    private float tspeed;
    private TagCloud mTagCloud;
    private float mAngleX = 0;
    private float mAngleY = 0;
    private float centerX, centerY;
    private float radius;
    private Context mContext;
    private int textSizeMin, textSizeMax;
    private List<TextView> mTextView;
    private List<LayoutParams> mParams;
    private int shiftLeft;
}
