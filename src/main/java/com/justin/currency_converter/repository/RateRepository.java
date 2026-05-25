package com.justin.currency_converter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.justin.currency_converter.model.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate,Long> {
    @Query("SELECT r FROM Rate r WHERE r.code = :code")
    public Rate findByCode(String code);
}
