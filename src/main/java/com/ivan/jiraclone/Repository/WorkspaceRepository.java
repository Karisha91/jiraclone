package com.ivan.jiraclone.Repository;


import com.ivan.jiraclone.dto.MemberSummary;
import com.ivan.jiraclone.model.Issue;
import com.ivan.jiraclone.model.User;
import com.ivan.jiraclone.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {


    Set<Workspace> findByOwnerOrMembers(User owner, User member);


}
