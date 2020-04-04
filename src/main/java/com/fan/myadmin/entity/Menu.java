package com.fan.myadmin.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.sql.Update;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

/**
 * @author fanweiwei
 * @create 2020-04-01 20:19
 */
@Entity
@Getter
@Setter
@Table(name = "menu")
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {Update.class})
    private Long id;

    @NotBlank
    private String name;

    @Column(unique = true)
    private Long sort = 999L;

    @Column(name = "path")
    private String path;

    /** 类型，目录、菜单、按钮 */
    @Column(name = "type")
    private Integer type;

    /** 权限 */
    @Column(name = "permission")
    private String permission;

    @Column(columnDefinition = "bit(1) default 0")
    private Boolean hidden;

    /** 上级菜单ID */
    @Column(name = "pid",nullable = false)
    private Long pid;

    @ManyToMany(mappedBy = "menus")
    @JsonIgnore
    private Set<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
