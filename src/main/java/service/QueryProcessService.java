package service;

import model.Query;

public interface QueryProcessService {
    Query processQuery(String line);
}
