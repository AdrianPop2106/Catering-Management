package presentation_layer;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Clasa RegisterPage este folosita pentru realziara unei ferestre grafice prin care un utilizator se poate inregistra intr-unul
 * din rolurile de client, administrator sau angajat. Daca indformatia este corecta, aceasta va fi scrisa intr-unul dintre
 * fisierele "adminsitrator.txt", "client.txt" sau "employee.txt"
 */
public class RegisterPage extends JFrame {

    JTextField username;
    JTextField password;

    /**
     * Se va creea fereastra ce va permite scrierea unui username, a unei parole si de bifare a rolului utilizatorului
     */
    RegisterPage(EmployeePage p){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(240,290);

        JLabel log=new JLabel("Register");
        log.setFont(new Font("Serif", Font.PLAIN,24));

        JPanel name=new JPanel();
        JPanel passwords=new JPanel();
        JPanel writer=new JPanel();
        JPanel buttons=new JPanel();
        JPanel radio=new JPanel();

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

        JRadioButton client=new JRadioButton("Client");
        JRadioButton employee=new JRadioButton("Employee");
        JRadioButton administrator=new JRadioButton("Administrator");

        ButtonGroup bg=new ButtonGroup();
        bg.add(client);
        bg.add(employee);
        bg.add(administrator);

        client.setAlignmentX(Component.LEFT_ALIGNMENT);
        employee.setAlignmentX(Component.LEFT_ALIGNMENT);
        administrator.setAlignmentX(Component.LEFT_ALIGNMENT);

        radio.add(client);
        radio.add(employee);
        radio.add(administrator);

        radio.setLayout(new BoxLayout(radio,BoxLayout.Y_AXIS));

        JButton register=new JButton("Save");
        JButton back=new JButton("Back");
        buttons.add(register);
        register.addActionListener(e -> {
            if(username.getText().split(" ").length==2 && (client.isSelected() || employee.isSelected() || administrator.isSelected())){
                new Login(p);
                FileWriter w;
                try {
                    if(client.isSelected()){
                        w=new FileWriter("client.txt",true);
                        w.append(username.getText()+" "+password.getText()+"\n");
                        w.close();
                    }
                    else if(employee.isSelected()){
                        w=new FileWriter("employee.txt",true);
                        w.append(username.getText()+" "+password.getText()+"\n");
                        w.close();
                    }
                    else if(administrator.isSelected()){
                        w=new FileWriter("administrator.txt",true);
                        w.append(username.getText()+" "+password.getText()+"\n");
                        w.close();
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                this.dispose();
            }
        });
        buttons.add(back);
        back.addActionListener(e -> {
            new Login(p);
            this.dispose();
        });

        this.getContentPane().add(log);
        this.getContentPane().add(Box.createRigidArea(new Dimension(1,5)));
        this.getContentPane().add(writer);
        this.getContentPane().add(radio);
        this.getContentPane().add(buttons);
        this.getContentPane().add(Box.createRigidArea(new Dimension(1,10)));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        log.setAlignmentX(Component.CENTER_ALIGNMENT);
        writer.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.setVisible(true);
    }
}
