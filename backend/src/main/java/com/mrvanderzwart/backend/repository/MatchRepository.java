package com.mrvanderzwart.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrvanderzwart.backend.model.Matches;

@Repository
public interface MatchRepository extends JpaRepository<Matches,Integer> {

}