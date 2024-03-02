package me.hol22mol22.javatestlearn.study;

public class Study {

    private StudyStatus status = StudyStatus.DRAFT;

    private int limit;

    public Study(int limit){
        if(limit < 0){
            throw new IllegalArgumentException("limit 은 0이상이어야함");
        }
        this.limit = limit;
    }

    public StudyStatus getStatus(){
        return status;
    }

    public int getLimit() {
        return limit;
    }
}
