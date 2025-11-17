package org.example.apilogin.data.entities;

import jakarta.persistence.*;
import org.example.apilogin.common.Constantes;

@Entity
@Table(name = Constantes.TABLE_RENOS)
public class RenoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private String nombre;
    private String color;
    private Boolean cuernos;

    public RenoEntity() {}

    public RenoEntity(Integer id, Integer userId, String nombre, String color, Boolean cuernos) {
        this.id = id;
        this.userId = userId;
        this.nombre = nombre;
        this.color = color;
        this.cuernos = cuernos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getCuernos() {
        return cuernos;
    }

    public void setCuernos(Boolean cuernos) {
        this.cuernos = cuernos;
    }
}
