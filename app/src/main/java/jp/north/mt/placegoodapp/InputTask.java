package jp.north.mt.placegoodapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class InputTask extends AppCompatActivity implements View.OnClickListener {

    private int mYear, mMonth, mDay, mHour, mMinute;
    private double mValue1, mValue2;
    private EditText mTitleEdit, mPlaceEdit, mCommentEdit;
    private Listdata mListdata;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final int CHOOSER_REQUEST_CODE = 100;
    private Button mDoneButton;
    private Uri mPictureUri;
    private ImageView mImageView;

    //↓決定ボタンにて追加↓
    private View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addTask();
            //[決定]ボタン押下後、List画面に遷移させる
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
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

        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setOnClickListener(this);

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
            mValue1 = intent.getDoubleExtra("VALUE1", 0);
            mValue2 = intent.getDoubleExtra("VALUE2", 0);
        } else {
            // 更新の場合
            mTitleEdit.setText(mListdata.getTitle());
            mPlaceEdit.setText(mListdata.getPlace());
            mCommentEdit.setText(mListdata.getContent());

            double lat = mListdata.getLatitude();
            double lng = mListdata.getLongitude();
            mValue1 = lat;
            mValue2 = lng;

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

        TextView textView = (TextView) findViewById(R.id.latlngBodyText);
        textView.setText(String.valueOf(mValue1) + "," + String.valueOf(mValue2));

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
        mListdata.setLatitude(mValue1);
        mListdata.setLongitude(mValue2);
        GregorianCalendar calendar = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute);
        Date date = calendar.getTime();
        mListdata.setDate(date);

        realm.copyToRealmOrUpdate(mListdata);
        realm.commitTransaction();

        realm.close();
    }
    //↑Realmの設定で追加↑

    //↓画像保存処理により追加↓
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSER_REQUEST_CODE) {

            if (resultCode != RESULT_OK) {
                if (mPictureUri != null) {
                    getContentResolver().delete(mPictureUri, null, null);
                    mPictureUri = null;
                }
                return;
            }

            // 画像を取得
            Uri uri = (data == null || data.getData() == null) ? mPictureUri : data.getData();

            // URIからBitmapを取得する
            Bitmap image;
            try {
                ContentResolver contentResolver = getContentResolver();
                InputStream inputStream = contentResolver.openInputStream(uri);
                image = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                return;
            }

            // 取得したBimapの長辺を500ピクセルにリサイズする
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            float scale = Math.min((float) 500 / imageWidth, (float) 500 / imageHeight); // (1)

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            Bitmap resizedImage = Bitmap.createBitmap(image, 0, 0, imageWidth, imageHeight, matrix, true);

            // BitmapをImageViewに設定する
            mImageView.setImageBitmap(resizedImage);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mImageView) {
            // パーミッションの許可状態を確認する
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // 許可されている
                    showChooser();
                } else {
                    // 許可されていないので許可ダイアログを表示する
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);

                    return;
                }
            } else {
                showChooser();
            }
        } else if (v == mDoneButton) {
            // キーボードが出てたら閉じる
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            Map<String, String> data = new HashMap<String, String>();

            // Preferenceから名前を取る
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

            // 添付画像を取得する
            BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();

            // 添付画像が設定されていれば画像を取り出してBASE64エンコードする
            if (drawable != null) {
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                String bitmapString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

                data.put("image", bitmapString);
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // ユーザーが許可したとき
                    showChooser();
                }
                return;
            }
        }
    }

    private void showChooser() {
        // ギャラリーから選択するIntent
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);

        // カメラで撮影するIntent
        String filename = System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, filename);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        mPictureUri = getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPictureUri);

        // ギャラリー選択のIntentを与えてcreateChooserメソッドを呼ぶ
        Intent chooserIntent = Intent.createChooser(galleryIntent, "画像を取得");

        // EXTRA_INITIAL_INTENTS にカメラ撮影のIntentを追加
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        startActivityForResult(chooserIntent, CHOOSER_REQUEST_CODE);
    }
    //ボタン押したら保存する処理

    //↑画像保存処理により追加↑
}
