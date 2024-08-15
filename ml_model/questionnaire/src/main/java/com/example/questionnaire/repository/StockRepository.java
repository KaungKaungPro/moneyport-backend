package com.example.questionnaire.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.questionnaire.entity.Stock;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
	
	


}