package com.gf.BugManagerMobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.adapter.ProjectModuleAdapter;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.models.LocalInfo;
import com.gf.BugManagerMobile.models.ProjectModuleInfo;
import com.gf.BugManagerMobile.utils.HttpConnectResultUtils;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.MyConstant;

import java.net.HttpURLConnection;
import java.util.List;

/**
 * 显示项目模块信息
 * Created by Administrator on 5/14 0014.
 */
public class ProjectModuleActivity extends BaseActivity {
    private static final String TAG = "ProjectModuleActivity";

    private ExpandableListView projectModuleXlv;
    private ProjectModuleAdapter projectModuleAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCenterContentLyt(R.layout.page_project_module);

        projectModuleXlv = (ExpandableListView) findViewById(R.id.project_module_xlv);
        projectModuleAdapter = new ProjectModuleAdapter(this);
        projectModuleXlv.setAdapter(projectModuleAdapter);

        HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "project/project-module", true, "加载中...",
            loadDataOnHttpFinishListener);

    }

    private HttpVisitUtils.OnHttpFinishListener loadDataOnHttpFinishListener =
        new HttpVisitUtils.OnHttpFinishListener() {
            @Override
            public void onVisitFinish(HttpResult result) {
                // if (result == null)
                // throw new NullPointerException("加载数据出错");
                if (result == null)
                    return;
                Log.i(TAG, result.toString());
                if (result.getHttpResponseCode() == HttpURLConnection.HTTP_OK) {
                    if (result.getCode() == MyConstant.VISIT_CODE_SUCCESS) {
                        List<ProjectModuleInfo> projectModuleInfo = null;
                        try {
                            projectModuleInfo = JSON.parseArray(result.getResult(), ProjectModuleInfo.class);
                            projectModuleAdapter.setData(projectModuleInfo);
                        } catch (Exception e) {
                            // Log.e(TAG, "解析出现错误");
                            Toast.makeText(ProjectModuleActivity.this, "解析异常", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(ProjectModuleActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    HttpConnectResultUtils.optFailure(ProjectModuleActivity.this, result);
                }

            }
        };

    /**
     * 顶部栏事件处理
     * @param v
     */
    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.project_module_back_imgv:
                this.finish();
                break;
        }
    }

}
