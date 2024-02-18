package edu.java.bot.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class State {

    public State(EnumState nowState) {

        this.nowState = nowState;
    }

    public enum EnumState {
        NONE, FILE_ADD, FILE_DEL
    }

    private EnumState nowState;

}
