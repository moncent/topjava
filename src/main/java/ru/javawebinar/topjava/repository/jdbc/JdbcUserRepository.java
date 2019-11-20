package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
//        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        List<Role> userRoles = new ArrayList<>(user.getRoles());

        if (user.isNew()) {
//            Number newKey = insertUser.executeAndReturnKey(parameterSource);
//            user.setId(newKey.intValue());
             jdbcTemplate.batchUpdate("INSERT INTO users (name, email, password, registered, enabled, calories_per_day) VALUES (?,?,?,?,?,?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, user.getPassword());
                    ps.setTimestamp(4, Timestamp.from(user.getRegistered().toInstant()));
                    ps.setBoolean(5, user.isEnabled());
                    ps.setInt(6, user.getCaloriesPerDay());
                }

                @Override
                public int getBatchSize() {
                    return 1;
                }
            });

            Integer id  = getByEmail(user.getEmail()).getId();
            user.setId(id);
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?,?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, id);
                    ps.setString(2, userRoles.get(i).name());
                }

                @Override
                public int getBatchSize() {
                    return userRoles.size();
                }
            });

        } else {
            jdbcTemplate.batchUpdate("UPDATE users" +
            " SET name=?, email=?, password=?, " +
            "registered=?, enabled=?, calories_per_day=? WHERE id=?", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getEmail());
                    ps.setString(3, user.getPassword());
                    ps.setTimestamp(4, Timestamp.from(user.getRegistered().toInstant()));
                    ps.setBoolean(5, user.isEnabled());
                    ps.setInt(6, user.getCaloriesPerDay());
                    ps.setInt(7, user.getId());
                }

                @Override
                public int getBatchSize() {
                    return 1;
                }
            });
            jdbcTemplate.batchUpdate("UPDATE user_roles SET role = ? WHERE user_id = ?", new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, userRoles.get(i).name());
                        ps.setInt(2, user.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return userRoles.size();
                    }
                });
         }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
//        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        List<User> users = jdbcTemplate.query("SELECT id, name, email, password, registered, enabled, calories_per_day, ARRAY(select role from user_roles where user_id = id) as roles FROM users WHERE id=?", this::setUserWithRoles, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT id, name, email, password, registered, enabled, calories_per_day, ARRAY(select role from user_roles where user_id = id) as roles FROM users WHERE email=?", this::setUserWithRoles, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT id, name, email, password, registered, enabled, calories_per_day, ARRAY(select role from user_roles where user_id = id) as roles FROM users ORDER BY name, email", this::setUserWithRoles);
    }

    private List<User> setUserWithRoles(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setRegistered(rs.getTimestamp("registered"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setCaloriesPerDay(rs.getInt("calories_per_day"));

            String[] roleArr = (String[]) rs.getArray("roles").getArray();
            Set<Role> roles = new HashSet<>(Arrays.asList(roleArr)).stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
            users.add(user);
        }
        return users;
    }
}
