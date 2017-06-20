package br.com.javapi.beertime.tsrd.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.javapi.beertime.tsrd.entity.InstrumentPriceModifier;

public interface InstrumentPriceModifierRepository extends CrudRepository<InstrumentPriceModifier, Integer> {

	List<InstrumentPriceModifier> findAll();
}
