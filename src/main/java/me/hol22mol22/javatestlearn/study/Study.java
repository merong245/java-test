package me.hol22mol22.javatestlearn.study;

public class Study {

    private StudyStatus status = StudyStatus.DRAFT;

    private int limit;

    public Study(int limit){
        this.limit = limit;
    }

    public StudyStatus getStatus(){
        return status;
    }

    public int getLimit() {
        return limit;
    }
}
