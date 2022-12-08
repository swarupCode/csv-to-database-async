package com.swarudas.csvtodatabase.service;

import com.swarudas.csvtodatabase.entity.UserDemo;
import com.swarudas.csvtodatabase.repository.UserDemoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserDemoService {

    @Autowired
    private UserDemoRepository repository;

    private final Logger logger = LoggerFactory.getLogger(UserDemoService.class);

    @Async
    public CompletableFuture<List<UserDemo>> saveUsers(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();

        List<UserDemo> userDemoList = parseCSVFile(file);
        logger.info("Saving list of Users of size {}; {}", userDemoList.size(), Thread.currentThread().getName());
        repository.saveAll(userDemoList);
        long end = System.currentTimeMillis();
        logger.info("Total time taken: {}", (end - start));

        return CompletableFuture.completedFuture(userDemoList);
    }

    @Async
    public CompletableFuture<List<UserDemo>> fetchUsers() {
        logger.info("Get list of Users by {}", Thread.currentThread().getName());
        List<UserDemo> users = repository.findAll();

        return CompletableFuture.completedFuture(users);
    }

    private List<UserDemo> parseCSVFile(final MultipartFile file) throws Exception {
        final List<UserDemo> users = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final UserDemo user = new UserDemo();
                    user.setName(data[0]);
                    user.setEmail(data[1]);
                    user.setGender(data[2]);
                    users.add(user);
                }
                return users;
            }
        } catch (final IOException e) {
            logger.error("Failed to parse CSV file {}", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }

}
