package com.example.blog.repository.accountRepository;

import com.example.blog.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // 권한 조회
    Role findByName(String name);
}
