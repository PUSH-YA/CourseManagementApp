package ui;

import model.Student;
import persistence.JsonReader;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        Student student = null;
        try {
            JsonReader reader = new JsonReader("./data/pushya.json");
            student = reader.read();
        } catch (IOException e) {
            System.out.println("where");
        }

        AskNameUI name =  new AskNameUI();
        //yellow colour is 253,231,166
        //grey is 64,64,64


    }
}
