package com.bjfu.contest.controller;

import com.bjfu.contest.enums.ContestStatusEnum;
import com.bjfu.contest.enums.ResultEnum;
import com.bjfu.contest.exception.BaseAppException;
import com.bjfu.contest.exception.BizException;
import com.bjfu.contest.pojo.BaseResult;
import com.bjfu.contest.pojo.dto.ContestDTO;
import com.bjfu.contest.pojo.dto.UserDTO;
import com.bjfu.contest.pojo.request.contest.ContestCreateRequest;
import com.bjfu.contest.pojo.request.contest.ContestEditRequest;
import com.bjfu.contest.pojo.request.contest.ContestListAllRequest;
import com.bjfu.contest.pojo.request.contest.ContestListCreatedRequest;
import com.bjfu.contest.pojo.vo.ContestVO;
import com.bjfu.contest.security.annotation.RequireLogin;
import com.bjfu.contest.security.annotation.RequireTeacher;
import com.bjfu.contest.service.ContestService;
import com.bjfu.contest.utils.UserInfoContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping("/contest")
public class ContestController {

    @Autowired
    private ContestService contestService;

    @RequireTeacher
    @PostMapping("/create")
    public BaseResult<ContestVO> create(@Validated @RequestBody ContestCreateRequest request) {
        UserDTO userDTO = UserInfoContextUtil.getUserInfo()
                .orElseThrow(() -> new BaseAppException(ResultEnum.USER_CONTEXT_ERROR));
        ContestDTO contestDTO = contestService.create(request, userDTO.getAccount());
        return BaseResult.success(new ContestVO(contestDTO));
    }

    @RequireTeacher
    @PostMapping("/edit")
    public BaseResult<Void> edit(@Validated @RequestBody ContestEditRequest request) {
        if(request.getStatus().equals(ContestStatusEnum.DELETE)) {
            throw new BizException(ResultEnum.WRONG_REQUEST_PARAMS);
        }
        UserDTO userDTO = UserInfoContextUtil.getUserInfo()
                .orElseThrow(() -> new BaseAppException(ResultEnum.USER_CONTEXT_ERROR));
        contestService.edit(request, userDTO.getAccount());
        return BaseResult.success();
    }

    @RequireTeacher
    @DeleteMapping("/delete")
    public BaseResult<Void> delete(@NotNull(message = "竞赛id不能为空!") Long contestId) {
        UserDTO userDTO = UserInfoContextUtil.getUserInfo()
                .orElseThrow(() -> new BaseAppException(ResultEnum.USER_CONTEXT_ERROR));
        contestService.delete(contestId, userDTO.getAccount());
        return BaseResult.success();
    }

    @GetMapping("/getInfo")
    public BaseResult<ContestVO> getInfo(@NotNull(message = "竞赛id不能为空!") Long contestId) {
        ContestDTO contestDTO = contestService.getInfo(contestId);
        return BaseResult.success(new ContestVO(contestDTO));
    }

    @RequireTeacher
    @PostMapping("/listCreated")
    public BaseResult<Page<ContestVO>> listCreated(@Validated @RequestBody ContestListCreatedRequest request) {
        UserDTO userDTO = UserInfoContextUtil.getUserInfo()
                .orElseThrow(() -> new BaseAppException(ResultEnum.USER_CONTEXT_ERROR));
        Page<ContestDTO> contestDTOS = contestService.listCreated(request, userDTO.getAccount());
        return BaseResult.success(contestDTOS.map(ContestVO::new));
    }

    @RequireLogin
    @PostMapping("/listAll")
    public BaseResult<Page<ContestVO>> listAll(@Validated @RequestBody ContestListAllRequest request) {
        UserDTO userDTO = UserInfoContextUtil.getUserInfo()
                .orElseThrow(() -> new BaseAppException(ResultEnum.USER_CONTEXT_ERROR));
        Page<ContestDTO> contestDTOS = contestService.listAll(request, userDTO.getAccount());
        return BaseResult.success(contestDTOS.map(ContestVO::new));
    }

}
