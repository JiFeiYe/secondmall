package com.tu.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author JiFeiYe
 * @since 2024-10-10
 */
@ApiModel(description = "用户信息表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花/UUID？
     */
    @ApiModelProperty("雪花/UUID？")
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;

    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String account;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 密码（MD5）
     */
    @ApiModelProperty("密码（MD5）")
    private String password;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String name;

    /**
     * 性别（0女，1男）
     */
    @ApiModelProperty("性别（0女，1男）")
    private Integer gender;

    /**
     * 出生日期
     */
    @ApiModelProperty("出生日期")
    private LocalDateTime birthdate;

    /**
     * 头像（OSS）
     */
    @ApiModelProperty("头像（OSS）")
    private String picture;

    /**
     * 微信号
     */
    @ApiModelProperty("微信号")
    private String wechat;

    /**
     * 用户状态（0正常，1封禁）
     */
    @ApiModelProperty("用户状态（0正常，1封禁）")
    private Integer status;

    /**
     * 身份（0用户，1管理员）
     */
    @ApiModelProperty("身份（0用户，1管理员）")
    private Integer identity;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("")
    private Integer isDeleted;


}
