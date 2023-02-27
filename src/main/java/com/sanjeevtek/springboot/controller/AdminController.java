package com.sanjeevtek.springboot.controller;

import com.sanjeevtek.springboot.model.Admin;
import com.sanjeevtek.springboot.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Admin createAdmin(@RequestBody Admin admin){
        return adminService.saveAdmin(admin);
    }

    @GetMapping
    public List<Admin> getAllAdmins(){
        return adminService.getAllAdmins();
    }

    @GetMapping("{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable("id") long adminId){
        return adminService.getAdminById(adminId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable("id") long AdminId,
                                                 @RequestBody Admin admin){
        return adminService.getAdminById(AdminId)
                .map(savedAdmin -> {

                    savedAdmin.setFirstName(admin.getFirstName());
                    savedAdmin.setLastName(admin.getLastName());
                    savedAdmin.setEmail(admin.getEmail());

                    Admin updatedAdmin = adminService.updateAdmin(savedAdmin);
                    return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable("id") long adminId){
        adminService.deleteAdmin(adminId);
        return new ResponseEntity<String>("Admins deleted successfully!.", HttpStatus.OK);
    }

}
