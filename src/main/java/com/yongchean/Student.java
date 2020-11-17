package com.yongchean;

public class Student {
    private String number;
    private String matric;
    private String name;
    private String link;
    private String github_id;
    private int comment_number;

    Student(String number, String matric, String name, String link, String github_id, int comment_number){
        this.number = number;
        this.matric = matric;
        this.name = name;
        this.link = link;
        this.github_id = github_id;
        this.comment_number = comment_number;
    }

    //getter
    String get_number(){
        return number;
    }

    String get_matric(){
        return matric;
    }

    String get_name(){
        return name;
    }

    String get_link(){
        return link;
    }

    String get_github_id(){
        return github_id;
    }

    int get_comment_number(){
        return comment_number;
    }

    //setter
    void set_link(String link){
        this.link = link;
    }

    void set_github_id(String github_id){
        this.github_id = github_id;
    }

    void set_comment_number(int comment_number){
        this.comment_number = comment_number;
    }
}
