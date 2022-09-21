package entity;

import lombok.Data;
import lombok.ToString;

/**
 * Json 数据对应的实体类
 */
@Data
public class DataEntity {
    private String transportId;
    private String plateNo;
    private String orderType;
    private String type;
}
