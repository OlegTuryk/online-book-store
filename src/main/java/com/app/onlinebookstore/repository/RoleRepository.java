package com.app.onlinebookstore.repository;

import com.app.onlinebookstore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role getByName(Role.RoleName roleName);
}
