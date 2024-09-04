package com.example.mukherjinagarkbc;



public class QuestionModel {
    String question,option1,option2,option3,option4;
    String timer;
    int time,right_ans;

    public QuestionModel(String question, String option1, String option2, String option3, String option4,int right_ans,int time) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.right_ans=right_ans;
        this.time = time;
    }

    public QuestionModel(){

    }
}
