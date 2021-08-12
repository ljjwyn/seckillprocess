package com.glodon.groupsix.seckillprocess.models.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_seckill_record
 * @author
 */
@ApiModel(value="TSeckillRecord", description = "秒杀记录实体类")
@Data
public class TSeckillRecord implements Serializable {
    private Integer id;

    private String phone;

    private String commodityId;

    private String commodityName;

    private String seckillPrice;

    private Date seckillTime;

    private String status;

    private static final long serialVersionUID = 1L;

    public TSeckillRecord(String phone, String commodityId, String commodityName, String seckillPrice, Date seckillTime, String status) {
        this.phone = phone;
        this.commodityId = commodityId;
        this.commodityName = commodityName;
        this.seckillPrice = seckillPrice;
        this.seckillTime = seckillTime;
        this.status = status;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TSeckillRecord other = (TSeckillRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getCommodityId() == null ? other.getCommodityId() == null : this.getCommodityId().equals(other.getCommodityId()))
            && (this.getCommodityName() == null ? other.getCommodityName() == null : this.getCommodityName().equals(other.getCommodityName()))
            && (this.getSeckillPrice() == null ? other.getSeckillPrice() == null : this.getSeckillPrice().equals(other.getSeckillPrice()))
            && (this.getSeckillTime() == null ? other.getSeckillTime() == null : this.getSeckillTime().equals(other.getSeckillTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getCommodityId() == null) ? 0 : getCommodityId().hashCode());
        result = prime * result + ((getCommodityName() == null) ? 0 : getCommodityName().hashCode());
        result = prime * result + ((getSeckillPrice() == null) ? 0 : getSeckillPrice().hashCode());
        result = prime * result + ((getSeckillTime() == null) ? 0 : getSeckillTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", phone=").append(phone);
        sb.append(", commodityId=").append(commodityId);
        sb.append(", commodityName=").append(commodityName);
        sb.append(", seckillPrice=").append(seckillPrice);
        sb.append(", seckillTime=").append(seckillTime);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
