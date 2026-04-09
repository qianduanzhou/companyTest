package com.company.consumer.controller;

import com.company.api.dto.UserDTO;
import com.company.api.dto.UserPageQueryDTO;
import com.company.api.entity.User;
import com.company.api.result.PageResult;
import com.company.api.result.Result;
import com.company.api.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @DubboReference
    private UserService userService;

    @GetMapping("/{id:\\d+}")
    public Result<User> getById(@PathVariable @Min(value = 1, message = "user id must be greater than 0") Long id) {
        return userService.getById(id);
    }

    @GetMapping("/list")
    public Result<List<User>> list() {
        return userService.listAll();
    }

    @GetMapping("/page")
    public Result<PageResult<User>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) String keyword) {
        UserPageQueryDTO queryDTO = new UserPageQueryDTO();
        queryDTO.setPageNum(pageNum);
        queryDTO.setPageSize(pageSize);
        queryDTO.setKeyword(keyword);
        return userService.page(queryDTO);
    }

    @PostMapping
    public Result<Boolean> add(@Valid @RequestBody UserDTO userDTO) {
        return userService.add(userDTO);
    }

    @PutMapping
    public Result<Boolean> update(@Valid @RequestBody UserDTO userDTO) {
        return userService.update(userDTO);
    }

    @DeleteMapping("/{id:\\d+}")
    public Result<Boolean> delete(@PathVariable @Min(value = 1, message = "user id must be greater than 0") Long id) {
        return userService.deleteById(id);
    }
}
