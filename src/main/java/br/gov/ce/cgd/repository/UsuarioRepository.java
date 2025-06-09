package br.gov.ce.cgd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.gov.ce.cgd.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
//    Optional<Usuario> findByCpf(String cpf);
//    Optional<Usuario> findByEmail(String email);
//    Optional<Usuario> findByCodigoValidador(int codigoValidador);
//    List<Usuario> findByNomeContaining(String nome, Sort sort);
//    List<Usuario> findByCpfContaining(String cpf, Sort sort);
//    
    @Query("SELECT u FROM Usuario u ORDER BY u.id ASC")
    List<Usuario> findAllUsuariosOrderById();
	
	Optional<Usuario> findByCpf(String cpf);
    Optional<Usuario> findByEmail(String email);
//    Optional<Usuario> findByCodigoValidador(int codigoValidador);

    Page<Usuario> findByNomeContaining(String nome, Pageable pageable);
    Page<Usuario> findByCpfContaining(String cpf, Pageable pageable);

    @Query("SELECT u FROM Usuario u ORDER BY u.id ASC")
    Page<Usuario> findAllUsuarios(Pageable pageable);
}
