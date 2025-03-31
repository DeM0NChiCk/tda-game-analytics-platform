package ru.itis.tdagameanalytics.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.itis.tdagameanalytics.model.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {

    List<Project> findAllByUserId(String userId);

    Optional<Project> findByIdAndUserId(String projectId, String userId);
}
