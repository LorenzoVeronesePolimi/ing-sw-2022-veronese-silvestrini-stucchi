package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.Enumerations.SPColour;
import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsCloudException;
import it.polimi.ingsw.Model.Pawns.Student;

import java.util.ArrayList;
import java.util.List;

/*
* Always add all Students
* Always remove all Students
* */
public class Cloud {
    private final int numMaxStudents;
    private List<Student> students;

    public Cloud(int numMaxStudents) {
        this.numMaxStudents = numMaxStudents;
        this.students = new ArrayList<Student>();
    }

    public List<Student> getStudents(){
        List<Student> got = new ArrayList<Student>();

        got.addAll(this.students);

        return got;
    }

    // If we want to pass all 3/4 students in one step. We may decide to pass one per time
    public void fill(List<Student> toAdd) throws ExceededMaxStudentsCloudException {
        if (this.students.size() == 0){
            this.students.addAll(toAdd);
        } else {
            throw new ExceededMaxStudentsCloudException();
        }
    }

    //This method gives a list of students and deletes the current students in the cloud
    public List<Student> empty() {
        List<Student> removed = new ArrayList<Student>();

        removed.addAll(this.students);
        this.students.clear();

        return removed;
    }
}
