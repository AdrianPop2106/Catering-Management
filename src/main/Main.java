package main;

import presentation_layer.EmployeePage;
import presentation_layer.Login;

public class Main {
    public static void main(String[] args){
        new Login(new EmployeePage());
    }
}
