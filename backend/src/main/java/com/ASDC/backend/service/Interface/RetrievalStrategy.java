package com.ASDC.backend.service.Interface;

import com.ASDC.backend.entity.Room;

import java.util.List;

public interface RetrievalStrategy<T> {
    List<T> retrieveItems(Room room);
}
