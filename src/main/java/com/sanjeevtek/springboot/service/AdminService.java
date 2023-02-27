package com.sanjeevtek.springboot.service;

import com.sanjeevtek.springboot.model.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminService {
    Admin saveAdmin(Admin admin);
    List<Admin> getAllAdmins();
    Optional<Admin> getAdminById(long id);
    Admin updateAdmin(Admin updatedAdmin);
    void deleteAdmin(long id);
}
