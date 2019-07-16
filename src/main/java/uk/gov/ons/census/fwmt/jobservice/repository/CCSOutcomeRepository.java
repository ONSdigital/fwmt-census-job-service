package uk.gov.ons.census.fwmt.jobservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.ons.census.fwmt.jobservice.entity.CCSOutcomeEntity;

@Repository
public interface CCSOutcomeRepository extends CrudRepository<CCSOutcomeEntity, String> {
}