package com.util.commons.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tb_user")
public class TbUser extends BaseRowModel {
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  @ExcelProperty(index = 0 ,value = "ID")
  private String             id;
  @ExcelProperty(index = 1 ,value = "用户名")
  private String             username;
  @ExcelProperty(index = 2 ,value = "密码")
  private String             password;
  @ExcelProperty(index = 3 ,value = "电话")
  private String             phone;
  @ExcelProperty(index = 4 ,value = "邮箱")
  private String             email;
  @ExcelProperty(index = 4 ,value = "创建时间")
  private Date               created;
  @ExcelProperty(index = 4 ,value = "更新时间")
  private Date               updated;

/*无参构造方法是导出必要存在的*/
  public TbUser() {
  }
}
