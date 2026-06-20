package com.project.fitness.service;

import com.project.fitness.dto.ActivityRequest;
import com.project.fitness.dto.ActivityResponse;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.model.Activity;
import com.project.fitness.model.User;
import com.project.fitness.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ModelMapper modelMapper;

    public  ActivityResponse trackActivity(ActivityRequest request) {

        Activity savedActivity = modelMapper.map(request,Activity.class);
        savedActivity = activityRepository.save(savedActivity);
        return modelMapper.map(savedActivity,ActivityResponse.class);


    }

    public  List<ActivityResponse> getUserActivities(@RequestHeader(value = "X-User-ID") String userId) {

        List<Activity> activities = activityRepository.findByUserId(userId);

        return activities
                .stream()
                .map(activity -> modelMapper.map(activity, ActivityResponse.class))
                .collect(Collectors.toList());
    }
}

