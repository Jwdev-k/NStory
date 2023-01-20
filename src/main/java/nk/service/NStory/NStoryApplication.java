package nk.service.NStory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class NStoryApplication {
	public static void main(String[] args) {
		SpringApplication.run(NStoryApplication.class, args);
	}
}
