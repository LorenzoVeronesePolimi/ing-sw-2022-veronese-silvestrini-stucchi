package it.polimi.ingsw.Model.Places;

import it.polimi.ingsw.Model.Exceptions.ExceededMaxStudentsCloudException;
import it.polimi.ingsw.Model.Pawns.Student;

import java.util.ArrayList;
import java.util.List;

/*
* Always add all Students
* Always remove all Students
* */

/**
 * This class represents a cloud in the game. Each cloud contains a group of students. Removing them means emptying
 * the cloud, while adding them means filling completely the cloud. The cloud has NUMMAXSTUDENTS or zero students on
 * itself.
 */
public class Cloud {
    private final int NUMMAXSTUDENS;
    private final List<Student> students;

    /**
     * This method initialize the constant representing the number of students that can be contained by a cloud.
     * @param numMaxStudents maximum number of students contained in a cloud.
     */
    public Cloud(int numMaxStudents) {
        this.NUMMAXSTUDENS = numMaxStudents;
        this.students = new ArrayList<>();
    }

    /**
     * @return the list of students present on the cloud.
     */
    //this method is to show the elements inside the cloud
    public List<Student> getStudents(){
        return new ArrayList<>(this.students);
    }

    /**
     * @return the maximum number of students that can be contained on an island.
     */
    public int getNumMaxStudents() {
        return NUMMAXSTUDENS;
    }

    /**
     * This method fills the cloud with the list of students received by parameter, only if the cloud is empty.
     * The number of students added depends on the type of match played.
     * @param toAdd List of students that need to be added to the cloud.
     * @throws ExceededMaxStudentsCloudException when there are already students on the cloud.
     */
    // If we want to pass all 3 or 4 students in one step. We may decide to pass one per time
    public void fill(List<Student> toAdd) throws ExceededMaxStudentsCloudException {
        if ((this.students.size() + toAdd.size()) <= this.NUMMAXSTUDENS){
            this.students.addAll(toAdd);
        } else {
            throw new ExceededMaxStudentsCloudException();
        }
    }

    /**
     * @return the students on the cloud, removing them.
     */
    //This method gives a list of students and deletes the current students in the cloud
    public List<Student> empty() {
        List<Student> removed = new ArrayList<>(this.students);

        this.students.clear();

        return removed;
    }

    /**
     * Method toString of the structure of the class.
     * @return The description of the class.
     */
    @Override
    public String toString() {
        return "Cloud{" +
                "students=" + students +
                '}';
    }
}
