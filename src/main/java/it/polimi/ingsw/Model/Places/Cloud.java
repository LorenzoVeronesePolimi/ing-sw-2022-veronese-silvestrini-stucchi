package it.polimi.ingsw.Model.Places;

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
    private final List<Student> students;

    public Cloud(int numMaxStudents) {
        this.numMaxStudents = numMaxStudents;
        this.students = new ArrayList<>();
    }

    //TODO: maybe in this case is better to use the toString instead of passing the exact objects
    //this method is to show the elements inside the cloud
    public List<Student> getStudents(){
        return new ArrayList<>(this.students);
    }

    public int getNumMaxStudents() {
        return numMaxStudents;
    }

    @Override
    public String toString() {
        return "Cloud{" +
                "students=" + students +
                '}';
    }

    // If we want to pass all 3 or 4 students in one step. We may decide to pass one per time
    public void fill(List<Student> toAdd) throws ExceededMaxStudentsCloudException {
        if ((this.students.size() + toAdd.size()) <= this.numMaxStudents){
            this.students.addAll(toAdd);
        } else {
            throw new ExceededMaxStudentsCloudException();
        }
    }

    //This method gives a list of students and deletes the current students in the cloud
    public List<Student> empty() {
        List<Student> removed = new ArrayList<>(this.students);

        this.students.clear();

        return removed;
    }
}
