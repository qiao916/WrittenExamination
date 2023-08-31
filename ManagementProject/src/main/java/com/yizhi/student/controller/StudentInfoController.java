package com.yizhi.student.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.xml.internal.bind.v2.TODO;
import com.yizhi.common.annotation.Log;
import com.yizhi.common.controller.BaseController;
import com.yizhi.common.utils.*;
import com.yizhi.student.domain.ClassDO;
import com.yizhi.student.service.ClassService;
import com.yizhi.student.service.CollegeService;
import com.yizhi.student.service.MajorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.yizhi.student.domain.StudentInfoDO;
import com.yizhi.student.service.StudentInfoService;

/**
 * 生基础信息表
 */

@Controller
@RequestMapping("/student/studentInfo")
public class StudentInfoController {


    @Autowired
    private StudentInfoService studentInfoService;

    //
    @Log("学生信息保存")
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("student:studentInfo:add")
    public R save(StudentInfoDO studentInfoDO) {
        //TODO 应当获取当前操作人的id，进行插入 此处笔试为超级管理员 将设置为1
        studentInfoDO.setAddUserid(1);
        studentInfoDO.setAddTime(new Date());
        int save = studentInfoService.save(studentInfoDO);

        if (save > 0) {
            return R.ok("添加成功");
        }

        return R.error("添加失败");
    }

    /**
     * 可分页 查询
     */
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("student:studentInfo:studentInfo")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        System.out.println("params======"+params);
        Query query = new Query(params);

        List<StudentInfoDO> list = studentInfoService.list(query);

        System.out.println(list);
        PageUtils page = new PageUtils(list, list.size(), 1, 10);
        return page;

    }


    /**
     * 修改
     */
    @Log("学生基础信息表修改")
    @ResponseBody
    @PostMapping("/update")
    @RequiresPermissions("student:studentInfo:edit")
    public R update(StudentInfoDO studentInfo) {
        Date date = new Date();// 获取当前时间
        studentInfo.setEditTime(date);
        //TODO //应当获取当前操作人的id，进行插入 此处笔试为超级管理员 将设置为1
        studentInfo.setEditUserid(1);
        int update = studentInfoService.update(studentInfo);
        if (update > 0) {
            return R.ok("修改成功");
        }

        return R.error("修改失败");
    }

    /**
     * 删除
     */
    @Log("学生基础信息表删除")
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("student:studentInfo:remove")
    public R remove(Integer id) {
        int remove = studentInfoService.remove(id);
        if (remove != 0) {
            return R.ok("删除成功");
        }
        return R.error("删除失败");
    }

    /**
     * 批量删除
     */
    @Log("学生基础信息表批量删除")
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("student:studentInfo:batchRemove")
    public R remove(@RequestParam("ids[]") Integer[] ids) {
        int i = studentInfoService.batchRemove(ids);
        if (i != 0) {
            return R.ok("批量删除成功");
        }

        return R.error("批量删除失败");
    }


    //前后端不分离 客户端 -> 控制器-> 定位视图

    /**
     * 学生管理 点击Tab标签 forward页面
     */
    @GetMapping()
    @RequiresPermissions("student:studentInfo:studentInfo")
    String StudentInfo() {
        return "student/studentInfo/studentInfo";
    }

    /**
     * 更新功能 弹出View定位
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("student:studentInfo:edit")
    String edit(@PathVariable("id") Integer id, Model model) {
        StudentInfoDO studentInfo = studentInfoService.get(id);
        model.addAttribute("studentInfo", studentInfo);
        return "student/studentInfo/edit";
    }

    /**
     * 学生管理 添加学生弹出 View
     */
    @GetMapping("/add")
    @RequiresPermissions("student:studentInfo:add")
    String add() {
        return "student/studentInfo/add";
    }

}//end class
