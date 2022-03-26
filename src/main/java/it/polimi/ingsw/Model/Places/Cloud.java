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
    private final int NUMMAXSTUDENTS;
    private List<Student> students;

    public Cloud(int numMaxStudents) {
        this.NUMMAXSTUDENTS = numMaxStudents;
        this.students = new ArrayList<Student>();
    }

    //TODO: maybe in this case is better to use the toString instead of passing the exact objects
    //this method is to show the elements inside the cloud
    public List<Student> getStudents(){
        List<Student> got = new ArrayList<Student>();

        got.addAll(this.students);

        return got;
    }

    @Override
    public String toString() {
        return "Cloud{" +
                "students=" + students +
                '}';
    }

    // If we want to pass all 3 or 4 students in one step. We may decide to pass one per time
    public void fill(List<Student> toAdd) throws ExceededMaxStudentsCloudException {
        if ((this.students.size() + toAdd.size()) <= NUMMAXSTUDENTS){
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
