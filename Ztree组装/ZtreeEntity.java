package  com.leimingtech.base.entity.vo;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by liuzhen on 2017/4/13.
 */
public class ZtreeEntity implements RowMapper<ZtreeEntity> {

    private String id;
    private String parentId;//父菜单
    private String name;//菜单名称

    @Override
    public ZtreeEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        ZtreeEntity z = new ZtreeEntity();
        z.setId(resultSet.getString("id"));
        z.setName(resultSet.getString("name"));
        z.setParentId(resultSet.getString("parentId"));
        return z;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
