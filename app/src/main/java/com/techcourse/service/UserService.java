package com.techcourse.service;

import com.interface21.transaction.TransactionManager;
import com.techcourse.dao.UserDao;
import com.techcourse.dao.UserHistoryDao;
import com.techcourse.domain.User;
import com.techcourse.domain.UserHistory;
import java.sql.Connection;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserService {

    private final UserDao userDao;
    private final UserHistoryDao userHistoryDao;

    public UserService(UserDao userDao, UserHistoryDao userHistoryDao) {
        this.userDao = userDao;
        this.userHistoryDao = userHistoryDao;
    }

    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    public void insert(final User user) {
        userDao.insert(user);
    }

    public void changePassword(Connection connection, long id, String newPassword, String createBy) {
        TransactionManager.transaction(connection, () -> {
            User user = findById(id).orElseThrow(NoSuchElementException::new);
            user.changePassword(newPassword);
            userDao.update(connection, user);
            userHistoryDao.log(new UserHistory(user, createBy));
        });
    }
}
