package com.sanjeevtek.springboot.service.impl;

import com.sanjeevtek.springboot.exception.ResourceNotFoundException;
import com.sanjeevtek.springboot.model.Admin;
import com.sanjeevtek.springboot.repository.AdminRepository;
import com.sanjeevtek.springboot.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Admin saveAdmin(Admin admin) {

        Optional<Admin> savedAdmin = adminRepository.findByEmail(admin.getEmail());
        if(savedAdmin.isPresent()){
            throw new ResourceNotFoundException("Stores already exist with given email:" + admin.getEmail());
        }
        return adminRepository.save(admin);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Optional<Admin> getAdminById(long id) {
        return adminRepository.findById(id);
    }

    @Override
    public Admin updateAdmin(Admin updatedStores) {
        return adminRepository.save(updatedStores);
    }

    @Override
    public void deleteAdmin(long id) {
        adminRepository.deleteById(id);
    }
}
