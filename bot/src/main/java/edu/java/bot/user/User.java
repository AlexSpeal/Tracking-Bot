package edu.java.bot.user;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private final String name;
    private final Long id;
    private List<String> sites = new ArrayList<>();
    private State state;

    public User(String name, Long id, List<String> sites) {
        this.name = name;
        this.id = id;
        this.sites = sites;
        this.state = new State(State.EnumState.NONE);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        if (!sites.isEmpty()) {
            for (int i = 0; i < sites.size(); ++i) {
                result.append((i + 1)).append(". ").append(sites.get(i)).append("\n\n");
            }

        } else {
            result = new StringBuilder("Список отслеживаемых ссылок пуст!");
        }
        return result.toString();
    }
}
