package me.ildarorama.module3.task5;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.ildarorama.module3.task5.model.UserBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.logging.Logger;

public class UserDao {
    private static final Logger log = Logger.getLogger(UserDao.class.getName());
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public UserBean getUserById(long id) {
        try (var reader = Files.newBufferedReader(getUserFilePath(id))) {
            return gson.fromJson(reader, UserBean.class);
        } catch (IOException e) {
            log.warning("Can not fetch user data");
            throw new RuntimeException("Can not fetch user data", e);
        }
    }

    public void saveUser(UserBean user) {
        try (var writer = Files.newBufferedWriter(getUserFilePath(user.getId()), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
            writer.write(gson.toJson(user));
        } catch (Exception e) {
            log.warning("Can not save user data");
            throw new RuntimeException("Can not save user data", e);
        }
    }

    private Path getUserFilePath(long id) {
        return Path.of(id + ".json");
    }
}
