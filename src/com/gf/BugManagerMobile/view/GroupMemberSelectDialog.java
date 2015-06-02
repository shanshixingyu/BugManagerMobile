package com.gf.BugManagerMobile.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.gf.BugManagerMobile.R;
import com.gf.BugManagerMobile.adapter.GroupMemberAdapter;
import com.gf.BugManagerMobile.models.Group;
import com.gf.BugManagerMobile.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 选中用户
 * Created by GuLang on 2015-06-01.
 */
public class GroupMemberSelectDialog extends Dialog {
    private static final String TAG = "GroupMemberSelectDialog";

    private Button sureBtn, cancelBtn;
    private ListView allUserMemberLv;
    private GroupMemberAdapter mGroupMemberAdapter;
    private List<User> userData;
    private List<Integer> selectUserIdData = new ArrayList<Integer>();

    public GroupMemberSelectDialog(Context context) {
        this(context, 0);
    }

    public GroupMemberSelectDialog(Context context, int theme) {
        super(context, R.style.CustomDialogTheme2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_group_member);

        setCancelable(true);
        setCanceledOnTouchOutside(true);

        allUserMemberLv = (ListView) findViewById(R.id.group_member_lv);
        mGroupMemberAdapter = new GroupMemberAdapter(getContext(), userData);
        allUserMemberLv.setAdapter(mGroupMemberAdapter);

        sureBtn = (Button) findViewById(R.id.select_sure_btn);
        cancelBtn = (Button) findViewById(R.id.select_cancel_btn);
        sureBtn.setOnClickListener(onBtnClickListener);
        cancelBtn.setOnClickListener(onBtnClickListener);
    }

    public void resetListViewData(List<User> users) {
        userData = users;
        if (mGroupMemberAdapter != null)
            mGroupMemberAdapter.resetData(users);
    }

    private View.OnClickListener onBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            GroupMemberSelectDialog.this.dismiss();
            if (selectUserIdData == null)
                selectUserIdData = new ArrayList<Integer>();
            if (view.getId() == R.id.select_sure_btn) {
                selectUserIdData = mGroupMemberAdapter.getSelectedUserId();
            }
        }
    };

    public void show() {
        super.show();
        mGroupMemberAdapter.resetListState(selectUserIdData);
    }

    /**
     * 重置选中的项的内容
     */
    public void resetListState(List<Integer> selectUserIdData) {
        this.selectUserIdData = selectUserIdData;
        if (mGroupMemberAdapter != null)
            mGroupMemberAdapter.resetListState(selectUserIdData);
    }

    /**
     * 获得选中的用户ID列表
     * @return
     */
    public List<Integer> getSelectUserIdData() {
        if (selectUserIdData == null)
            selectUserIdData = new ArrayList<Integer>();
        return selectUserIdData;
    }

}
