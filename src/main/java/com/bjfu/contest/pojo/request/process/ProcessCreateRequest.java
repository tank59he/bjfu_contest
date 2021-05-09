package com.bjfu.contest.pojo.request.process;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 流程创建请求
 * @author warthog
 */
@Data
public class ProcessCreateRequest {
    /**
     * 对应竞赛id
     */
    @NotNull(message = "竞赛id不能为空!")
    private Long contestId;
    /**
     * 流程名
     */
    @NotBlank(message = "流程名不能为空!")
    @Length(max = 32, message = "流程名最大32位!")
    private String name;
    /**
     * 流程描述
     */
    @NotBlank(message = "流程描述不能为空!")
    @Length(max = 512, message = "流程描述最大512位!")
    private String description;
    /**
     * 需要提交的内容(json array)
     */
    @Length(max = 512, message = "提交的内容最大512位!")
    private String submitList;
    /**
     * 停止提交的时间
     */
    @NotNull(message = "停止提交的时间不能为空!")
    private Date endSubmitTime;
}
