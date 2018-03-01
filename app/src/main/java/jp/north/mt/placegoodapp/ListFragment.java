package jp.north.mt.placegoodapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class ListFragment extends Fragment {

    //Realmクラスを保持するmRealmを定義(メンバ変数の追加)
    private Realm mRealm;
    //RealmChangeListenerクラスのmRealmListenerはRealmのデータベースに追加や削除など変化があった場合に呼ばれるリスナー追加
    private RealmChangeListener mRealmListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            reloadListView();
        }
    };

    private ListView mListView;
    private ListAdapter mTaskAdapter;

    ListView listView1;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_list, container, false);

    //ExampleFragmentPagerAdapter.javaから呼び出せるように記載
    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    private void reloadListView() {
        // Realmデータベースから、「全てのデータを取得して新しい日時順に並べた結果」を取得
        RealmResults<Listdata> taskRealmResults = mRealm.where(Listdata.class).findAllSorted("mDate", Sort.DESCENDING);
        // 上記の結果を、TaskList としてセットする
        mTaskAdapter.setTaskList(mRealm.copyFromRealm(taskRealmResults));
        // TaskのListView用のアダプタに渡す
        mListView.setAdapter(mTaskAdapter);
        // 表示を更新するために、アダプターにデータが変更されたことを知らせる
        mTaskAdapter.notifyDataSetChanged();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        mRealm.close();
//    }

//    private void addTaskForTest() {
//        Task task = new Task();
//        task.setTitle("作業");
//        task.setContents("プログラムを書いてPUSHする");
//        task.setDate(new Date());
//        task.setId(0);
//        mRealm.beginTransaction();
//        mRealm.copyToRealmOrUpdate(task);
//        mRealm.commitTransaction();
//}
}