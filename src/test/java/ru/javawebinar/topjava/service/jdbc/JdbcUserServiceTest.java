package ru.javawebinar.topjava.service.jdbc;

import org.junit.Assume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
    @Autowired
    private Environment env;

    @Override
    public void setUp() {
        if(!env.acceptsProfiles(activeProfiles -> activeProfiles.test(JDBC))) {
            throw new RuntimeException("Profile is inactive!");
        }
    }

    @Override
    public void createWithException() {
        Assume.assumeTrue("JDBC doesn't support hibernate",true);
    }
}