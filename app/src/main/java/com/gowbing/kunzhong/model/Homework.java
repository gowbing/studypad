package com.gowbing.kunzhong.model;

/**
 * Created by Administrator on 2018-8-25.
 */

public class Homework {
    private int id;
    private String publish_date;
    private String title;
    private String content;
    private String pics;
    private Long create_time;
    private Long end_time;
    private String teacher_name;
    private String subject_name;
    private String answer_content;
    private String answer_pics;
    private Long answer_create_time;
    private String student_name;
    private String teacher_comment;
    private String teacher_comment_time;
    private String class_name;
    private int answer_id;
    private int answer_status;
    private int is_done;

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }

    public int getIs_done() {
        return is_done;
    }

    public void setIs_done(int is_done) {
        this.is_done = is_done;
    }

    public int getAnswer_status() {
        return answer_status;
    }

    public void setAnswer_status(int answer_status) {
        this.answer_status = answer_status;
    }

    public int getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }

    public String getAnswer_content() {
        return answer_content;
    }

    public void setAnswer_content(String answer_content) {
        this.answer_content = answer_content;
    }

    public String getAnswer_pics() {
        return answer_pics;
    }

    public void setAnswer_pics(String answer_pics) {
        this.answer_pics = answer_pics;
    }

    public Long getAnswer_create_time() {
        return answer_create_time;
    }

    public void setAnswer_create_time(Long answer_create_time) {
        this.answer_create_time = answer_create_time;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getTeacher_comment() {
        return teacher_comment;
    }

    public void setTeacher_comment(String teacher_comment) {
        this.teacher_comment = teacher_comment;
    }

    public String getTeacher_comment_time() {
        return teacher_comment_time;
    }

    public void setTeacher_comment_time(String teacher_comment_time) {
        this.teacher_comment_time = teacher_comment_time;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }
}
