package com.jju.feng.anti_theft.activities;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jju.feng.anti_theft.R;
import com.jju.feng.anti_theft.db.dao.AntivirusDao;
import com.jju.feng.anti_theft.domain.AntivirusInfo;
import com.jju.feng.anti_theft.domain.AppInfo;
import com.jju.feng.anti_theft.domain.App_Manager_Engine;
import com.jju.feng.anti_theft.utils.ToastUtils;
import com.stericson.RootTools.RootTools;

import java.util.ArrayList;
import java.util.List;

import static android.util.Log.i;

public class AntiVirusActivity extends BaseActivity {
    private static final int ERROR = -1;
    private static final int SUCESS = 0;
    private static final int SCANER_OVER = 1;
    private static final String TAG = "Antivirus_activity";
    private volatile boolean isStop = false;
    private int progress = 0;
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pb_scaner.setMax(scanerAppInfo.size());
            switch (msg.what) {
                case SUCESS:
                    pb_scaner.setProgress(++progress);
                    TextView tv_sucess = new TextView(getApplicationContext());
                    tv_scaner.setText("正在扫描：" + (String) msg.obj);
                    tv_sucess.setText("扫描安全：" + (String) msg.obj);
                    tv_sucess.setTextColor(Color.BLACK);
                    content.addView(tv_sucess, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    sv.scrollBy(0, 100);
                    break;
                case ERROR:
                    pb_scaner.setProgress(++progress);
                    TextView tv_error = new TextView(getApplicationContext());
                    tv_scaner.setText("正在扫描：" + (String) msg.obj);
                    tv_error.setText("发现病毒：" + (String) msg.obj);
                    tv_error.setTextColor(Color.RED);
                    content.addView(tv_error, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    sv.scrollBy(0, 100);
                    break;
                case SCANER_OVER:
                    tv_scaner.setText("完成扫描！");
                    bt_scaner_flag.setText("扫描结束");
                    iv_ic_scaner_paint.clearAnimation();
                    if (antivirus.size() == 0) {
                        iv_scaner_sucess.setVisibility(View.VISIBLE);
                    } else {
                        ll_antivirus.setVisibility(View.VISIBLE);
                        iv_scaner_sucess.setVisibility(View.GONE);
                        sv.setVisibility(View.GONE);
                        adapter = new MyAdapter();
                        lv_ant.setAdapter(adapter);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private ImageView iv_ic_scaner_paint;
    private TextView tv_scaner;
    private ProgressBar pb_scaner;
    private ScrollView sv;
    private LinearLayout content;
    private ListView lv_ant;
    private Button bt_scaner_flag;
    private List<AppInfo> antivirus;
    private ImageView iv_scaner_sucess;
    private List<AppInfo> scanerAppInfo;
    private LinearLayout ll_antivirus;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);
        //扫描需要旋转的图片
        iv_ic_scaner_paint = (ImageView) findViewById(R.id.iv_ic_scaner_paint);
        //扫描信息
        tv_scaner = (TextView) findViewById(R.id.tv_scaner);
        //扫描进度条
        pb_scaner = (ProgressBar) findViewById(R.id.pb_scaner);
        //ScrollView
        sv = (ScrollView) findViewById(R.id.sv);
        //被ScrollView包裹的LinearLayout
        content = (LinearLayout) findViewById(R.id.content);
        //如果扫描出病毒，那么用一个listview来进行操作
        lv_ant = (ListView) findViewById(R.id.lv_ant);
        //一个包含lv_ant的线性布局
        ll_antivirus = (LinearLayout) findViewById(R.id.ll_antivirus);
        //开关按钮
        bt_scaner_flag = (Button) findViewById(R.id.bt_scaner_flag);
        //成功的图片
        iv_scaner_sucess = (ImageView) findViewById(R.id.iv_scaner_sucess);
        //检查手机内是否有病毒数据库
        if (!AntivirusDao.isExist(this)) {
            AntivirusDao.copyFileToFiles(this);
        }
        //设置滚动条长度
        pb_scaner.setProgress(progress);
        //设置动画
        final RotateAnimation animation = new RotateAnimation(
                0,
                360,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        animation.setDuration(4000);
        animation.setRepeatCount(Integer.MAX_VALUE);


        bt_scaner_flag.setText("开始扫描");
        bt_scaner_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("开始扫描".equals(bt_scaner_flag.getText().toString())) {
                    bt_scaner_flag.setText("结束扫描");
                    tv_scaner.setText("正在加载数据...");
                    iv_ic_scaner_paint.startAnimation(animation);
                    isStop = false;
                    Loaddate();

                } else if ("结束扫描".equals(bt_scaner_flag.getText().toString())) {
                    bt_scaner_flag.setText("开始扫描");
                    tv_scaner.setText("扫描中止...");
                    progress = 0;
                    iv_ic_scaner_paint.clearAnimation();
                    isStop = true;


                }
            }
        });
        lv_ant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                final AppInfo itemAtPosition = (AppInfo) lv_ant.getItemAtPosition(position);
                final AppInfo itemAtPosition = (AppInfo) adapter.getItem(position);
//                View inflater = LayoutInflater.from(Antivirus_activity.this).inflate(R.layout.antivirus_item_layout, null);
//                Button bt_uninstall = (Button) inflater.findViewById(R.id.bt_uninstall);
                ViewHolder holder = (ViewHolder) view.getTag();
                i(TAG, "onItemClick" + itemAtPosition.toString());

                i(TAG, "onClick" + "点击卸载");
                //卸载用户应用
                if (itemAtPosition.isUserapp()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:" + itemAtPosition.getPackname()));
                    startActivity(intent);
                } else {
                    //卸载系统应用
                    if (!RootTools.isRootAvailable()) {
                        ToastUtils.showToast(AntiVirusActivity.this,"卸载系统应用，必须要root权限");
                        return;
                    }
                    try {
                        if (!RootTools.isAccessGiven()) {
                            ToastUtils.showToast(AntiVirusActivity.this,"请授予app获取Root权限");

                            return;
                        }
                        ToastUtils.showToast(AntiVirusActivity.this,"无法卸载此系统应用");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


            }
        });

    }

    //写个适配器
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return antivirus.size();
        }

        @Override
        public Object getItem(int position) {
            return antivirus.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder holder;
            if (convertView != null) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                holder = new ViewHolder();
                view = LayoutInflater.from(AntiVirusActivity.this).inflate(R.layout.antivirus_item_layout, null);
                holder.anti_item_iv = (ImageView) view.findViewById(R.id.anti_item_iv);
                holder.anti_name = (TextView) view.findViewById(R.id.anti_name);
                holder.anti_path = (TextView) view.findViewById(R.id.anti_path);
                holder.anti_size = (TextView) view.findViewById(R.id.anti_size);
                holder.bt_uninstall = (Button) view.findViewById(R.id.bt_uninstall);
                view.setTag(holder);
            }
            AppInfo appInfo = antivirus.get(position);
            holder.anti_item_iv.setImageDrawable(appInfo.getIcon());
            holder.anti_name.setText(appInfo.getAppname());
            if (appInfo.isSetinrom()) {
                holder.anti_path.setText("手机内存");
            } else {
                holder.anti_path.setText("外部存储");
            }
            holder.anti_size.setText("应用大小:" + Formatter.formatFileSize(AntiVirusActivity.this, appInfo.getAppsize()));
            return view;
        }
    }

    private class ViewHolder {
        ImageView anti_item_iv;
        TextView anti_name;
        TextView anti_path;
        TextView anti_size;
        Button bt_uninstall;
    }

    /**
     * 加载应用程序信息和病毒库信息进行比较
     */
    private void Loaddate() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                List<AntivirusInfo> antivirusinfos = AntivirusDao.getAntivirus(AntiVirusActivity.this);
                scanerAppInfo = App_Manager_Engine.getScanerAppInfo(AntiVirusActivity.this);
                antivirus = new ArrayList<>();
                for (AppInfo info : scanerAppInfo) {
                    boolean flag = false;
                    if (isStop) break;
                    for (AntivirusInfo antinfo : antivirusinfos) {
                        if (isStop) break;
                        //如果获取的病毒的包名和MD5和手机程序相同则将病毒程序信息加入antivirus集合中
                        if (antinfo.getName().equals(info.getPackname()) && antinfo.getMd5().equals(info.getMd5())) {
                            antivirus.add(info);
                            flag = true;
                            Message msg = Message.obtain();
                            msg.what = ERROR;
                            msg.obj = info.getAppname();
                            mhandler.sendMessage(msg);
                        }
                    }
                    if (!flag) {
                        Message msg = Message.obtain();
                        msg.what = SUCESS;
                        msg.obj = info.getAppname();
                        mhandler.sendMessage(msg);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!isStop) {
                    i(TAG, "run" + "发生送");
                    Message msg = Message.obtain();
                    msg.what = SCANER_OVER;
                    msg.obj = null;
                    mhandler.sendMessage(msg);
                }

            }
        }.start();
    }

//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent main_intent = new Intent(AntiVirusActivity.this, HomeActivity.class);
//            startActivity(main_intent);
//            finish();
//            overridePendingTransition(R.anim.trans_next_out, R.anim.trans_next_in);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
