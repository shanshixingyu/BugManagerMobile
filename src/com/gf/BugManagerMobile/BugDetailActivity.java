package com.gf.BugManagerMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.adapter.ImageRlvAdapter;
import com.gf.BugManagerMobile.models.*;
import com.gf.BugManagerMobile.utils.BugUtils;
import com.gf.BugManagerMobile.utils.HttpVisitUtils;
import com.gf.BugManagerMobile.utils.MyConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * bug详情界面
 * Created by Administrator on 5/16 0016.
 */
public class BugDetailActivity extends Activity {
    private static final String TAG = "BugDetailActivity";

    private RecyclerView mImageRecylerView;
    private ImageRlvAdapter mImageRlvAdapter;
    private int bugId;
    private TextView nameTv, projectTv, moduleTv, statusTv, assignTv, priorityTv, creatorTv, createTimeTv, activeTv,
        closeTimeTv, imageTv, introduceTv, attachmentTv;
    private List<BugIntroduceItem> mBugIntroduceItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_bug_detail);
        // setCenterContentLyt(R.layout.page_bug_detail);

        initComponent();

        bugId = getIntent().getIntExtra(MyConstant.BUG_LIST_2_BUG_DETAIL_BUG_ID, -1);

        if (bugId < 0) {
            Toast.makeText(this, "获取缺陷ID失败", Toast.LENGTH_SHORT).show();
        } else {
            HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "bug/detail&bugId=" + bugId, true,
                "加载数据中...", onHttpFinishListener);
        }

    }

    private void initComponent() {
        mImageRecylerView = (RecyclerView) findViewById(R.id.bug_detail_image_rlv);
        mImageRecylerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mImageRecylerView.setVisibility(View.GONE);
        mImageRlvAdapter = new ImageRlvAdapter(this, null);
        mImageRecylerView.setAdapter(mImageRlvAdapter);

        nameTv = (TextView) findViewById(R.id.bug_detail_name_tv);
        projectTv = (TextView) findViewById(R.id.bug_detail_project_tv);
        moduleTv = (TextView) findViewById(R.id.bug_detail_module_tv);
        statusTv = (TextView) findViewById(R.id.bug_detail_status_tv);
        assignTv = (TextView) findViewById(R.id.bug_detail_assgin_tv);
        priorityTv = (TextView) findViewById(R.id.bug_detail_priority_tv);
        creatorTv = (TextView) findViewById(R.id.bug_detail_creator_tv);
        createTimeTv = (TextView) findViewById(R.id.bug_detail_create_time_tv);
        activeTv = (TextView) findViewById(R.id.bug_detail_active_tv);
        closeTimeTv = (TextView) findViewById(R.id.bug_detail_close_time_tv);
        imageTv = (TextView) findViewById(R.id.bug_detail_image_tv);
        introduceTv = (TextView) findViewById(R.id.bug_detail_introduce_tv);
        attachmentTv = (TextView) findViewById(R.id.bug_detail_attachment_tv);

    }

    private HttpVisitUtils.OnHttpFinishListener onHttpFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null) {
                Toast.makeText(BugDetailActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                return;
            }
            if (result.isVisitSuccess()) {
                if (result.getResult() == null) {
                    Toast.makeText(BugDetailActivity.this, "数据过期", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.getResult());
                    String bugStr = jsonObject.getString("bug");
                    if (bugStr == null) {
                        Toast.makeText(BugDetailActivity.this, "数据过期", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Bug bug = JSON.parseObject(jsonObject.getString("bug"), Bug.class);
                    Log.i(TAG, "解析后的bug信息:" + bug.toString());
                    String projectName = jsonObject.getString("projectName");
                    String moduleName = jsonObject.getString("moduleName");
                    String assignName = jsonObject.getString("assignName");
                    String creatorName = jsonObject.getString("creatorName");
                    Log.i(TAG, "name=" + bug.getName());
                    nameTv.setText(bug.getName());
                    projectTv.setText(projectName);
                    moduleTv.setText(moduleName);
                    statusTv.setText(BugUtils.getStatusStr(bug.getStatus()));
                    assignTv.setText(assignName);
                    priorityTv.setText(BugUtils.getPriorityStr(bug.getPriority()));
                    creatorTv.setText(creatorName);
                    createTimeTv.setText(bug.getCreate_time());
                    activeTv.setText("" + bug.getActive_num());
                    closeTimeTv.setText(bug.getClose_time());
                    try {
                        List<String> imagePaths = JSON.parseArray(bug.getImg_path(), String.class);
                        if (imagePaths.size() <= 0) {
                            imageTv.setVisibility(View.VISIBLE);
                            mImageRecylerView.setVisibility(View.GONE);
                        } else {
                            imageTv.setVisibility(View.GONE);
                            mImageRecylerView.setVisibility(View.VISIBLE);
                            mImageRlvAdapter.resetData(imagePaths);
                        }
                    } catch (Exception e) {
                        imageTv.setVisibility(View.VISIBLE);
                        mImageRecylerView.setVisibility(View.GONE);
                    }
                    String introduce = bug.getIntroduce();
                    if (introduce == null || introduce.trim().equals("")) {
                        introduceTv.setVisibility(View.GONE);
                    } else {
                        try {
                            mBugIntroduceItems = JSON.parseArray(introduce, BugIntroduceItem.class);
                            if (mBugIntroduceItems.size() <= 0) {
                                introduceTv.setVisibility(View.GONE);
                            } else {
                                introduceTv.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            introduceTv.setVisibility(View.GONE);
                        }

                    }
                    String attachment = bug.getFile_path();
                    if (attachment == null || "".equals(attachment.trim())) {
                        attachmentTv.setVisibility(View.GONE);
                    } else {
                        attachmentTv.setVisibility(View.VISIBLE);
                        attachmentTv.setText(bug.getFile_path());
                    }

                } catch (JSONException e) {
                    Toast.makeText(BugDetailActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(BugDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    };

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.bug_detail_back_imgv:
                this.finish();
                break;
        }
    }

    /**
     * 处理点击查看bug注释事件
     *
     * @param v
     */
    public void onIntroduceClick(View v) {
        if (mBugIntroduceItems == null || mBugIntroduceItems.size() <= 0) {
            Toast.makeText(this, "没有本缺陷的注释", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, BugIntroduceActivity.class);
            // int count = mBugIntroduceItems.size();
            // ArrayList<BugIntroduceItem> introduces = new ArrayList<BugIntroduceItem>();
            // for (int i = count - 1; i >= 0; i--) {
            // introduces.add(mBugIntroduceItems.get(i));
            // }
            intent.putParcelableArrayListExtra(MyConstant.BUG_DETAIL_2_BUG_INTRODUCE,
                (ArrayList<BugIntroduceItem>) mBugIntroduceItems);
            startActivity(intent);
        }
    }
}
