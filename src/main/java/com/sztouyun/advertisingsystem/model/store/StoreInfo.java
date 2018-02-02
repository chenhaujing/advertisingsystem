package com.sztouyun.advertisingsystem.model.store;

import com.sztouyun.advertisingsystem.common.Constant;
import com.sztouyun.advertisingsystem.utils.UUIDUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * 门店网点
 */
@Entity
@SqlResultSetMapping(
        name = Constant.STOREINFO_SELECT,
        classes = @ConstructorResult(
                targetClass = StoreInfo.class,
                columns = {
                        @ColumnResult(name = "id",type= String.class),
                        @ColumnResult(name = "store_no",type= String.class),
                        @ColumnResult(name = "store_name",type= String.class),
                        @ColumnResult(name = "equipment_code",type= String.class),
                        @ColumnResult(name = "store_address",type= String.class),
                        @ColumnResult(name = "city_id",type= String.class),
                        @ColumnResult(name = "usedCount",type= Integer.class),
                        @ColumnResult(name = "isChoose",type= Integer.class)
                }))
public class StoreInfo {

    @Id
    @Column(nullable = false,length = 36)
    private String id = UUIDUtils.generateOrderedUUID();
    /**
     * 门店编号
     */
    @Column(nullable = false,length = 50)
    private String storeNo;
    /**
     * 门店名称
     */
    @Column(nullable = false,length = 50)
    private String storeName;
    /**
     * 门店类型（动态计算）
     */
    @Column
    private int storeType;
    /**
     * 设备编码
     */
    @Column(nullable = false,length = 50)
    private String equipmentCode;
    /**
     * 门店地址
     */
    @Column(nullable = false,length = 100)
    private String storeAddress;
    /**
     * 省份id
     */
    @Column(length=36,name = "province_id")
    private String provinceId;
    /**
     * 城市id
     */
    @Column(length=36,name = "city_id")
    private String cityId;
    /**
     * 区域id
     */
    @Column(length=36,name = "region_id")
    private String regionId;
    /**
     * 商品数量（动态计算）
     */
    @Column(length = 20)
    private Integer commodityCount;

    /**
     * 交易数量
     */
    @Column(length = 20)
    private Integer transactionCount;

    @Column(nullable = false,updatable = false)
    @CreatedDate
    private Date createdTime = new Date();

    @Column(nullable = false)
    @LastModifiedDate
    private Date updatedTime = new Date();
    /**
     *  已使用广告位数量
     */
    @Transient
    private Integer usedCount;
    /**
     *  是否已选择
     */
    @Transient
    private Integer isChoose;

    /**
     *  开店时间
     */
    @Column
    private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStoreType() {
        return storeType;
    }

    public void setStoreType(int storeType) {
        this.storeType = storeType;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public Integer getCommodityCount() {
        return commodityCount;
    }

    public void setCommodityCount(Integer commodityCount) {
        this.commodityCount = commodityCount;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Integer getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(Integer isChoose) {
        this.isChoose = isChoose;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public StoreInfo(String id, String storeNo, String storeName, String equipmentCode, String storeAddress, String cityId, Integer usedCount, Integer isChoose) {
        this.id = id;
        this.storeNo = storeNo;
        this.storeName = storeName;
        this.equipmentCode = equipmentCode;
        this.storeAddress = storeAddress;
        this.cityId = cityId;
        this.usedCount = usedCount;
        this.isChoose = isChoose;
    }

    public StoreInfo() {
    }
}
