package edu.java.errors;

public class ChatAlreadyExistsException extends RuntimeException{
    public ChatAlreadyExistsException(String message){
        super(message);
    }
}
