package com.company.api.service;

import com.company.api.dto.UserDTO;
import com.company.api.dto.UserPageQueryDTO;
import com.company.api.entity.User;
import com.company.api.result.PageResult;
import com.company.api.result.Result;

import java.util.List;

/**
 * 用户服务接口（Dubbo 服务契约）
 */
public interface UserService {

    Result<User> getById(Long id);

    Result<List<User>> listAll();

    Result<PageResult<User>> page(UserPageQueryDTO queryDTO);

    Result<Boolean> add(UserDTO userDTO);

    Result<Boolean> update(UserDTO userDTO);

    Result<Boolean> deleteById(Long id);
}
