package com.company.provider.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.company.api.dto.UserDTO;
import com.company.api.dto.UserPageQueryDTO;
import com.company.api.entity.User;
import com.company.api.result.PageResult;
import com.company.api.result.Result;
import com.company.api.service.UserService;
import com.company.provider.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

@DubboService
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public Result<User> getById(Long id) {
        User user = userMapper.selectById(id);
        return Result.success(user);
    }

    @Override
    public Result<List<User>> listAll() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(User::getId);
        List<User> users = userMapper.selectList(queryWrapper);
        return Result.success(users);
    }

    @Override
    public Result<PageResult<User>> page(UserPageQueryDTO queryDTO) {
        long pageNum = queryDTO.getPageNum() == null || queryDTO.getPageNum() < 1 ? 1 : queryDTO.getPageNum();
        long pageSize = queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1 ? 10 : Math.min(queryDTO.getPageSize(), 100);
        String keyword = StringUtils.trimToEmpty(queryDTO.getKeyword());

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keyword)) {
            queryWrapper.and(wrapper -> wrapper.like(User::getUsername, keyword)
                    .or()
                    .like(User::getPhone, keyword));
        }
        queryWrapper.orderByDesc(User::getId);

        Page<User> page = userMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        PageResult<User> result = PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
        return Result.success(result);
    }

    @Override
    public Result<Boolean> add(UserDTO userDTO) {
        if (existsByUsername(userDTO.getUsername(), null)) {
            return Result.fail(400, "username already exists");
        }

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setStatus(1);
        user.setDeleted(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return Result.success(userMapper.insert(user) > 0);
    }

    @Override
    public Result<Boolean> update(UserDTO userDTO) {
        if (userDTO.getId() == null) {
            return Result.fail(400, "user id is required for update");
        }
        if (existsByUsername(userDTO.getUsername(), userDTO.getId())) {
            return Result.fail(400, "username already exists");
        }

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setUpdateTime(LocalDateTime.now());
        return Result.success(userMapper.updateById(user) > 0);
    }

    @Override
    public Result<Boolean> deleteById(Long id) {
        return Result.success(userMapper.deleteById(id) > 0);
    }

    private boolean existsByUsername(String username, Long excludeId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        queryWrapper.ne(excludeId != null, User::getId, excludeId);
        return userMapper.selectCount(queryWrapper) > 0;
    }
}
