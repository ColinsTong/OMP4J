package com.omp4j.omp;

import com.omp4j.commands.*;
import com.proc.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Brent Jacobs
 */
public class Client {
    
    private final String username, password, host, port;
    
    public Client(String username, String password, String host, String port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }
    
    public Client(String username, String password) {
        this(username, password, "localhost", "9390");
    }
    
    private String ompCommand() {
        return " omp";
    }
    
    private String connectionParameters() {
        return "-u " + username + " -w " + password + " -h " + host + " -p " + port;
    }
    
    public Proc getCommand(String command) {
        try {
            String cmd = ompCommand() + " " + connectionParameters() + " " + command;
            byte[] arr = cmd.getBytes();
            return new Proc(new String(arr, "UTF8"));
        } catch (UnsupportedEncodingException ex) {
            System.out.println("Unable to convert to UTF8");
        }
        return null;
    }
    
    public String getConfigs() throws IOException, InterruptedException {
        Proc cmd = getCommand("--xml='" + new GetConfigs() + "' -i");
        

        
        
        return cmd.exec();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String... args) {
        try {
            Client omp = new Client("admin", "password");
            
            
            Proc proc = new Proc("nmap -sP 10.0.0.*");
            proc.addListener(omp.createListener());
            proc.exec();
            
            
            
            omp.testCommands();
            
            
            
            
        } catch (IOException ex) {
            System.out.println("Encountered an IOException:");
            System.out.println(ex);
        } catch (InterruptedException ex) {
            System.out.println("Encountered an InterruptedException:");
            System.out.println(ex);
        }
    }
    
    
    public void testCommands() throws IOException, InterruptedException {
        Proc cmd;
        
        System.out.println("===  new GetConfigs() ================================================".substring(0, 65));
        cmd = getCommand("--xml='" + new GetConfigs() + "' -i");
        System.out.println("command:");
        System.out.println(cmd.getCommand());
        cmd.addListener(createListener());
        cmd.exec();
        System.out.println();System.out.println();System.out.println();
        
        
        System.out.println("===  '<get_configs />' ===============================================".substring(0, 65));
        cmd = getCommand("--xml='<get_configs />' -i");
        System.out.println("command:");
        System.out.println(cmd.getCommand());
        cmd.addListener(createListener());
        cmd.exec();
        System.out.println();System.out.println();System.out.println();
        
        System.out.println("===  \"<get_configs />\".replaceAll(\"\\\"\", \"\") ===============================================".substring(0, 65));
        cmd = getCommand("--xml=\"<get_configs />\" -i".replaceAll("\"", ""));
        System.out.println("command:");
        System.out.println(cmd.getCommand());
        cmd.addListener(createListener());
        cmd.exec();
        System.out.println();System.out.println();System.out.println();
        
        System.out.println("===  \"<get_configs />\" =============================================".substring(0, 65));
        cmd = getCommand("--xml=\"<get_configs />\" -i");
        System.out.println("command:");
        System.out.println(cmd.getCommand());
        cmd.addListener(createListener());
        cmd.exec();
        System.out.println();System.out.println();System.out.println();
        
        System.out.println("===  \\\"<get_configs />\\\" =========================================".substring(0, 65));
        cmd = getCommand("--xml=\\\"<get_configs />\\\" -i");
        System.out.println("command:");
        System.out.println(cmd.getCommand());
        cmd.addListener(createListener());
        cmd.exec();
        System.out.println();System.out.println();System.out.println();
        
        
//        System.out.println("===   ================================================================".substring(0, 65));
        
        
//        System.out.println("===   ================================================================".substring(0, 65));
        
        
    }
    
    public ProcListener createListener() {
        return new ProcListener() {
            @Override
            public void start() {
                System.out.println("listener: start");
            }
            
            @Override
            public void line(String line) {
                System.out.println("line: " + line);
            }
            
            @Override
            public void errorLine(String error) {
                System.out.println("error: " + error);
            }
            
            @Override
            public void finished(String output, String error, int exitValue) {
                System.out.println("listener: finished");
                System.out.println("has output? " + !(output == null || output.equals("")));
                System.out.println("has errors? " + !(error == null || error.equals("")));
                System.out.println("exitValue: " + exitValue);
            }
        };
    }
    
}
