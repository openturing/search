package com.viglet.turing.persistence.repository.sn;

import com.viglet.turing.persistence.model.sn.TurSNSite;
import com.viglet.turing.persistence.model.sn.TurSNSiteField;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TurSNSiteFieldRepository extends JpaRepository<TurSNSiteField, String> {

	List<TurSNSiteField> findAll();

	@Cacheable("turSNSiteFieldfindById")
	Optional<TurSNSiteField> findById(String id);

	@Cacheable("turSNSiteFieldfindByTurSNSite")
	List<TurSNSiteField> findByTurSNSite(TurSNSite turSNSite);

	@CacheEvict(value = { "turSNSiteFieldfindById", "turSNSiteFieldfindByTurSNSite" }, allEntries = true)
	TurSNSiteField save(TurSNSiteField turSNSiteField);

	@CacheEvict(value = { "turSNSiteFieldfindById", "turSNSiteFieldfindByTurSNSite" }, allEntries = true)
	void delete(TurSNSiteField turSNSiteField);

	@Modifying
	@Query("delete from TurSNSiteField ssf where ssf.id = ?1")
	@CacheEvict(value = { "turSNSiteFieldfindById", "turSNSiteFieldfindByTurSNSite" }, allEntries = true)
	void delete(String turSnSiteFieldId);
}
