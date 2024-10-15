package com.codegen.processor.vo;

import com.jpa.support.BaseJpaAggregate;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author: yp
 * @date: 2024/10/14 17:00
 * @description:
 */
public class AbstractBaseJpaVO {
    @Schema(
            title = "数据版本"
    )
    private int version;
    @Schema(
            title = "主键"
    )
    private Long id;
    @Schema(
            title = "创建时间"
    )
    private Long createdAt;
    @Schema(
            title = "修改时间"
    )
    private Long updatedAt;

    protected AbstractBaseJpaVO(BaseJpaAggregate source) {
        this.setVersion(source.getVersion());
        this.setId(source.getId());
        this.setCreatedAt(source.getCreatedAt().toEpochMilli());
        this.setUpdatedAt(source.getUpdatedAt().toEpochMilli());
    }

    protected AbstractBaseJpaVO() {
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
