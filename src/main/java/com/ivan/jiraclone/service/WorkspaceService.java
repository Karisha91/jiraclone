package com.ivan.jiraclone.service;


import com.ivan.jiraclone.Repository.WorkspaceRepository;
import com.ivan.jiraclone.dto.*;
import com.ivan.jiraclone.enums.SubscriptionStatus;
import com.ivan.jiraclone.exception.ResourceAlreadyExistsException;
import com.ivan.jiraclone.exception.ResourceNotFoundException;
import com.ivan.jiraclone.exception.UnauthorizedException;
import com.ivan.jiraclone.model.User;
import com.ivan.jiraclone.model.Workspace;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    public WorkspaceService(WorkspaceRepository workspaceRepository, UserService userService, NotificationService notificationService) {
        this.workspaceRepository = workspaceRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }


        public WorkspaceResponse createWorkspace(WorkspaceRequest  workspaceRequest, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        int workspaceCount = workspaceRepository.countByOwnerOrMembers(user);
            System.out.println("Workspace count for " + user.getUsername() + ": " + workspaceCount);

        if (workspaceCount >= 3 && user.getSubscriptionStatus() != SubscriptionStatus.PREMIUM) {
            throw new UnauthorizedException("You have reached the maximum number of workspaces for your subscription plan. Please upgrade to create more workspaces.");
        }
        Workspace workspace = new Workspace();

        workspace.setName(workspaceRequest.getName());
        workspace.setDescription(workspaceRequest.getDescription());
        workspace.setCreatedAt(LocalDateTime.now());
        workspace.setOwner(user);
        return convertWorkspace(workspaceRepository.save(workspace));
    }


    public WorkspaceResponse convertWorkspace(Workspace  workspace) {
        User owner = workspace.getOwner();
        WorkspaceResponse workspaceResponse = new WorkspaceResponse();
        workspaceResponse.setName(workspace.getName());
        workspaceResponse.setDescription(workspace.getDescription());
        workspaceResponse.setCreatedAt(workspace.getCreatedAt());
        workspaceResponse.setId(workspace.getId());
        OwnerSummary ownerSummary = new OwnerSummary();
        ownerSummary.setUsername(owner.getUsername());
        ownerSummary.setId(owner.getId());
        workspaceResponse.setOwner(ownerSummary);
        workspaceResponse.setMembers(workspace.getMembers().stream().map(member -> {
            MemberSummary memberSummary = new MemberSummary();
            memberSummary.setUsername(member.getUsername());
            memberSummary.setId(member.getId());
            memberSummary.setAvatarUrl(member.getAvatarUrl());
            return memberSummary;
        }).collect(Collectors.toList()));
        workspaceResponse.setProjects(workspace.getProjects().stream().map(project -> {
            ProjectSummary projectSummary = new ProjectSummary();
            projectSummary.setId(project.getId());
            projectSummary.setName(project.getName());
            return projectSummary;
        }).collect(Collectors.toList()));

        return workspaceResponse;



    }
    @Transactional
    public List<WorkspaceResponse> getWorkspaces(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Set<Workspace> workspaces = workspaceRepository.findByOwnerOrMembers(user, user);
        return workspaces.stream().map(this::convertWorkspace).collect(Collectors.toList());
    }
    @Transactional
    public WorkspaceResponse getWorkspace(Long id) {
        return workspaceRepository.findById(id)
                .map(this::convertWorkspace)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
    }

    @Transactional
    public WorkspaceResponse updateWorkspace(Long id, WorkspaceRequest  workspaceRequest, Principal principal) {

        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
        if  (!Objects.equals(principal.getName(), workspace.getOwner().getUsername())) {

            throw new UnauthorizedException("You are not owner of this workspace");
        }
        workspace.setName(workspaceRequest.getName());
        workspace.setDescription(workspaceRequest.getDescription());
        return convertWorkspace(workspaceRepository.save(workspace));


    }
    @Transactional
    public void deleteWorkspace(Long id, Principal principal) {
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
        if  (!Objects.equals(principal.getName(), workspace.getOwner().getUsername())) {
            throw new UnauthorizedException("You are not owner of this workspace");

        }
        workspaceRepository.delete(workspace);

    }
    @Transactional
    public MemberSummary addMemberToWorkspace(Long id, UserDTO userDTO, Principal principal) {
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
        if (!Objects.equals(principal.getName(), workspace.getOwner().getUsername())) {
            throw new UnauthorizedException("You are not owner of this workspace");
        }

        User user = userService.findByUsername(userDTO.getUsername());
        int workspaceCount = workspaceRepository.countByOwnerOrMembers(user);
        if (workspaceCount >= 3 && user.getSubscriptionStatus() != SubscriptionStatus.PREMIUM) {
            throw new UnauthorizedException("Member have reached the maximum number of workspaces for their subscription plan.");
        }
        if (workspace.getMembers().contains(user)) {
            throw new ResourceAlreadyExistsException("User is already a member of this workspace");
        }
        MemberSummary memberSummary = new MemberSummary();
        memberSummary.setUsername(user.getUsername());
        memberSummary.setId(user.getId());
        memberSummary.setAvatarUrl(user.getAvatarUrl());

        workspace.getMembers().add(user);
        workspaceRepository.save(workspace);
        notificationService.sendNotification(user.getId(),workspace.getOwner().getUsername() + " added you to: " + workspace.getName(), null, workspace.getId());
        return memberSummary;
    }
    @Transactional
    public MemberSummary removeMemberFromWorkspace(Long id,UserDTO userDTO, Principal principal) {
        Workspace workspace = workspaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
        if (!Objects.equals(principal.getName(), workspace.getOwner().getUsername())) {
            throw new UnauthorizedException("You are not owner of this workspace");
        }
        User user = userService.findByUsername(userDTO.getUsername());
        MemberSummary memberSummary = new MemberSummary();
        memberSummary.setUsername(user.getUsername());
        memberSummary.setId(user.getId());
        memberSummary.setAvatarUrl(user.getAvatarUrl());
        if (!workspace.getMembers().contains(user)) {
           throw new ResourceNotFoundException("There is no such user in this workspace");
        }
        workspace.getMembers().remove(user);
        workspaceRepository.save(workspace);
        return memberSummary;




    }
    @Transactional
    public Workspace getRealWorkspace(Long id) {
      return workspaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
    }


}
