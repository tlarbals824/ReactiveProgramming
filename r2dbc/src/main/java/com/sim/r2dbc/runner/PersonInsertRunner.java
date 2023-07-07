package com.sim.r2dbc.runner;

import com.sim.r2dbc.common.repository.UserEntity;
import com.sim.r2dbc.repository.UserR2dbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

@Slf4j
@RequiredArgsConstructor
//@Component
public class PersonInsertRunner implements CommandLineRunner {

    private final UserR2dbcRepository userR2dbcRepository;

    @Override
    public void run(String... args) throws Exception {
        UserEntity sim = new UserEntity("sim", 24, "1", "1q2w3e4r!");
        UserEntity savedUser = userR2dbcRepository.save(sim).block();
        log.info("savedUser: {}", savedUser);
    }
}
