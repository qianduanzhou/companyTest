package com.company.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户分页查询参数
 */
@Data
public class UserPageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
}
