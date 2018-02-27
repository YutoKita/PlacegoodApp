package jp.north.mt.placegoodapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ListAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater = null;
    private List<Listdata> mTaskList;

    public ListAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTaskList(List<Listdata> taskList) {
        mTaskList = taskList;
    }

    @Override
    public int getCount() {
        return mTaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.fragment_list, parent, false);
        }

        //タイトル
        TextView titleText = (TextView) convertView.findViewById(R.id.titleTextView);
        titleText.setText(mTaskList.get(position).getTitle());

        //現在日時、時間
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE);
        Date date = mTaskList.get(position).getDate();
        TextView dateText = (TextView) convertView.findViewById(R.id.dateTextView);
        dateText.setText(simpleDateFormat.format(date));

        //場所
        TextView placeText = (TextView) convertView.findViewById(R.id.placeTextView);
        placeText.setText(mTaskList.get(position).getPlace());

        //緯度
        TextView latitudeText = (TextView) convertView.findViewById(R.id.latitudeTextView);
        placeText.setText(mTaskList.get(position).getLatitude());

        //経度
        TextView longitudeText = (TextView) convertView.findViewById(R.id.longitudeTextView);
        placeText.setText(mTaskList.get(position).getLongitude());

        //コメント
        TextView commentText = (TextView) convertView.findViewById(R.id.commentTextView);
        placeText.setText(mTaskList.get(position).getComment());

        //写真
        byte[] bytes = mTaskList.get(position).getImageBytes();
        if (bytes.length != 0) {
            Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageView.setImageBitmap(image);
        }

        return convertView;
    }
}