package br.com.javapi.beertime.tsrd.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode
@Table(name="INSTRUMENT_PRICE_MODIFIER")
public class InstrumentPriceModifier {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="modifier")
    private double modifier;
}
