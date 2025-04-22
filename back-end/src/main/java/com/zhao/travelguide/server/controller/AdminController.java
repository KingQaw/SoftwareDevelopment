package com.zhao.travelguide.server.controller;

import com.zhao.travelguide.pojo.dto.SetUserStatusDTO;
import com.zhao.travelguide.pojo.dto.admin.ImportDataDTO;
import com.zhao.travelguide.pojo.dto.admin.ModifyPasswordDTO;
import com.zhao.travelguide.pojo.dto.admin.QueryUsersDTO;
import com.zhao.travelguide.pojo.dto.admin.SetAuditStatusDTO;
import com.zhao.travelguide.pojo.entity.VUserDetails;
import com.zhao.travelguide.pojo.vo.Result;
import com.zhao.travelguide.pojo.vo.admin.GetDataOverviewVO;
import com.zhao.travelguide.pojo.vo.admin.GetUsersVO;
import com.zhao.travelguide.server.service.AdminService;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.ResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/overview")
    public Result<GetDataOverviewVO> getDataOverview() {
        return Result.success("获取数据成功", adminService.getDataOverview());
    }

    @PostMapping("/audit")
    public Result<?> setAuditStatus(@RequestBody SetAuditStatusDTO setAuditStatusDTO) {
        adminService.setAuditStatus(setAuditStatusDTO);
        return Result.success("审核成功", null);
    }

    @GetMapping("/users")
    public Result<?> getUsers(@RequestParam(required = false, defaultValue = "1") Integer page,
                             @RequestParam(required = false, defaultValue = "1000") Integer size,
                             @RequestParam(required = false) String status,
                             @RequestParam(required = false) String nickname) {
        QueryUsersDTO queryUsersDTO = new QueryUsersDTO(page, size, status);
        return Result.success("获取用户列表成功", adminService.getUsers(queryUsersDTO));
    }

    @DeleteMapping("/guides/{id}")
    public Result<?> deleteGuide(@PathVariable Integer id) {
        adminService.deleteGuide(id);
        return Result.success("删除成功", null);
    }

    @PostMapping("/modifyPassword")
    public Result<?> modifyPassword(@RequestBody ModifyPasswordDTO modifyPasswordDTO) {
        adminService.modifyPassword(modifyPasswordDTO);
        return Result.success("修改密码成功", null);
    }

    @GetMapping("/visits")
    public Result<?> getVisits(@RequestParam(required = false, defaultValue = "1") Integer page,
                               @RequestParam(required = false, defaultValue = "1000") Integer size) {
        return Result.success("获取访问记录成功", adminService.getVisits(page, size));
    }

    @PostMapping("/users/status")
    public Result<?> setUserStatus(@RequestBody SetUserStatusDTO setAuditStatusDTO) {
        adminService.setUserStatus(setAuditStatusDTO);
        return Result.success("修改用户状态成功", null);
    }

    @PostMapping("/users/import")
    public Result<?> importData(@RequestBody List<ImportDataDTO> importDataDTO) {
        return Result.success("导入成功", adminService.importData(importDataDTO));
    }

}
