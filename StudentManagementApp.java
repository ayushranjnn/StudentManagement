import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Student implements Serializable {
    private String name;
    private int rollNumber;
    private String grade;

    public Student(String name, int rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", rollNumber=" + rollNumber +
                ", grade='" + grade + '\'' +
                '}';
    }
}

class StudentManagementSystem {
    private List<Student> students;

    public StudentManagementSystem() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(int rollNumber) {
        students.removeIf(student -> student.getRollNumber() == rollNumber);
    }

    public Student searchStudent(int rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber() == rollNumber) {
                return student;
            }
        }
        return null;
    }

    public List<Student> getAllStudents() {
        return students;
    }
}

class StudentDataStorage {
    public static void saveStudentsToFile(List<Student> students, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Student> loadStudentsFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

public class StudentManagementApp {
    private static final String FILE_NAME = "students.dat";

    public static void main(String[] args) {
        StudentManagementSystem sms = new StudentManagementSystem();
        List<Student> loadedStudents = StudentDataStorage.loadStudentsFromFile(FILE_NAME);
        if (loadedStudents != null) {
            for (Student student : loadedStudents) {
                sms.addStudent(student);
            }
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nStudent Management System");
            System.out.println("1. Add Student");
            System.out.println("2. Remove Student");
            System.out.println("3. Search Student");
            System.out.println("4. Display All Students");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    while (name.isEmpty()) {
                        System.out.print("Name cannot be empty. Enter name: ");
                        name = scanner.nextLine();
                    }

                    System.out.print("Enter roll number: ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("Invalid roll number. Enter roll number: ");
                        scanner.next(); // consume invalid input
                    }
                    int rollNumber = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    System.out.print("Enter grade: ");
                    String grade = scanner.nextLine();
                    while (grade.isEmpty()) {
                        System.out.print("Grade cannot be empty. Enter grade: ");
                        grade = scanner.nextLine();
                    }

                    sms.addStudent(new Student(name, rollNumber, grade));
                    StudentDataStorage.saveStudentsToFile(sms.getAllStudents(), FILE_NAME);
                    break;
                case 2:
                    System.out.print("Enter roll number to remove: ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("Invalid roll number. Enter roll number: ");
                        scanner.next(); // consume invalid input
                    }
                    int rollToRemove = scanner.nextInt();
                    sms.removeStudent(rollToRemove);
                    StudentDataStorage.saveStudentsToFile(sms.getAllStudents(), FILE_NAME);
                    break;
                case 3:
                    System.out.print("Enter roll number to search: ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("Invalid roll number. Enter roll number: ");
                        scanner.next(); // consume invalid input
                    }
                    int rollToSearch = scanner.nextInt();
                    Student student = sms.searchStudent(rollToSearch);
                    if (student != null) {
                        System.out.println(student);
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;
                case 4:
                    System.out.println("All Students:");
                    for (Student s : sms.getAllStudents()) {
                        System.out.println(s);
                    }
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    StudentDataStorage.saveStudentsToFile(sms.getAllStudents(), FILE_NAME);
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
