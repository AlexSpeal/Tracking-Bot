package edu.java.bot.user;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class UsersBase {
    private List<User> userBase = new ArrayList<>();

    public boolean containId(Long user) {
        for (User value : userBase) {
            if (value.getId().equals(user)) {
                return true;
            }
        }
        return false;
    }

    public User getUser(Long id) {
        for (User value : userBase) {
            if (value.getId().equals(id)) {
                return value;
            }
        }
        return null;
    }
}
