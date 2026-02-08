package com.blog01.backend.admin.response;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardResponse {

    private long totalUsers;
    private long totalPosts;
    private long totalComments;
    private long totalReports;

    private Map<String, Long> reportsByType;
}
