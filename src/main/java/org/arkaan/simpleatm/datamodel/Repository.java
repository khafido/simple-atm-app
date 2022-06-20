package org.arkaan.simpleatm.datamodel;

import java.util.Optional;

public interface Repository<T> {
    Optional<T> findOne(int id);
    T save(T data);
    T update(int id, T data);
    T remove(int id);
}
