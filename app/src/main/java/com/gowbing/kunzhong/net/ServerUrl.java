package com.gowbing.kunzhong.net;

/**
 * Created by 刘 on 2016/10/19.
 */
public class ServerUrl {
    public static final String SERVER = "http://kunzhong.kssina.com";
    public static final String APP = "/api.php";
    public static final String V1_0 = "/api";
    public static final String V2_0 = "/v2.0";
    /**
     * 获取学科列表
     */
    public static final String GET_SUBJECT_LIST = SERVER + APP + V1_0 + "/get_subjects";

    /**
     * 获取学科列表
     */
    public static final String LOGIN = SERVER + APP + V1_0 + "/login";

    /**
     * 获取最新作业列表
     */
    public static final String GET_LAST_HOMEWORK = SERVER + APP + V1_0 + "/get_last_homework";

    /**
     * 获取作业详情
     */
    public static final String GET_HOMEWORK_DETAIL = SERVER + APP + V1_0 + "/get_homework";

    /**
     * 获取往期作业列表
     */
    public static final String GET_PREVIOUS_HOMEWORK = SERVER + APP + V1_0 + "/get_previous_homework";

    /**
     * 获取学习资料
     */
    public static final String GET_LEARN_INFO = SERVER + APP + V1_0 + "/get_learn_info";

    /**
     * 修改密码
     */
    public static final String CHANGE_PASSWORD = SERVER + APP + V1_0 + "/change_password";

    /**
     * 上传图片
     */
    public static final String UPLOAD_IMG = SERVER + APP + V1_0 + "/upload_img";


    /**
     * 获取学习资料详情
     */
    public static final String GET_LEARN_DETAIL = SERVER + APP + V1_0 + "/get_learn_info_by_id";

    /**
     * 获取最新作业数量
     */
    public static final String GET_LAST_HOMEWORK_NUM = SERVER + APP + V1_0 + "/get_last_homework_num";

    /**
     * 获取往期作业数量
     */
    public static final String GET_PREVIOUS_HOMEWORK_NUM = SERVER + APP + V1_0 + "/get_previous_homework_num";

    /**
     * 新增/保存作业
     */
    public static final String SAVE_HOMEWORK = SERVER + APP + V1_0 + "/save_homework";


}
