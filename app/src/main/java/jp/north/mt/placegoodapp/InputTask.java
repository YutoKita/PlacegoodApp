package jp.north.mt.placegoodapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class InputTask extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute;
    private EditText mTitleEdit, mPlaceEdit, mCommentEdit;
    private Listdata mListdata;

    //↓決定ボタンにて追加↓
    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addTask();
            finish();
        }
    };
    //↑決定ボタンにて追加↑

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_input);
        //登録画面でツールバーに"Memo"の文字を入力
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Memo");

        //↓Realmの設定で追加↓
        // ActionBarを設定する
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // UI部品の設定
        findViewById(R.id.done_button).setOnClickListener(mOnDoneClickListener);
        mTitleEdit = (EditText) findViewById(R.id.titleText);
        mPlaceEdit = (EditText) findViewById(R.id.placeBodyText);
        mCommentEdit = (EditText) findViewById(R.id.contentBodyText);

        // EXTRA_TASK から Listdata の id を取得して、 id から Listdata のインスタンスを取得する
        Intent intent = getIntent();
        int taskId = intent.getIntExtra(ListFragment.EXTRA_TASK, -1);
        Realm realm = Realm.getDefaultInstance();
        mListdata = realm.where(Listdata.class).equalTo("id", taskId).findFirst();
        realm.close();

        if (mListdata == null) {
            // 新規作成の場合
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);
        } else {
            // 更新の場合
            mTitleEdit.setText(mListdata.getTitle());
            mPlaceEdit.setText(mListdata.getPlace());
            mCommentEdit.setText(mListdata.getContent());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mListdata.getDate());
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);

            String dateString = mYear + "/" + String.format("%02d", (mMonth + 1)) + "/" + String.format("%02d", mDay);
            String timeString = String.format("%02d", mHour) + ":" + String.format("%02d", mMinute);
        }

        double value1 = intent.getDoubleExtra("VALUE1", 0);
        double value2 = intent.getDoubleExtra("VALUE2", 0);

        TextView textView = (TextView) findViewById(R.id.latlngBodyText);
        textView.setText(String.valueOf(value1) + "," + String.valueOf(value2));

    }
    //↑Realmの設定で追加↑

    //↓Realmの設定で追加↓
    private void addTask() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        if (mListdata == null) {
            // 新規作成の場合
            mListdata = new Listdata();

            RealmResults<Listdata> listdataRealmResults = realm.where(Listdata.class).findAll();

            int identifier;
            if (listdataRealmResults.max("id") != null) {
                identifier = listdataRealmResults.max("id").intValue() + 1;
            } else {
                identifier = 0;
            }
            mListdata.setId(identifier);
        }

        String title = mTitleEdit.getText().toString();
        String place = mPlaceEdit.getText().toString();
        String content = mCommentEdit.getText().toString();

        mListdata.setTitle(title);
        mListdata.setPlace(place);
        mListdata.setContent(content);
        GregorianCalendar calendar = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
        Date date = calendar.getTime();
        mListdata.setDate(date);

        realm.copyToRealmOrUpdate(mListdata);
        realm.commitTransaction();

        realm.close();
    }
    //↑Realmの設定で追加↑


}
