package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.models.ChartDataItem;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.utils.HttpConnectResultUtils;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.LocalInfo;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.*;

/**
 * 显示缺陷报表
 * Created by GuLang on 2015-06-05.
 */
public class ChartsShowActivity extends Activity {
    private static final String TAG = "ChartsShowActivity";

    private int mProjectId;
    private long mStartTime;
    private long mEndTime;
    private BarChart mDaySubmitBarChart;
    private BarChart mMonthSubmitBarChart;
    private BarChart mDayTotalBarChart;
    private BarChart mMonthTotalBarChart;
    private PieChart mModulePieChart;
    private PieChart mPriorityChart;
    private PieChart mStatusChart;

    private List<ChartDataItem> mChartDataItemList;
    private List<Integer> mPieColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts_show);

        Intent startIntent = getIntent();
        mProjectId = startIntent.getIntExtra(MyConstant.CHARTS_SEARCH_2_CHARTS_SHOW_PROJECT_ID, -1);
        mStartTime = startIntent.getLongExtra(MyConstant.CHARTS_SEARCH_2_CHARTS_SHOW_START_TIME, -1);
        mEndTime = startIntent.getLongExtra(MyConstant.CHARTS_SEARCH_2_CHARTS_SHOW_END_TIME, -1);

        if (mProjectId < 0 || mStartTime < 0 || mEndTime < 0) {
            Toast.makeText(this, "传递数据出错", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, "传递数据：projectId=" + mProjectId + ",startTime=" + mStartTime + ",endTime=" + mEndTime);

        String pathUrl =
            LocalInfo.getBaseUrl(this) + "bug/charts&projectId=" + mProjectId + "&startTime=" + mStartTime
                + "&endTime=" + mEndTime;
        Log.i(TAG, "路径:" + pathUrl);

        initComponent();

        HttpVisitUtils.getHttpVisit(this, pathUrl, true, "查询中...", onSearchFinishListener);
    }

    private void initComponent() {
        mDaySubmitBarChart = (BarChart) findViewById(R.id.charts_show_day_submit_bar);
        initBarChart(mDaySubmitBarChart);
        mMonthSubmitBarChart = (BarChart) findViewById(R.id.charts_show_month_submit_bar);
        initBarChart(mMonthSubmitBarChart);
        mDayTotalBarChart = (BarChart) findViewById(R.id.charts_show_day_total_bar);
        initBarChart(mDayTotalBarChart);
        mMonthTotalBarChart = (BarChart) findViewById(R.id.charts_show_month_total_bar);
        initBarChart(mMonthTotalBarChart);

        mModulePieChart = (PieChart) findViewById(R.id.charts_show_module_pie);
        initPieChart(mModulePieChart);
        mPriorityChart = (PieChart) findViewById(R.id.charts_show_priority_pie);
        initPieChart(mPriorityChart);
        mStatusChart = (PieChart) findViewById(R.id.charts_show_status_pie);
        initPieChart(mStatusChart);
    }

    private void initBarChart(BarChart barChart) {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        // mDaySubmitBarChart.setDescription("缺陷提交日期分布表（天）");
        barChart.setDescription("");
        barChart.setMaxVisibleValueCount(12);
        barChart.setPinchZoom(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setDrawValuesForWholeStack(true);
        // x轴
        XAxis xDaySubmitAxis = barChart.getXAxis();
        xDaySubmitAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xDaySubmitAxis.setDrawGridLines(true);
        xDaySubmitAxis.setSpaceBetweenLabels(2);
        // y轴
        YAxis yDaySubmitAxis = barChart.getAxisLeft();
        yDaySubmitAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yDaySubmitAxis.setDrawGridLines(true);
        yDaySubmitAxis.setLabelCount(8);
        yDaySubmitAxis.setSpaceTop(15f);

        YAxis yRightAxis = barChart.getAxisRight();
        yRightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yRightAxis.setEnabled(false);
        barChart.animateY(2500);
    }

    private void initPieChart(PieChart pieChart) {
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);

        mPieColors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            mPieColors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            mPieColors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            mPieColors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            mPieColors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            mPieColors.add(c);
        mPieColors.add(ColorTemplate.getHoloBlue());

    }

    private HttpVisitUtils.OnHttpFinishListener onSearchFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Log.i(TAG, result.getResult());
                try {
                    mChartDataItemList = JSON.parseArray(result.getResult(), ChartDataItem.class);
                    resetCharts();
                } catch (Exception e) {
                    Toast.makeText(ChartsShowActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                HttpConnectResultUtils.optFailure(ChartsShowActivity.this, result);
            }
        }
    };

    private void resetCharts() {
        if (mChartDataItemList != null) {
            for (ChartDataItem itemData : mChartDataItemList) {
                Log.i(TAG, itemData.toString());
                String type = itemData.getType();
                if (type == null) {

                } else if (type.equals(MyConstant.ECHART_TYPE_SUBMIT_DAY)) {
                    updateBarChart(mDaySubmitBarChart, itemData);
                } else if (type.equals(MyConstant.ECHART_TYPE_SUBMIT_MONTH)) {
                    updateBarChart(mMonthSubmitBarChart, itemData);
                } else if (type.equals(MyConstant.ECHART_TYPE_TOTAL_DAY)) {
                    updateBarChart(mDayTotalBarChart, itemData);
                } else if (type.equals(MyConstant.ECHART_TYPE_TOTAL_MONTH)) {
                    updateBarChart(mMonthTotalBarChart, itemData);
                } else if (type.equals(MyConstant.ECHART_TYPE_MODULE)) {
                    updatePieChart(mModulePieChart, itemData);
                } else if (type.equals(MyConstant.ECHART_TYPE_PRIORITY)) {
                    updatePieChart(mPriorityChart, itemData);
                } else if (type.equals(MyConstant.ECHART_TYPE_STATUS)) {
                    updatePieChart(mStatusChart, itemData);
                }
            }
        }
    }

    private void updateBarChart(BarChart barChart, ChartDataItem itemData) {
        if (itemData != null && itemData.getData() != null) {
            List<String> xValDataList = new ArrayList<String>();
            List<BarEntry> yValDataList = new ArrayList<BarEntry>();

            TreeMap<String, Integer> dataMap = itemData.getData();
            Set<String> keySet = itemData.getData().keySet();
            int index = 0;
            for (String key : keySet) {
                xValDataList.add(key);
                yValDataList.add(new BarEntry(dataMap.get(key), index));
                index++;
            }

            BarDataSet yBarDataSet = new BarDataSet(yValDataList, "缺陷数量");

            BarData data = new BarData(xValDataList, yBarDataSet);
            data.setValueTextSize(10f);
            barChart.setData(data);
        }
    }

    private void updatePieChart(PieChart pieChart, ChartDataItem itemData) {
        if (itemData != null && itemData.getData() != null) {
            List<String> xValDataList = new ArrayList<String>();
            List<Entry> yValDataList = new ArrayList<Entry>();

            TreeMap<String, Integer> dataMap = itemData.getData();
            Set<String> keySet = itemData.getData().keySet();
            int index = 0;
            int preIndex = 0;// 上一个不是0的
            for (String key : keySet) {
                xValDataList.add(key);
                int itemValue = dataMap.get(key);
                if (itemValue <= 0) {
                    yValDataList.add(new Entry(itemValue, index));
                    preIndex = index;
                } else {
                    yValDataList.add(new Entry(itemValue, preIndex));
                }
                index++;
            }

            PieDataSet dataSet = new PieDataSet(yValDataList, "");

            dataSet.setColors(mPieColors);
            PieData data = new PieData(xValDataList, dataSet);
            pieChart.setData(data);
        }
    }

    public void onOptClick(View view) {
        switch (view.getId()) {
            case R.id.charts_show_back_imgv:
                this.finish();
                break;
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return true;
        }
    }

}
