package io.github.isaacbao.scaffold.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

/**
 * 一张表
 * Created by rongyang_lu on 2017/7/10.
 */
@Entity
@Table(name = "t_item")
public class Item extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column
    private Date createDate;

    @Column
    private Date updateDate;

    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    public Item() {
        this.id = UUID.randomUUID();
        this.createDate = new Date();
        this.updateDate = new Date();
    }

    public String getId() {
        return this.id.toString();
    }

    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUserId() {
        if (null != userId) {
            return userId.toString();
        }
        return null;
    }

    public void setUserId(String userId) {
        this.userId = UUID.fromString(userId);
    }

    public void setReferrerId(UUID referrerId) {
        this.userId = referrerId;
    }
}
