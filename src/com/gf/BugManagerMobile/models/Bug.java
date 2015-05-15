package com.gf.BugManagerMobile.models;

/**
 * Bug模型类
 * Created by Administrator on 5/16 0016.
 */
public class Bug {
    private static final String TAG = "Bug";

    private int id;
    private String name;
    private int project_id;
    private int module_id;
    private int priority;
    private int serious_id;
    private int assign_id;
    private int status;
    private int active_num;
    private int creator_id;
    private String create_time;
    private String img_path;
    private String file_path;
    private String introduce;
    private String reappear;
    private int resolve_id;
    private String resolve_time;
    private String close_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getSerious_id() {
        return serious_id;
    }

    public void setSerious_id(int serious_id) {
        this.serious_id = serious_id;
    }

    public int getAssign_id() {
        return assign_id;
    }

    public void setAssign_id(int assign_id) {
        this.assign_id = assign_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getActive_num() {
        return active_num;
    }

    public void setActive_num(int active_num) {
        this.active_num = active_num;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getReappear() {
        return reappear;
    }

    public void setReappear(String reappear) {
        this.reappear = reappear;
    }

    public int getResolve_id() {
        return resolve_id;
    }

    public void setResolve_id(int resolve_id) {
        this.resolve_id = resolve_id;
    }

    public String getResolve_time() {
        return resolve_time;
    }

    public void setResolve_time(String resolve_time) {
        this.resolve_time = resolve_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    @Override
    public String toString() {
        return "Bug{" + "id=" + id + ", name='" + name + '\'' + ", project_id=" + project_id + ", module_id="
            + module_id + ", priority=" + priority + ", serious_id=" + serious_id + ", assign_id=" + assign_id
            + ", status=" + status + ", active_num=" + active_num + ", creator_id=" + creator_id + ", create_time='"
            + create_time + '\'' + ", img_path='" + img_path + '\'' + ", file_path='" + file_path + '\''
            + ", introduce='" + introduce + '\'' + ", reappear='" + reappear + '\'' + ", resolve_id=" + resolve_id
            + ", resolve_time='" + resolve_time + '\'' + ", close_time='" + close_time + '\'' + '}';
    }
}
