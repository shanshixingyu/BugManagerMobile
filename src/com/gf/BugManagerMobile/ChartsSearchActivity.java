package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.adapter.ProjectSpinnerAdapter;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.models.Project;
import com.gf.BugManagerMobile.utils.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 生成报表条件界面
 * Created by GuLang on 2015-06-05.
 */
public class ChartsSearchActivity extends Activity {
    private static final String TAG = "ChartsSearchActivity";

    private Spinner mProjectSp;
    private Button mTopSearchBtn;
    private EditText mStartDateEt, mEndDateEt;
    private ProjectSpinnerAdapter mProjectSpinnerAdapter;
    private List<Project> mProjectDataList;
    private SimpleDateFormat mSimpleDateFormat;
    private Date mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts_search);

        mProjectSp = (Spinner) findViewById(R.id.charts_search_project_sp);
        mTopSearchBtn = (Button) findViewById(R.id.charts_search_top_btn);
        mStartDateEt = (EditText) findViewById(R.id.charts_search_start_date_et);
        mEndDateEt = (EditText) findViewById(R.id.charts_search_end_date_et);

        mSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        mSimpleDateFormat.setTimeZone(TimeZone.getDefault());
        mDate = new Date();

        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "bug/get-all-project", true, "加载项目信息中...",
            onLoadProjectFinishListener);
    }

    private HttpVisitUtils.OnHttpFinishListener onLoadProjectFinishListener =
        new HttpVisitUtils.OnHttpFinishListener() {
            @Override
            public void onVisitFinish(HttpResult result) {
                if (result == null)
                    return;
                if (result.isVisitSuccess()) {
                    try {
                        mProjectDataList = JSON.parseArray(result.getResult(), Project.class);
                        mProjectSpinnerAdapter = new ProjectSpinnerAdapter(ChartsSearchActivity.this, mProjectDataList);
                        mProjectSp.setAdapter(mProjectSpinnerAdapter);

                        mTopSearchBtn.setVisibility(View.VISIBLE);

                        long now = System.currentTimeMillis();
                        long sevenDayAgo = now - MyConstant.SEVEN_DAY_MILLISECOND_LENGTH;
                        mDate.setTime(sevenDayAgo);
                        mStartDateEt.setText(mSimpleDateFormat.format(mDate));
                        mDate.setTime(now);
                        mEndDateEt.setText(mSimpleDateFormat.format(mDate));
                    } catch (Exception e) {
                        Toast.makeText(ChartsSearchActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    HttpConnectResultUtils.optFailure(ChartsSearchActivity.this, result);
                }
            }
        };

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.charts_search_back_imgv:
                this.finish();
                break;
            case R.id.charts_search_top_btn:
                searchCharts();
                break;
        }
    }

    private void searchCharts() {
        /* 项目 */
        int projectId;
        if (mProjectSpinnerAdapter == null) {
            Toast.makeText(this, "暂时没有任何项目信息", Toast.LENGTH_SHORT).show();
            return;
        } else {
            final Project project = mProjectSpinnerAdapter.getItem(mProjectSp.getSelectedItemPosition());
            if (project == null) {
                Toast.makeText(this, "没有选中任何项目", Toast.LENGTH_SHORT).show();
                return;
            }
            projectId = project.getId();
        }

        /* 开始日期 */
        String startDateStr = mStartDateEt.getText().toString().trim();
        if ("".equals(startDateStr)) {
            mStartDateEt.setError("开始日期必填");
            return;
        }
        if (!RegexUtils.isMatch(MyConstant.DATE_PATTERN, startDateStr)) {
            mStartDateEt.setError("格式不合法");
            return;
        }
        long startTime;
        try {
            Date startDate = mSimpleDateFormat.parse(startDateStr);
            startTime = startDate.getTime() / 1000;
            Log.i(TAG, "开始时间计算前：" + startDate.getTime() + ",计算后：" + startTime);
        } catch (ParseException e) {
            mStartDateEt.setError("格式不合法");
            return;
        }

        /* 结束日期 */
        String endDateStr = mEndDateEt.getText().toString().trim();
        if ("".equals(endDateStr)) {
            mEndDateEt.setError("结束日期必填");
            return;
        }
        if (!RegexUtils.isMatch(MyConstant.DATE_PATTERN, endDateStr)) {
            mEndDateEt.setError("格式不合法");
            return;
        }
        long endTime;
        try {
            Date endDate = mSimpleDateFormat.parse(endDateStr);
            endTime = endDate.getTime() / 1000;
            Log.i(TAG, "结束时间计算前：" + endDate.getTime() + ",计算后：" + endTime);
        } catch (ParseException e) {
            mStartDateEt.setError("格式不合法");
            return;
        }

        Intent intent = new Intent(this, ChartsShowActivity.class);
        intent.putExtra(MyConstant.CHARTS_SEARCH_2_CHARTS_SHOW_PROJECT_ID, projectId);
        intent.putExtra(MyConstant.CHARTS_SEARCH_2_CHARTS_SHOW_START_TIME, startTime);
        intent.putExtra(MyConstant.CHARTS_SEARCH_2_CHARTS_SHOW_END_TIME, endTime);
        startActivity(intent);
    }
}
