package br.gov.ce.cgd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.ce.cgd.entity.Cartorio;

public interface CartorioRepository extends JpaRepository<Cartorio, Long>{
    List<Cartorio> findByCartorioAtivoTrue();

}
