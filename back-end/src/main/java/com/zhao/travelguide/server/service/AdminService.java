package com.zhao.travelguide.server.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhao.travelguide.pojo.dto.SetUserStatusDTO;
import com.zhao.travelguide.pojo.dto.admin.ImportDataDTO;
import com.zhao.travelguide.pojo.dto.admin.ModifyPasswordDTO;
import com.zhao.travelguide.pojo.dto.admin.QueryUsersDTO;
import com.zhao.travelguide.pojo.dto.admin.SetAuditStatusDTO;
import com.zhao.travelguide.pojo.entity.*;
import com.zhao.travelguide.pojo.vo.admin.ImportDataVO;
import com.zhao.travelguide.pojo.vo.admin.RegionCount;
import com.zhao.travelguide.server.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhao.travelguide.pojo.vo.admin.GetDataOverviewVO;

@Service
public class AdminService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private GuidesMapper guidesMapper;
    @Autowired
    private VGuideDetailsMapper vGuideDetailsMapper;
    @Autowired
    private GuidesAuditStatusMapper guidesAuditStatusMapper;
    @Autowired
    private VUserDetailsMapper vUserDetailsMapper;
    @Autowired
    private VisitsMapper visitsMapper;
    @Autowired
    private VUsersVisitsMapper vUsersVisitsMapper;

    public GetDataOverviewVO getDataOverview() {
        // 获取当前的年月日
        GetDataOverviewVO getDataOverviewVO = new GetDataOverviewVO();
        LocalDate now = LocalDate.now();
        GetDataOverviewVO.ChartData sevenDayData = new GetDataOverviewVO.ChartData();
        sevenDayData.setDate(new ArrayList<>());
        sevenDayData.setUserCounts(new ArrayList<>());
        sevenDayData.setGuideCounts(new ArrayList<>());

        GetDataOverviewVO.VisitData visitSevenData = new GetDataOverviewVO.VisitData();
        visitSevenData.setDate(new ArrayList<>());
        visitSevenData.setVisitCounts(new ArrayList<>());
        for (int i = 7; i >= 0; i--) {
            LocalDate date = now.minusDays(i);
            QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
            usersQueryWrapper.le("create_time", Date.valueOf(date));
            Long count = usersMapper.selectCount(usersQueryWrapper);
            sevenDayData.getDate().add(date.toString());
            sevenDayData.getUserCounts().add(count.intValue());
            QueryWrapper<Guides> guidesQueryWrapper = new QueryWrapper<>();
            guidesQueryWrapper.le("create_time", Date.valueOf(date));
            Long count1 = guidesMapper.selectCount(guidesQueryWrapper);
            sevenDayData.getGuideCounts().add(count1.intValue());
            QueryWrapper<VUsersVisits> vUsersVisitsQueryWrapper = new QueryWrapper<>();
            vUsersVisitsQueryWrapper.le("login_time", Date.valueOf(date));
            Long count2 = vUsersVisitsMapper.selectCount(vUsersVisitsQueryWrapper);
            visitSevenData.getDate().add(date.toString());
            visitSevenData.getVisitCounts().add(count2.intValue());
        }
        getDataOverviewVO.setSevenDayData(sevenDayData);
        getDataOverviewVO.setVisitSevenData(visitSevenData);

        GetDataOverviewVO.ChartData monthData = new GetDataOverviewVO.ChartData();
        monthData.setDate(new ArrayList<>());
        monthData.setUserCounts(new ArrayList<>());
        monthData.setGuideCounts(new ArrayList<>());
        GetDataOverviewVO.VisitData monthVisitData = new GetDataOverviewVO.VisitData();
        monthVisitData.setDate(new ArrayList<>());
        monthVisitData.setVisitCounts(new ArrayList<>());
        for (int i = 30; i >= 0; i--) {
            LocalDate date = now.minusDays(i);
            QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
            usersQueryWrapper.le("create_time", Date.valueOf(date));
            Long count = usersMapper.selectCount(usersQueryWrapper);
            monthData.getDate().add(date.toString());
            monthData.getUserCounts().add(count.intValue());
            QueryWrapper<Guides> guidesQueryWrapper = new QueryWrapper<>();
            guidesQueryWrapper.le("create_time", Date.valueOf(date));
            Long count1 = guidesMapper.selectCount(guidesQueryWrapper);
            monthData.getGuideCounts().add(count1.intValue());
            QueryWrapper<VUsersVisits> vUsersVisitsQueryWrapper = new QueryWrapper<>();
            vUsersVisitsQueryWrapper.le("login_time", Date.valueOf(date));
            Long count2 = vUsersVisitsMapper.selectCount(vUsersVisitsQueryWrapper);
            monthVisitData.getDate().add(date.toString());
            monthVisitData.getVisitCounts().add(count2.intValue());
        }
        getDataOverviewVO.setMonthData(monthData);
        getDataOverviewVO.setVisitMonthData(monthVisitData);

        QueryWrapper<VGuideDetails> vGuideDetailsQueryWrapper = new QueryWrapper<>();
        vGuideDetailsQueryWrapper.eq("audit_status", "pending");
        Long pendingGuideCount = vGuideDetailsMapper.selectCount(vGuideDetailsQueryWrapper);

        QueryWrapper<VGuideDetails> vGuideDetailsQueryWrapper1 = new QueryWrapper<>();
        vGuideDetailsQueryWrapper1.eq("audit_status", "approved");
        Long approvedGuideCount = vGuideDetailsMapper.selectCount(vGuideDetailsQueryWrapper1);

        QueryWrapper<VGuideDetails> vGuideDetailsQueryWrapper2 = new QueryWrapper<>();
        vGuideDetailsQueryWrapper2.eq("audit_status", "rejected");
        Long rejectedGuideCount = vGuideDetailsMapper.selectCount(vGuideDetailsQueryWrapper2);

        getDataOverviewVO.setPendingGuidesCounts(pendingGuideCount.intValue());
        getDataOverviewVO.setApprovedGuidesCounts(approvedGuideCount.intValue());
        getDataOverviewVO.setRejectedGuidesCounts(rejectedGuideCount.intValue());

        List<RegionCount> regionCounts = vUsersVisitsMapper.countByRegion();
        getDataOverviewVO.setRegionData(regionCounts);
        return getDataOverviewVO;
    }

    public void setAuditStatus(SetAuditStatusDTO setAuditStatusDTO) {
        Long guideId = setAuditStatusDTO.getId();
        String status = setAuditStatusDTO.getStatus();

        QueryWrapper<GuidesAuditStatus> guidesAuditStatusQueryWrapper = new QueryWrapper<>();
        guidesAuditStatusQueryWrapper.eq("name", status);
        GuidesAuditStatus guidesAuditStatus = guidesAuditStatusMapper.selectOne(guidesAuditStatusQueryWrapper);
        Long auditStatusId = guidesAuditStatus.getId();

        UpdateWrapper<Guides> guidesUpdateWrapper = new UpdateWrapper<>();
        guidesUpdateWrapper.eq("id", guideId).set("guides_audit_status_id", auditStatusId);
        guidesMapper.update(null, guidesUpdateWrapper);
    }

    public Page<VUserDetails> getUsers(QueryUsersDTO queryUsersDTO) {
        Page<VUserDetails> page = new Page<>(queryUsersDTO.getPage(), queryUsersDTO.getSize());
        QueryWrapper<VUserDetails> vUserDetailsQueryWrapper = new QueryWrapper<>();
        vUserDetailsQueryWrapper.ne("account", "admin");
        if (queryUsersDTO.getStatus() != null) {
            vUserDetailsQueryWrapper.eq("status", queryUsersDTO.getStatus());
        }
        return vUserDetailsMapper.selectPage(page, vUserDetailsQueryWrapper);
    }

    public void deleteGuide(Integer id) {
        guidesMapper.deleteById(id);
    }

    public void modifyPassword(ModifyPasswordDTO modifyPasswordDTO) {
        Long id = modifyPasswordDTO.getId();
        String password = modifyPasswordDTO.getPassword();
        UpdateWrapper<Users> usersUpdateWrapper = new UpdateWrapper<>();
        usersUpdateWrapper.eq("id", id).set("password", password);
        usersMapper.update(null, usersUpdateWrapper);
    }

    public Page<VUsersVisits> getVisits(Integer page, Integer size) {
        return vUsersVisitsMapper.selectPage(new Page<>(page, size), null);
    }

    public void setUserStatus(SetUserStatusDTO setUserStatusDTO) {
        Long id = setUserStatusDTO.getId();
        String status = setUserStatusDTO.getStatus();
        UpdateWrapper<Users> usersUpdateWrapper = new UpdateWrapper<>();
        Long userStatusId = status.equals("正常") ? 1L : status.equals("禁言") ? 2L : 3L;
        usersUpdateWrapper.eq("id", id).set("users_status_id", userStatusId);
        usersMapper.update(null, usersUpdateWrapper);
    }

    public ImportDataVO importData(List<ImportDataDTO> importDataDTO) {
        int total = 0;
        int error = 0;
        List<String> errorAccounts = new ArrayList<>();
        for (ImportDataDTO dto : importDataDTO) {
            total++;
            if (dto.getAccount() != null && dto.getNickname() != null && dto.getPassword() != null) {
                Users users = new Users();
                users.setAccount(dto.getAccount());
                users.setNickname(dto.getNickname());
                users.setPassword(dto.getPassword());
                users.setRolesId(1L);
                try {
                    usersMapper.insert(users);
                } catch (Exception e) {
                    errorAccounts.add(dto.getAccount());
                }
            } else {
                errorAccounts.add(dto.getAccount());
            }
        }
        error = errorAccounts.size();
        return new ImportDataVO(total, error, errorAccounts);
    }
}
