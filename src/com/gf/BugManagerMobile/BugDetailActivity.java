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
import com.gf.BugManagerMobile.utils.*;
import com.gf.BugManagerMobile.view.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * bug详情界面
 * Created by Administrator on 5/16 0016.
 */
public class BugDetailActivity extends Activity {
    private static final String TAG = "BugDetailActivity";

    private RecyclerView mImageRecyclerView;
    private ImageRlvAdapter mImageRlvAdapter;
    private int bugId;
    private TextView nameTv, projectTv, moduleTv, statusTv, assignTv, priorityTv, creatorTv, createTimeTv, activeTv,
        closeTimeTv, imageTv, introduceTv, attachmentTv;
    private List<BugIntroduceItem> mBugIntroduceItems;
    private String attachmentName = null;
    private boolean solveItemStatus = false;
    private boolean modifyItemStatus = false;
    private boolean activeItemStatus = false;
    private boolean closeItemStatus = false;
    private boolean deleteItemStatus = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_detail);

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
        mImageRecyclerView = (RecyclerView) findViewById(R.id.bug_detail_image_rlv);
        mImageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mImageRecyclerView.setVisibility(View.GONE);
        mImageRlvAdapter = new ImageRlvAdapter(this, null);
        mImageRecyclerView.setAdapter(mImageRlvAdapter);

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
                    String projectName = jsonObject.getString("projectName");
                    String moduleName = jsonObject.getString("moduleName");
                    String assignName = jsonObject.getString("assignName");
                    String creatorName = jsonObject.getString("creatorName");
                    Bug bug = JSON.parseObject(jsonObject.getString("bug"), Bug.class);
                    if (bug != null) {
                        Log.i(TAG, "解析后的bug信息:" + bug.toString());
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
                                mImageRecyclerView.setVisibility(View.GONE);
                            } else {
                                imageTv.setVisibility(View.GONE);
                                mImageRecyclerView.setVisibility(View.VISIBLE);
                                mImageRlvAdapter.resetData(imagePaths);
                            }
                        } catch (Exception e) {
                            imageTv.setVisibility(View.VISIBLE);
                            mImageRecyclerView.setVisibility(View.GONE);
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
                        attachmentName = bug.getFile_path();
                        if (attachmentName == null || "".equals(attachmentName.trim())) {
                            attachmentTv.setVisibility(View.GONE);
                        } else {
                            attachmentTv.setVisibility(View.VISIBLE);
                            attachmentTv.setText(attachmentName);
                        }

                        resetOptPopStatus(bug.getStatus(), bug.getCreator_id());
                    }
                } catch (JSONException e) {
                    Toast.makeText(BugDetailActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(BugDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    };

    /**
     * 重新设置操作弹窗的状态
     * @param status
     * @param creatorId
     */
    private void resetOptPopStatus(int status, int creatorId) {
        /* 解决操作项：任何人 bug状态为未解决和重新激活时可操作 */
        if (status == 1 || status == 3)
            solveItemStatus = true;
        else
            solveItemStatus = false;

        LoginSuccessInfo mLoginSuccessInfo = LocalInfo.getLoginSuccessInfo(this);

        if (mLoginSuccessInfo.getRoleId() == 0 || mLoginSuccessInfo.getUserId() == creatorId) {
            /* 修改操作项：提交者 bug状态为除关闭外的所有状态可操作 */
            if (status != 0)
                modifyItemStatus = true;
            else
                modifyItemStatus = false;
            /* 激活操作项：提交者 bug状态为已解决、关闭、其它状态是可操作 */
            if (status == 0 || status == 2 || status == 4)
                activeItemStatus = true;
            else
                activeItemStatus = false;
            /* 关闭操作项：提交者 bug状态为已解决、其它状态可操作 */
            if (status == 2 || status == 4)
                closeItemStatus = true;
            else
                closeItemStatus = false;
            /* 删除操作项：提交者 bug状态为任何状态下可操作 */
            deleteItemStatus = true;
        } else {
            modifyItemStatus = false;
            activeItemStatus = false;
            closeItemStatus = false;
            deleteItemStatus = false;
        }
        initOptPopWindow();
    }

    private BugDetailOptPopWindow mBugDetailOptPopWindow;

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.bug_detail_back_imgv:
                this.finish();
                break;
            case R.id.bug_detail_opt:
                initOptPopWindow();
                mBugDetailOptPopWindow.showAsDropDown(v);
                break;
        }
    }

    /**
     * 设置操作弹窗的状态
     */
    private void initOptPopWindow() {
        if (mBugDetailOptPopWindow == null) {
            mBugDetailOptPopWindow = new BugDetailOptPopWindow(this);
            mBugDetailOptPopWindow.setOnOptItemClickListener(onOptItemClickListener);
        }

        mBugDetailOptPopWindow.setPopItemStatus(BugDetailOptPopWindow.ItemType.Solve, solveItemStatus);
        mBugDetailOptPopWindow.setPopItemStatus(BugDetailOptPopWindow.ItemType.Modify, modifyItemStatus);
        mBugDetailOptPopWindow.setPopItemStatus(BugDetailOptPopWindow.ItemType.Active, activeItemStatus);
        mBugDetailOptPopWindow.setPopItemStatus(BugDetailOptPopWindow.ItemType.Close, closeItemStatus);
        mBugDetailOptPopWindow.setPopItemStatus(BugDetailOptPopWindow.ItemType.Delete, deleteItemStatus);
    }

    private BugSolveDialog mBugSolveDialog = null;
    private BugActiveDialog mBugActiveDialog = null;
    private BugCloseDialog mBugCloseDialog = null;

    private BugDetailOptPopWindow.OnOptItemClickListener onOptItemClickListener =
        new BugDetailOptPopWindow.OnOptItemClickListener() {
            @Override
            public void onClick(BugDetailOptPopWindow.ItemType itemType) {
                Log.i(TAG, "" + itemType);
                if (itemType == BugDetailOptPopWindow.ItemType.Solve) {
                    if (mBugSolveDialog == null) {
                        mBugSolveDialog = new BugSolveDialog(BugDetailActivity.this);
                        mBugSolveDialog.setBugId(bugId);
                        mBugSolveDialog.setOnBugOptInterface(onBugOptListener);
                    }
                    mBugSolveDialog.show();
                } else if (itemType == BugDetailOptPopWindow.ItemType.Modify) {

                } else if (itemType == BugDetailOptPopWindow.ItemType.Active) {
                    if (mBugActiveDialog == null) {
                        mBugActiveDialog = new BugActiveDialog(BugDetailActivity.this);
                        mBugActiveDialog.setBugId(bugId);
                        mBugActiveDialog.setOnBugOptListener(onBugOptListener);
                    }
                    mBugActiveDialog.show();
                } else if (itemType == BugDetailOptPopWindow.ItemType.Close) {
                    if (mBugCloseDialog == null) {
                        mBugCloseDialog = new BugCloseDialog(BugDetailActivity.this);
                        mBugCloseDialog.setBugId(bugId);
                        mBugCloseDialog.setOnBugOptListener(onBugOptListener);
                    }
                    mBugCloseDialog.show();
                } else if (itemType == BugDetailOptPopWindow.ItemType.Delete) {
                    initDeleteConfirmDialog();
                    mDeleteConfirmDialog.show();
                }
            }
        };

    private OnBugOptListener onBugOptListener = new OnBugOptListener() {
        @Override
        public void onSaveSuccessFinish() {
            Log.i(TAG, "onSaveSuccessFinish");
            HttpVisitUtils.getHttpVisit(BugDetailActivity.this, LocalInfo.getBaseUrl(BugDetailActivity.this)
                + "bug/detail&bugId=" + bugId, true, "数据更新中...", onHttpFinishListener);
        }
    };

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
            intent.putParcelableArrayListExtra(MyConstant.BUG_DETAIL_2_BUG_INTRODUCE,
                (ArrayList<BugIntroduceItem>) mBugIntroduceItems);
            startActivity(intent);
        }
    }

    public void onAttachmentClick(View v) {
        if (attachmentName == null || "".equals(attachmentName.trim())) {
            Toast.makeText(this, "没有附件", Toast.LENGTH_SHORT).show();
            attachmentTv.setVisibility(View.GONE);
        } else {
            final File saveFile = new File(LocalInfo.getAttachmentSavePath() + attachmentName);
            if (saveFile.exists()) {
                ConfirmDialog confirmDialog = new ConfirmDialog(this);
                confirmDialog.setMessageTvText("保存的文件已经存在，继续下载将覆盖，是否继续？");
                confirmDialog.setLeftBtnText("取消");
                confirmDialog.setRightBtnText("继续");
                confirmDialog.setOnConfirmDialogListener(onConfirmDialogListener);
                confirmDialog.show();
            } else {
                downloadAttachmentFile();
            }

        }
    }

    private ConfirmDialog mDeleteConfirmDialog = null;

    private void initDeleteConfirmDialog() {
        if (mDeleteConfirmDialog == null) {
            mDeleteConfirmDialog = new ConfirmDialog(this);
            mDeleteConfirmDialog.setMessageTvText(R.string.bug_delete_message);
            mDeleteConfirmDialog.setLeftBtnText(R.string.bug_delete_cancel);
            mDeleteConfirmDialog.setRightBtnText(R.string.bug_delete_sure);
            mDeleteConfirmDialog.setOnConfirmDialogListener(new ConfirmDialog.OnConfirmDialogListener() {
                @Override
                public void onLeftBtnClick(ConfirmDialog dialog) {
                    dialog.dismiss();
                }

                @Override
                public void onRightBtnClick(ConfirmDialog dialog) {
                    dialog.dismiss();
                    HttpVisitUtils.getHttpVisit(BugDetailActivity.this, LocalInfo.getBaseUrl(BugDetailActivity.this)
                        + "bug/delete&bugId=" + bugId, true, "删除中...", onDeleteFinishListener);
                }
            });
        }
    }

    private HttpVisitUtils.OnHttpFinishListener onDeleteFinishListener = new HttpVisitUtils.OnHttpFinishListener() {
        @Override
        public void onVisitFinish(HttpResult result) {
            if (result == null)
                return;
            if (result.isVisitSuccess()) {
                Toast.makeText(BugDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                BugDetailActivity.this.finish();
            } else {
                HttpConnectResultUtils.optFailure(BugDetailActivity.this, result);
            }
        }
    };

    private ConfirmDialog.OnConfirmDialogListener onConfirmDialogListener =
        new ConfirmDialog.OnConfirmDialogListener() {
            @Override
            public void onLeftBtnClick(ConfirmDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onRightBtnClick(ConfirmDialog dialog) {
                dialog.dismiss();
                downloadAttachmentFile();
            }
        };

    /**
     * 下载附件
     */
    private void downloadAttachmentFile() {
        String name = null;
        try {
            name = URLEncoder.encode(attachmentName, "UTF-8");
        } catch (Exception e) {
            name = attachmentName;
        }
        String url = LocalInfo.getBaseUrl(this) + "bug/download&fileName=" + name;
        HttpVisitUtils.downloadFile(this, url, attachmentName, true, "正在下载");
    }
}
