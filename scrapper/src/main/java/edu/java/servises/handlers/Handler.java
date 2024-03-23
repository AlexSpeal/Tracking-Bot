package edu.java.servises.handlers;

import java.net.URI;

public interface Handler<T> {
    String getData(T dto);

    T getInfoByUrl(URI url);
}
