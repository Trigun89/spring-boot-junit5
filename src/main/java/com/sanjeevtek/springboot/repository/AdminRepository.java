package com.sanjeevtek.springboot.repository;

import com.sanjeevtek.springboot.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);

    // define custom query using JPQL with index params
    @Query("select e from Admin e where e.firstName = ?1 and e.lastName = ?2")
    Admin findByJPQL(String firstName, String lastName);

    // define custom query using JPQL with named params
    @Query("select e from Admin e where e.firstName =:firstName and e.lastName =:lastName")
    Admin findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // define custom query using Native SQL with index params
    @Query(value = "select * from Admin e where e.first_name =?1 and e.last_name =?2", nativeQuery = true)
    Admin findByNativeSQL(String firstName, String lastName);

    // define custom query using Native SQL with named params
    @Query(value = "select * from Admin e where e.first_name =:firstName and e.last_name =:lastName",
            nativeQuery = true)
    Admin findByNativeSQLNamed(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
