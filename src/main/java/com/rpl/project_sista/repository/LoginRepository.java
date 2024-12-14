package com.rpl.project_sista.repository;

import java.util.List;

import com.rpl.project_sista.model.entity.Users;

public interface LoginRepository {
    List<Users> findUserByEmail(String email);
    int findMhsId(int userId);
    int findDosenId(int userId);
}
