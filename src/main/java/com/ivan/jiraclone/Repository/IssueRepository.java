package com.ivan.jiraclone.Repository;

import com.ivan.jiraclone.enums.Status;
import com.ivan.jiraclone.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByProjectId(Long projectId);

    List<Issue> findByStatus(Status status);

    @Query("SELECT i FROM Issue i WHERE i.project.id = :projectId and i.status = :status")
    List<Issue> findByProjectIdAndStatus(@Param("projectId") Long projectId, @Param("status") Status status);

    @Query("SELECT i FROM Issue i WHERE i.assignee.id = :assigneeId")
    List<Issue> findByAssignee(@Param("assigneeId") Long assigneeId);



}
