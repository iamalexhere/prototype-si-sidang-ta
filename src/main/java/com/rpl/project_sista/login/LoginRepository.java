package com.rpl.project_sista.login;

import java.util.List;

public interface LoginRepository {
    List<User> findByEmail(String email);
}
