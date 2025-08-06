package cn.com.ctsi.dict.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("aes_dict")
public final class AesDictDto implements Serializable {
    private static final long serialVersionUID = -5008296536373087914L;
    //"序号"
    private Long seqId;
    //"编码值"
    private String dictCode;
    //"字典内容"
    private String dictName;
    //"映射名称"
    private String shineName;
    //"分类"
    private String dictType;
    //"排序"
    private String dictOrder;
    //"备注解释"
    private String comment;
    //"北向对应编码值"
    private String northCode;
    //"入库时间"
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

}
