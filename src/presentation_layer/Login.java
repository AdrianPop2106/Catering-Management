package presentation_layer;

import bussiness_layer.MenuItem;
import bussiness_layer.Order;
import data_layer.Serializator;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Clasa Login va creea o fereastra prin care utilizatorul se va putea loga ca unul dintre cele 3 tipuri de utilizatori:
 * client, angajat sau administrator. Aceasta clasa are 2 atribute de tip JTextField: username si password
 */
public class Login extends JFrame {
    JTextField username;
    JTextField password;
    EmployeePage employee;

    /**
     * Constructorul va creea fereastra prin care utilizatorul va putea introduce un username si o parola. Daca aceste
     * informatii corespund cu ceea ce este scris intr-unul din fisierele ”administrator.txt”, ”client.txt” sau
     * ”employee.txt”, atunci la apasarea butonului ”Accept” se va deschide o fereastra corespunzatoare tipului de utilizator.
     * De asemenea se poate deschide o fereastra de registrare pentru crearea unui nou utilizator
     */
    public Login(EmployeePage p){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(240,170);

        employee= p;

        JLabel log=new JLabel("Login");
        log.setFont(new Font("Serif", Font.PLAIN,24));

        JPanel name=new JPanel();
        JPanel passwords=new JPanel();
        JPanel writer=new JPanel();
        JPanel buttons=new JPanel();

        JLabel user=new JLabel("Username");
        JLabel pass=new JLabel("Password");
        name.add(user);
        passwords.add(pass);
        setLocationRelativeTo(null);

        username=new JTextField(10);
        password=new JTextField(10);
        name.add(username);
        passwords.add(password);
        writer.add(name);
        writer.add(passwords);
        writer.setLayout(new GridLayout(2,1));

        JButton accept=new JButton("Accept");
        accept.addActionListener(e -> {
            short ind=0;
            boolean ok=false;
            if(checkExistence("client.txt")){
                ok=true;
                ind=1;
            }
            if(checkExistence("employee.txt") && !ok){
                ok=true;
                ind=2;
            }
            if(checkExistence("administrator.txt") && !ok){
                ok=true;
                ind=3;
            }
            if(ok){
                if(ind==1)
                    new ClientPage(username.getText(),employee);
                else if(ind==2)
                    employee.login();
                else
                    new AdministratorPage(employee);
                this.setVisible(false);
            }
            else JOptionPane.showMessageDialog(new JFrame(),"Username or password incorrect!");
        });
        JButton register=new JButton("Register");
        buttons.add(accept);
        register.addActionListener(e -> {
            new RegisterPage(employee);
            this.dispose();
        });
        buttons.add(register);

        this.getContentPane().add(log);
        this.getContentPane().add(Box.createRigidArea(new Dimension(1,5)));
        this.getContentPane().add(writer);
        this.getContentPane().add(buttons);
        this.getContentPane().add(Box.createRigidArea(new Dimension(1,10)));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        log.setAlignmentX(Component.CENTER_ALIGNMENT);
        writer.setAlignmentX(Component.CENTER_ALIGNMENT);
        accept.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setVisible(true);

    }

    /**
     * Metoda va primii ca si parametru un obiect Scanner s care este folosit pentru a citii un rand din fisierul
     * lui s si returna o valoare booleana in functie de exactitatea dintre informatiile citite si cele scrise in
     * obiectele JTextField
     * @param s Scanner folosit pentru citirea din fisier
     * @return boolean
     */
    private boolean checkUser(Scanner s){
        String temp=s.nextLine();
        String[] info=temp.split(" ");
        return username.getText().equals(info[0] + " " + info[1]) && password.getText().equals(info[2]);
    }

    /**
     * Metoda va primii ca si parametru un String filename ce reprezinta calea spre un fisier si va returna true
     * daca unul dintre apelurile metodei checkUser returneaza true, respectiv false in caz contrar
     * @param filename String calea fisierului din care se citeste
     * @return boolean
     */
    private boolean checkExistence(String filename){
        Scanner s= null;
        try {
            s = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(s.hasNextLine())
            if(checkUser(s))
                return true;
        return false;
    }
}
