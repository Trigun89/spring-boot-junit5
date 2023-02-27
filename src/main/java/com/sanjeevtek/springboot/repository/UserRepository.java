package com.sanjeevtek.springboot.repository;

import com.sanjeevtek.springboot.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    // define custom query using JPQL with index params
    @Query("select e from Users e where e.firstName = ?1 and e.lastName = ?2")
    Users findByJPQL(String firstName, String lastName);

    // define custom query using JPQL with named params
    @Query("select e from Users e where e.firstName =:firstName and e.lastName =:lastName")
    Users findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // define custom query using Native SQL with index params
    @Query(value = "select * from Users e where e.first_name =?1 and e.last_name =?2", nativeQuery = true)
    Users findByNativeSQL(String firstName, String lastName);

    // define custom query using Native SQL with named params
    @Query(value = "select * from Users e where e.first_name =:firstName and e.last_name =:lastName",
            nativeQuery = true)
    Users findByNativeSQLNamed(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
