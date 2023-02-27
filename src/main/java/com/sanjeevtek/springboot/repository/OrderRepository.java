package com.sanjeevtek.springboot.repository;

import com.sanjeevtek.springboot.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findByEmail(String email);

    // define custom query using JPQL with index params
    @Query("select e from Orders e where e.firstName = ?1 and e.lastName = ?2")
    Orders findByJPQL(String firstName, String lastName);

    // define custom query using JPQL with named params
    @Query("select e from Orders e where e.firstName =:firstName and e.lastName =:lastName")
    Orders findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // define custom query using Native SQL with index params
    @Query(value = "select * from Orders e where e.first_name =?1 and e.last_name =?2", nativeQuery = true)
    Orders findByNativeSQL(String firstName, String lastName);

    // define custom query using Native SQL with named params
    @Query(value = "select * from Orders e where e.first_name =:firstName and e.last_name =:lastName",
            nativeQuery = true)
    Orders findByNativeSQLNamed(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
