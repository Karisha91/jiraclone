package com.ivan.jiraclone.Repository;

import com.ivan.jiraclone.model.Project;
import com.ivan.jiraclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByWorkspaceId(Long workspaceId);
}
