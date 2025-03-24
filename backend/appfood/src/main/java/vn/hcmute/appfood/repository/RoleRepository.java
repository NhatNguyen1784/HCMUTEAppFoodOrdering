package vn.hcmute.appfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hcmute.appfood.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findById(long id);
}
