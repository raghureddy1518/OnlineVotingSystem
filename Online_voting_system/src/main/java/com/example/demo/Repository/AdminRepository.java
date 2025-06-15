package com.example.demo.Repository;

import com.example.demo.Entity.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Admin findByUsername(String username); // Changed from findByEmail to match the field
}
