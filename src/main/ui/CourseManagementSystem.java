package ui;

import model.Course;
import model.exceptions.AlreadyExists;
import model.exceptions.NullCourseException;
import model.HomeWork;
import model.Student;
import model.exceptions.NullHomeWorkException;
import model.exceptions.TooLongDuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Console based ui for the course management system where 1 student can manage his courses by seeing
// and managing their schedule
public class CourseManagementSystem {
    private Student student;
    private Scanner scanner;
    private boolean keepRunning;

    //EFFECTS: creates a course management app with a student name and then
    //         runs the program's functions options
    public CourseManagementSystem() {
        keepRunning = true;
        runMain();
    }

    //MODIFIES: student [name]
    //EFFECTS: inputs the name of the student given then displays the menu
    private void runMain() {
        System.out.println("Welcome to the Course management System, please enter your name: ");

        scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        student = new Student(name);
        displayMenu();
    }

    //MODIFIES: scanner
    //EFFECTS: displays the options available to choose for the user and
    //          takes the input and passes it to options
    private void displayMenu() {
        while (keepRunning) {
            System.out.println("Choose one of the following options: " + "\n"
                    + "\t" + "c for adding a course" + "\n"
                    + "\t" + "h for adding a homework for a  specific course" + "\n"
                    + "\t" + "s for displaying your schedule" + "\n"
                    + "\t" + "g to get a course grade" + "\n"
                    + "\t" + "d for changing the status of a homework" + "\n"
                    + "\t" + "l for changing the grade of a homework" + "\n"
                    + "\t" + "anything else to quit");
            scanner = new Scanner(System.in);
            String optionChosen = scanner.nextLine();
            optionChosen = optionChosen.toLowerCase();
            options(optionChosen);
        }
    }

    //EFFECTS:   Chooses the options and then passes to the corresponding method
    //           Also for AlreadyExits courses or homeworks error displays the error
    //           quits the program for any other responses
    private void options(String optionChosen) {
        if (optionChosen.equals("c")) {
            try {
                addCourseForStudent();
            } catch (AlreadyExists e) {
                System.out.println("This already exists");
            }
        } else if (optionChosen.equals("h")) {
            try {
                addHomeWorkForStudent();
            } catch (AlreadyExists e) {
                System.out.println("This already exists");
            }
        } else if (optionChosen.equals("s")) {
            displaySchedule();
        } else if (optionChosen.equals("g")) {
            showCourseGrade();
        } else if (optionChosen.equals("d")) {
            changeStatus();
        } else if (optionChosen.equals("l")) {
            changeHomeWorkGrade();
        } else {
            keepRunning = false;
        }
    }

    //MODIFIES: student [listOfCourses]
    //EFFECTS: if !existsAlready adds the course with the name to the students
    //          else throws AlreadyExists error
    private void addCourseForStudent() throws AlreadyExists {
        System.out.println("add the name of the course [not case sensitive] ");
        String name = scanner.nextLine();
        name = name.toLowerCase();
        try {
            Course course = new Course(name);
            student.addCourse(course);
        } catch (AlreadyExists e) {
            throw new AlreadyExists();
        }

    }

    //EFFECTS: gets the course through the getCourse method
    //         if catches NullCourseException, TooLongDuration, AlreadyExists shows the corresponding message
    //         sends the chosen course to the checkIfHomeWorkAlreadyInCourse
    private void addHomeWorkForStudent() throws AlreadyExists {
        System.out.println("you will have to add some details about the course");
        try {
            Course courseChosen = getCourse();
            checkIfHomeWorkalreadyInCourse(courseChosen);
        } catch (NullCourseException e) {
            System.out.println("you are currently not registered in this section");
        } catch (TooLongDuration e) {
            System.out.println("You should not have more than 20 hours of work in 1 day, split your work :)");
        } catch (AlreadyExists e) {
            throw new AlreadyExists();
        }
    }

    //EFFECTS: gets the name of the homeworks and passes the course and
    //          the name of the homework to the addHomeWorkInSchedule
    private void checkIfHomeWorkalreadyInCourse(Course courseChosen) throws TooLongDuration, AlreadyExists {
        System.out.println("Name of the homework? [not case sensitive] ");
        String name = scanner.nextLine();

        try {
            addHomeWorkInSchedule(courseChosen, name);

        } catch (TooLongDuration e) {
            throw new TooLongDuration();

        }

    }

    //MODIFIES: student
    //EFFECTS: takes the course chosen and name and then takes the input of
    //          date, duration and weighing of the homework and then creates the homework
    //          if (!catches any exception), adds the homework to the given course
    //          if catches AlreadyExists or TooLongDuration, throws it
    private void addHomeWorkInSchedule(Course courseChosen, String name) throws TooLongDuration, AlreadyExists {
        System.out.println("add the following details for the code"
                + "\n" + "startDate = d/mm/yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        String startDate = scanner.nextLine();
        LocalDate localDate = LocalDate.parse(startDate, formatter);
        System.out.println("duration (hours) [give a whole number]");
        int duration = scanner.nextInt();
        if (duration > 20) {
            throw new TooLongDuration();
        }
        System.out.println("weighing of this homework (%) ");
        double weighing = scanner.nextDouble();
        HomeWork homeWork  = new HomeWork(name, localDate, courseChosen.getCourseName(), duration, weighing);
        try {
            courseChosen.addHomeWork(homeWork);
            student.addHomeWorkToSchedule(homeWork);
        } catch (TooLongDuration e) {
            throw new TooLongDuration();
        } catch (AlreadyExists e) {
            throw new AlreadyExists();

        }
    }

    //EFFECTS: displays the schedule with the date and then the homeworks (courseName) corresponding to it
    private void displaySchedule() {
        System.out.println("\n");
        LinkedHashMap<LocalDate, List<HomeWork>> schedule = student.getSchedule();
        Set<LocalDate> localDates = schedule.keySet();
        for (LocalDate localDate : localDates) {
            List<HomeWork> list = schedule.get(localDate);
            String listString = "";
            for (HomeWork hwk : list) {
                listString = listString + " " + hwk.getName() + " (" + hwk.getCourse() + "), ";
            }
            System.out.println(localDate + " : " + listString);
        }
        System.out.println("\n");
    }

    //EFFECTS: takes the input of the course,
    //        goes through the list of courses of the student and returns the course with the same name
    //        if no course found, throws NullCourseException
    private Course getCourse() throws NullCourseException {
        System.out.println("name of the course? [not case sensitive] ");
        Course courseChosen = null;
        String name = scanner.nextLine();
        name = name.toLowerCase();
        List<Course> courseList = student.getListOfCourses();
        for (Course course : courseList) {
            String courseName = course.getCourseName();
            courseName.toLowerCase();
            if (courseName.equals(name)) {
                courseChosen = course;
            }
        }
        if (courseChosen == null) {
            throw new NullCourseException();
        }
        return courseChosen;
    }

    //EFFECTS: gets the name of the course and then returns the grade of the course
    //          if catches NullCourseException, displays that the user is not registered in the course
    public void showCourseGrade() {
        try {
            Course course = getCourse();
            System.out.println(course.getGrade());
        } catch (NullCourseException e) {
            System.out.println("you are not registered in this course currently");
        }

    }


    //EFFECTS: takes the the course, and then takes the input of the homework
    //        goes through the list of homeworks in the courses of the student and
    //        then returns the course with the same name
    //        if no homework is found, throws NullHomeWorkException
    public HomeWork getHomeWork(Course course) throws NullHomeWorkException {
        System.out.println("which homework status do you want? [not case sensitive] ");
        HomeWork homeworkChosen = null;
        String name = scanner.nextLine();
        name = name.toLowerCase();
        List<HomeWork> hwkList = course.getHomeworks();
        for (HomeWork hwk : hwkList) {
            String hwkName = hwk.getName().toLowerCase();
            if (hwkName.equals(name)) {
                homeworkChosen = hwk;
            }
        }
        if (homeworkChosen == null) {
            throw new NullHomeWorkException();
        }
        return homeworkChosen;
    }

    //MODIFIES: student [homework]
    //EFFECTS: gets the course and the homeworks and then changes the status of the homework
    //          [from true and vice versa] and then displays whether that homework is "done" or "incomplete"
    private void changeStatus() {
        try {
            Course course = getCourse();
            HomeWork hwk = getHomeWork(course);
            hwk.changeStatus();
            System.out.println("Changing the status of " + hwk.getName() + "to " + hwk.getStatus());
        } catch (NullCourseException e) {
            System.out.println("you are not registered in this course currently");
        } catch (NullHomeWorkException e) {
            System.out.println("you have no homework with that name, do not lie :(");
        }
    }

    //MODIFIES: student [homework]
    //EFFECTS: gets the course and the homeworks and then changes the grade of that homeworks
    //          displays the message that grade of the homework is changed and then the current
    //          progress of the course
    //          if catches NullCourseExceptions, NullHomeWorkExceptions, shows the message
    private void changeHomeWorkGrade() {
        try {
            Course course = getCourse();
            HomeWork hwk = getHomeWork(course);

            System.out.println("What grade do you want to give this homework [give a whole number]");
            int grade = scanner.nextInt();
            hwk.setGrade(grade);
            //progress report
            System.out.println("You have changed " + hwk.getName() + " grade to " + hwk.getGrade()
                    + "\n" + "you current progress in " + course.getCourseName() + "is " + course.getGrade());

        } catch (NullCourseException e) {
            System.out.println("you are not registered in this course currently");
        } catch (NullHomeWorkException e) {
            System.out.println("you have no homework with that name, do not lie :(");
        }
    }

}
