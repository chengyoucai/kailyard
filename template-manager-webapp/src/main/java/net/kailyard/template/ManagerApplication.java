package net.kailyard.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class ManagerApplication {
    @RequestMapping("/tt1")
    public String tt() {
        return "test222";
    }

    public static void main( String[] args ) throws Exception {
        System.out.println( "Hello World ! App!" );
        try{
            SpringApplication.run(ManagerApplication.class, args);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
