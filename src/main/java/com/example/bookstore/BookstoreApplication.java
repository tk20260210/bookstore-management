package com.example.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BookstoreApplication {

	public static void main(String[] args) {

        //Debug
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String hash = encoder.encode("password");
//        System.out.println("★ 生成ハッシュ: " + hash);
//        System.out.println("★ 検証結果: " + encoder.matches("password", hash));

		SpringApplication.run(BookstoreApplication.class, args);
	}

}
