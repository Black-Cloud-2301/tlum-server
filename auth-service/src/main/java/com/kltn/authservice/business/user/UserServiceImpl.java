package com.kltn.authservice.business.user;

import com.kltn.authservice.business.file.ExcelToJsonService;
import com.kltn.authservice.business.user.exceptions.UserAlreadyExistsException;
import com.kltn.authservice.business.user.exceptions.UserNotFoundException;
import com.kltn.authservice.config.I18n;
import com.kltn.authservice.utils.exception.CustomException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExcelToJsonService excelToJsonService;
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ExcelToJsonService excelToJsonService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.excelToJsonService = excelToJsonService;
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User findByCodeIgnoreCase(String code) {
        return userRepository.findByCodeIgnoreCase(code).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByCodeIgnoreCase(user.getCode())) throw new UserAlreadyExistsException();
        user.setCode(user.getCode().toUpperCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> importUsers(MultipartFile file) {
        try {
            List<List<Object>> excelData = excelToJsonService.convertExcelToJson(file);
            List<User> users = new ArrayList<>();

            for (List<Object> rowData : excelData) {
                this.validateUserImport(rowData);
                User user = new User();
                user.setCode((String) rowData.get(0));
                user.setFirstname((String) rowData.get(1));
                user.setLastname((String) rowData.get(2));
                user.setPhoneNumber((String) rowData.get(3));
                user.setEmail((String) rowData.get(4));
                user.setGender(Gender.fromString((String) rowData.get(5)));
                user.setDateOfBirth((LocalDate) rowData.get(6));
                user.setAddress((String) rowData.get(7));
                users.add(user);
            }
            return userRepository.saveAll(users);
        } catch (IOException e) {
            LOGGER.error("Error while converting excel to json", e);
            throw new Error("Error processing file");
        }
    }

    private void validateUserImport(List<Object> rowData) {
        if (rowData.getFirst() == null || rowData.getFirst().toString().isEmpty()) {
            throw new CustomException(I18n.getMessage("msg.validate.required", I18n.getMessage("msg.field.user.code")));
        }

        if(rowData.get(0).toString().length() != 6) {
            throw new IllegalArgumentException("Code should not exceed 6 characters");
        }

        if (rowData.get(1) == null || rowData.get(1).toString().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }

        if(rowData.get(1).toString().length() > 255) {
            throw new IllegalArgumentException("First name should not exceed 255 characters");
        }

        if (rowData.get(2) == null || rowData.get(2).toString().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }

        if(rowData.get(2).toString().length() > 255) {
            throw new IllegalArgumentException("First name should not exceed 255 characters");
        }

        if (rowData.get(3) == null || rowData.get(3).toString().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        if(rowData.get(3).toString().length() > 12) {
            throw new IllegalArgumentException("Phone number should not exceed 12 characters");
        }

        if (rowData.get(4) == null || rowData.get(4).toString().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if(rowData.get(4).toString().length() > 1023) {
            throw new IllegalArgumentException("Email should not exceed 1023 characters");
        }

        if (rowData.get(5) == null || rowData.get(5).toString().isEmpty()) {
            throw new IllegalArgumentException("Gender is required");
        }

        if (rowData.get(6) == null || rowData.get(6).toString().isEmpty()) {
            throw new IllegalArgumentException("Date of birth is required");
        }

        if (rowData.get(7) != null && rowData.get(7).toString().length() > 2047) {
            throw new IllegalArgumentException("Address should not exceed 2047 characters");
        }
    }

}
