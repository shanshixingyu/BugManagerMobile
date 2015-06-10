package com.gf.BugManagerMobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.gf.BugManagerMobile.models.Group;
import com.gf.BugManagerMobile.models.HttpResult;
import com.gf.BugManagerMobile.models.User;
import com.gf.BugManagerMobile.utils.*;
import com.gf.BugManagerMobile.view.GroupMemberSelectDialog;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * 修改团队信息
 * Created by GuLang on 2015-06-02.
 */
public class ModifyGroupActivity extends BaseActivity {
    private static final String TAG = "ModifyGroupActivity";

    private int groupId;
    private Group mGroup;
    private String mCreatorName;
    private List<User> mAllUserData;
    private List<Integer> mSelectedUserIds;

    private Button topOptSaveBtn;
    private EditText groupNameEt, groupIntroduceEt;
    private TextView groupMemberTv, groupCreatorTv, groupCreateTimeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_modify);

        if (savedInstanceState != null)
            groupId = savedInstanceState.getInt("GROUP_ID", -1);
        else
            groupId = getIntent().getIntExtra(MyConstant.GROUP_LIST_2_GROUP_MODIFY, -1);
        if (groupId < 0) {
            Toast.makeText(this, "传递团队ID数据出现问题", Toast.LENGTH_SHORT).show();
        } else {
            HttpVisitUtils.getHttpVisit(this, LocalInfo.getBaseUrl(this) + "group/get-info&groupId=" + groupId, true,
                "加载团队信息中...", loadGroupDataFinishListener);
        }

        initComponent();
    }

    private void initComponent() {
        topOptSaveBtn = (Button) findViewById(R.id.group_modify_save_btn);
        groupNameEt = (EditText) findViewById(R.id.group_modify_name_et);
        groupMemberTv = (TextView) findViewById(R.id.group_modify_member_tv);
        groupIntroduceEt = (EditText) findViewById(R.id.group_modify_introduce_et);
        groupCreatorTv = (TextView) findViewById(R.id.group_modify_creator_tv);
        groupCreateTimeTv = (TextView) findViewById(R.id.group_modify_create_time_tv);
    }

    private HttpVisitUtils.OnHttpFinishListener loadGroupDataFinishListener =
        new HttpVisitUtils.OnHttpFinishListener() {
            @Override
            public void onVisitFinish(HttpResult result) {
                if (result == null)
                    return;
                if (result.isVisitSuccess()) {
                    Log.i(TAG, result.toString());
                    try {
                        JSONObject jsonObj = new JSONObject(result.getResult());
                        mGroup = JSON.parseObject(jsonObj.getString("group"), Group.class);
                        mCreatorName = jsonObj.getString("creatorName");
                        mAllUserData = JSON.parseArray(jsonObj.getString("allUser"), User.class);

                        if (mGroup != null) {
                            topOptSaveBtn.setEnabled(true);
                            topOptSaveBtn.setTextColor(Color.WHITE);
                            groupMemberTv.setVisibility(View.VISIBLE);

                            try {
                                mSelectedUserIds = JSON.parseArray(mGroup.getMember(), Integer.class);
                                // Log.i(TAG, "解析出来的序列:" + mSelectedUserIds.toString());
                            } catch (Exception e) {
                                // Log.i(TAG, "成员序列解析失败");
                                mSelectedUserIds = null;
                            }

                            groupNameEt.setText(mGroup.getName());
                            groupIntroduceEt.setText(mGroup.getIntroduce());
                            groupCreatorTv.setText(mCreatorName);
                            groupCreateTimeTv.setText(mGroup.getCreate_time());
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ModifyGroupActivity.this, "团队信息解析失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    HttpConnectResultUtils.optFailure(ModifyGroupActivity.this, result);
                }
            }
        };

    private GroupMemberSelectDialog mGroupMemberSelectDialog = null;

    public void onSelectMemberClick(View v) {
        if (mGroupMemberSelectDialog == null) {
            mGroupMemberSelectDialog = new GroupMemberSelectDialog(this);
            mGroupMemberSelectDialog.resetListViewData(mAllUserData);
            mGroupMemberSelectDialog.resetListState(mSelectedUserIds);
        }
        if (!mGroupMemberSelectDialog.isShowing())
            mGroupMemberSelectDialog.show();
    }

    public void onOptClick(View v) {
        switch (v.getId()) {
            case R.id.group_modify_back_imgv:
                this.finish();
                break;
            case R.id.group_modify_save_btn:
                saveModifyGroupInfo();
                break;
        }
    }

    /**
     * 保存修改的信息
     */
    private void saveModifyGroupInfo() {
        /* 团队名称 */
        String groupName = groupNameEt.getText().toString().trim();
        if ("".equals(groupName)) {
            groupNameEt.setError("团队名称必填");
            return;
        }
        if (!RegexUtils.isMatch(MyConstant.NAME_PATTERN, groupName)) {
            groupNameEt.setError("只能输入中文、英文、数字、下划线");
            return;
        }

        /* 团队成员 */
        List<Integer> selectedUserIdData = null;
        if (mGroupMemberSelectDialog != null)
            selectedUserIdData = mGroupMemberSelectDialog.getSelectUserIdData();
        else
            selectedUserIdData = mSelectedUserIds;

        /* 团队简介 */
        String groupIntroduce = groupIntroduceEt.getText().toString().trim();

        if (!isChanged(groupName, selectedUserIdData, groupIntroduce)) {
            Toast.makeText(this, "未修改任何数据", Toast.LENGTH_SHORT).show();
            return;
        }

        String postData =
            "name=" + groupName + "&memberIds=" + JSON.toJSON(selectedUserIdData) + "&introduce=" + groupIntroduce;

        HttpVisitUtils.postHttpVisit(this, LocalInfo.getBaseUrl(this) + "group/modify&groupId=" + groupId, postData,
            true, "添加中...", onModifyGroupFinishListener);
    }

    private HttpVisitUtils.OnHttpFinishListener onModifyGroupFinishListener =
        new HttpVisitUtils.OnHttpFinishListener() {
            @Override
            public void onVisitFinish(HttpResult result) {
                if (result == null)
                    return;
                if (result.isVisitSuccess()) {
                    Toast.makeText(ModifyGroupActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    /* 需要更新选中的团队成员信息，不然下次保存判断是否修改了的时候就会出错 */
                    if (mGroupMemberSelectDialog != null)
                        mSelectedUserIds = mGroupMemberSelectDialog.getSelectUserIdData();
                } else {
                    HttpConnectResultUtils.optFailure(ModifyGroupActivity.this, result);
                }
            }
        };

    /**
     * 判断数据是否发生修改
     * @param name
     * @param selectedUserId
     * @param introduce
     * @return
     */
    private boolean isChanged(String name, List<Integer> selectedUserId, String introduce) {
        if (mGroup == null)
            return false;
        if (mGroup.getName() != null && !mGroup.getName().equals(name))
            return true;
        if (mGroup.getIntroduce() != null && !mGroup.getIntroduce().equals(introduce))
            return true;
        if (mSelectedUserIds != null && selectedUserId != null) {
            int oldCount = mSelectedUserIds.size();
            if (oldCount == selectedUserId.size()) {// 只有如此才有可能相同
                for (int i = 0; i < oldCount; i++) {// 只要发现一个新的列表中不存在原始列表的一个元素，表示已经改变
                    if (!selectedUserId.contains(mSelectedUserIds.get(i)))
                        return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putInt("GROUP_ID", groupId);
        }
        super.onSaveInstanceState(outState);
    }
}
