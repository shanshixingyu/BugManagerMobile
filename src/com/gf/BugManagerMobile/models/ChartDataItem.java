package com.gf.BugManagerMobile.models;

import java.util.Map;
import java.util.TreeMap;

/**
 * 每个图表的信息
 * Created by GuLang on 2015-06-06.
 */
public class ChartDataItem {
    private static final String TAG = "ChartDataItem";

    private String type;
    private TreeMap<String, Integer> data;
    private int totalCount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TreeMap<String, Integer> getData() {
        return data;
    }

    public void setData(TreeMap<String, Integer> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "ChartDataItem{" + "type='" + type + '\'' + ", data=" + data + ", totalCount=" + totalCount + '}';
    }
}
