package com.promote.project.tool.swagger;

import com.promote.common.utils.StringUtils;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * swagger 使用者測試方法
 * 
 * @author ruoyi
 */
@Api("使用者資訊管理")
@RestController
@RequestMapping("/test/user")
public class TestController extends BaseController
{
    private final static Map<Integer, UserEntity> users = new LinkedHashMap<Integer, UserEntity>();
    {
        users.put(1, new UserEntity(1, "admin", "admin123", "15888888888"));
        users.put(2, new UserEntity(2, "ry", "admin123", "15666666666"));
    }

    @ApiOperation("獲取使用者列表")
    @GetMapping("/list")
    public AjaxResult userList()
    {
        List<UserEntity> userList = new ArrayList<UserEntity>(users.values());
        return AjaxResult.success(userList);
    }

    @ApiOperation("獲取使用者詳細")
    @ApiImplicitParam(name = "userId", value = "使用者ID", required = true, dataType = "int", paramType = "path")
    @GetMapping("/{userId}")
    public AjaxResult getUser(@PathVariable Integer userId)
    {
        if (!users.isEmpty() && users.containsKey(userId))
        {
            return AjaxResult.success(users.get(userId));
        }
        else
        {
            return AjaxResult.error("使用者不存在");
        }
    }

    @ApiOperation("新增使用者")
    @ApiImplicitParam(name = "userEntity", value = "新增使用者資訊", dataType = "UserEntity")
    @PostMapping("/save")
    public AjaxResult save(UserEntity user)
    {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId()))
        {
            return AjaxResult.error("使用者ID不能為空");
        }
        return AjaxResult.success(users.put(user.getUserId(), user));
    }

    @ApiOperation("更新使用者")
    @ApiImplicitParam(name = "userEntity", value = "新增使用者資訊", dataType = "UserEntity")
    @PutMapping("/update")
    public AjaxResult update(UserEntity user)
    {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUserId()))
        {
            return AjaxResult.error("使用者ID不能為空");
        }
        if (users.isEmpty() || !users.containsKey(user.getUserId()))
        {
            return AjaxResult.error("使用者不存在");
        }
        users.remove(user.getUserId());
        return AjaxResult.success(users.put(user.getUserId(), user));
    }

    @ApiOperation("刪除使用者資訊")
    @ApiImplicitParam(name = "userId", value = "使用者ID", required = true, dataType = "int", paramType = "path")
    @DeleteMapping("/{userId}")
    public AjaxResult delete(@PathVariable Integer userId)
    {
        if (!users.isEmpty() && users.containsKey(userId))
        {
            users.remove(userId);
            return AjaxResult.success();
        }
        else
        {
            return AjaxResult.error("使用者不存在");
        }
    }
}

@ApiModel("使用者實體")
class UserEntity
{
    @ApiModelProperty("使用者ID")
    private Integer userId;

    @ApiModelProperty("使用者名稱")
    private String username;

    @ApiModelProperty("使用者密碼")
    private String password;

    @ApiModelProperty("使用者手機")
    private String mobile;

    public UserEntity()
    {

    }

    public UserEntity(Integer userId, String username, String password, String mobile)
    {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
}
