package com.taurus.auction.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Clenio on 22/01/2018.
 */
@Entity
@Table(name = "tb_produto")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String name;

    @Column(name = "descricao")
    private String description;

    @Column(name = "inicio_fornecimento")
    private String supply_start;

    @Column(name = "fim_fornecimento")
    private String supply_end;

    @Column(name = "preco_reserva")
    private Long reservation_price;

    @Column(name = "preco_max")
    private Long max_price;

    @Column(name = "preco_min")
    private Long min_price;

    @Column(name = "exibir_preco_max")
    private char show_max_price;

    @Column(name = "exibir_preco_min")
    private char show_min_price;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User user;

    private char status;

    @Column(name = "quantidade_disponivel")
    private Long availabilityQuantity;

    @Column(name = "quantidade_total")
    private Long totalQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupply_start() {
        return supply_start;
    }

    public void setSupply_start(String supply_start) {
        this.supply_start = supply_start;
    }

    public String getSupply_end() {
        return supply_end;
    }

    public void setSupply_end(String supply_end) {
        this.supply_end = supply_end;
    }

    public Long getReservation_price() {
        return reservation_price;
    }

    public void setReservation_price(Long reservation_price) {
        this.reservation_price = reservation_price;
    }

    public Long getMax_price() {
        return max_price;
    }

    public void setMax_price(Long max_price) {
        this.max_price = max_price;
    }

    public Long getMin_price() {
        return min_price;
    }

    public void setMin_price(Long min_price) {
        this.min_price = min_price;
    }

    public char getShow_max_price() {
        return show_max_price;
    }

    public void setShow_max_price(char show_max_price) {
        this.show_max_price = show_max_price;
    }

    public char getShow_min_price() {
        return show_min_price;
    }

    public void setShow_min_price(char show_min_price) {
        this.show_min_price = show_min_price;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public Long getAvailabilityQuantity() {
        return availabilityQuantity;
    }

    public void setAvailabilityQuantity(Long availabilityQuantity) {
        this.availabilityQuantity = availabilityQuantity;
    }

}
