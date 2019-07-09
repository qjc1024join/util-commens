package com.util.commons.controller;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.util.commons.entity.TbUser;
import com.util.commons.exception.ExcelException;
import com.util.commons.service.EasyService;
import com.util.commons.util.CSVUtils;
import com.util.commons.util.CsvExportUtil;
import com.util.commons.util.ExcelUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.aspectj.bridge.MessageUtil.fail;

@Controller
@RequestMapping("/easy")
public class EasyExcelController {

    @Autowired
    private EasyService easyService;

    /**
     * 导出EasyExcel
     * @param response
     * @return
     */
    @GetMapping("/getExport")
    @ResponseBody
    public Object getExportData(HttpServletResponse response){
        List<TbUser> list = easyService.findAll();
        try {
            ExcelUtil.writeExcel(response,list,"导出用户","没有设定sheet名称", ExcelTypeEnum.XLSX,TbUser.class);
        } catch (ExcelException e) {
          e.printStackTrace();
        }
        return "导出成功";
    }

    /**
     * 导入      Excel
     * @param request
     * @return
     */
    @PostMapping("/importExcel")
    @ResponseBody
    public Object importExcel(MultipartHttpServletRequest request){
        //获取文件名字
        Iterator<String> itr = request.getFileNames();
        //是否还有下一个
        String uploadedFile = itr.next();
        //解析获取的文件
        List<MultipartFile> files = request.getFiles(uploadedFile);
        //判断是否为空
        if (CollectionUtils.isEmpty(files)) {
            return fail("请选择文件！");
        }
        try {
            //解析返回一个list  集合
            List<TbUser> list = ExcelUtil.readExcel(files.get(0),TbUser.class);
            return (JSON.toJSONString(list, SerializerFeature.PrettyFormat));
        } catch (ExcelException e) {
            return fail(""+e.getMessage());
        }
    }

    /**
     * 导出csv格式
     * @param request
     * @param httpResponse
     * @throws IOException
     */
    @RequestMapping("/exportCsv")
    @ResponseBody
    public void exportCsv(HttpServletRequest request, HttpServletResponse httpResponse) throws IOException {
        List<String> dataList = new ArrayList<String>();
        String reportName = "csv-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".csv";
        try {
            dataList.add("1,2,3,4,5,6,7,8");
            dataList.add("11,12,13,14,15,16,17,18");
            dataList.add("一,二,三,四,五,六,七,八");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CSVUtils.exportCSV(httpResponse, dataList, reportName);
        }
    }

    /**
     * 导出easyExcel
     *
     * @param uptype
     *
     * @param response
     * @param request
     */
    @GetMapping(value = "/downLoadCsv")
    public void csvweek(  /*@RequestParam(name="s_date") String s_date,
                          @RequestParam(name="asi") String asi,
                          @RequestParam(name="ry") String ry,
                          @RequestParam(name="uptype") String uptype,
                          @RequestParam(name="page", defaultValue = "0") Integer page,
                          @RequestParam(name="size", defaultValue = "10") Integer size,*/
                          @RequestParam(name="uptype") String uptype,
                          HttpServletResponse response,
                          HttpServletRequest request) {
        try {
            // 构造导出数据结构
            String titles = "ID,用户名,密码,电话,邮箱,创建时间,更新时间";  // 设置表头
            String keys = "id,username,password,phone,email,created,updated";  // 设置每列字段
            // 构造导出数据
            List<Map<String, Object>> datas = new ArrayList<>();
            Map<String, Object> map = null;
            // 设置导出文件前缀
            String fName = null;
            // 查询需要导出的数据
            Page<TbUser> pages=null;
            List<TbUser> list=null;
            if ("1".equals(uptype)) {
                fName = "用户全部导出";
                //查询全部并导出
                //pages = easyService.findAll();
                list=easyService.findAll();
            } else {
                //分页导出
                fName = "用户全部导出";
                //pages=ipv4ServiceService.selectAll2(s_date,asi,ry,page,size);
                list=easyService.findAll();

            }

            if (null != list){
                for (TbUser data : list) {
                    map = new HashMap<>();
                    map.put("id", data.getId());
                    map.put("username", data.getUsername());
                    map.put("password", data.getPassword());
                    map.put("phone", data.getPhone());
                    map.put("email", data.getEmail()==null? "":data.getEmail());
                    map.put("created", data.getCreated());
                    map.put("updated", data.getUpdated());
                    datas.add(map);
                }
            }
            // 文件导出
            OutputStream os = response.getOutputStream();
            CsvExportUtil.responseSetProperties(fName, response);
            CsvExportUtil.doExport(datas, titles, keys, os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
