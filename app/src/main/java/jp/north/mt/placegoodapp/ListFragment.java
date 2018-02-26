package jp.north.mt.placegoodapp;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.tasks.Task;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class ListFragment extends Fragment {

    private Realm mRealm;
    private RealmChangeListener mRealmListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element) {
            reloadListView();
        }
    };

    private ListView mListView;
    private ListAdapter mTaskAdapter;

//    //Realmの設定により追加↓↓
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_main);
//
//        // Realmの設定
//        mRealm = Realm.getDefaultInstance();
//        mRealm.addChangeListener(mRealmListener);
//
//        // ListViewの設定
//        mTaskAdapter = new ListAdapter(getActivity());
//        mListView = (ListView) findViewById(R.id.listView1);
//
//        // ListViewをタップしたときの処理
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // 入力・編集する画面に遷移させる
//            }
//        });
//
//        // ListViewを長押ししたときの処理
//        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                // タスクを削除する
//
//                return true;
//            }
//        });
//
//        // アプリ起動時に表示テスト用のタスクを作成する
//        addTaskForTest();
//
//        reloadListView();
//    }
//    //Realmの設定により追加↑↑

    private void reloadListView() {
        // Realmデータベースから、「全てのデータを取得して新しい日時順に並べた結果」を取得
        RealmResults<Listdata> taskRealmResults = mRealm.where(Listdata.class).findAllSorted("date", Sort.DESCENDING);
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


    //ListFragmentサンプルのまま↓
//    private final static String BACKGROUND_COLOR = "background_color";
//
//    public static ListFragment newInstance(@ColorRes int IdRes) {
//        ListFragment frag = new ListFragment();
//        Bundle b = new Bundle();
//        b.putInt(BACKGROUND_COLOR, IdRes);
//        frag.setArguments(b);
//        return frag;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_main, null);
//        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.fragment_main_linearlayout);
//        linearLayout.setBackgroundResource(getArguments().getInt(BACKGROUND_COLOR));
//
//        return view;
//    }
}