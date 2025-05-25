package com.example.jobmicroservice.job.maper;

import com.example.jobmicroservice.job.Job;
import com.example.jobmicroservice.job.dto.JobDTO;
import com.example.jobmicroservice.job.external.Company;
import com.example.jobmicroservice.job.external.Review;

import java.util.List;

public class JobMapper {
    public static JobDTO mapToJobWithCompanyDto(
            Job job,
            Company company, List<Review> reviews) {

        JobDTO jobWithCompanyDTO = new JobDTO();
        jobWithCompanyDTO.setId(job.getId());
        jobWithCompanyDTO.setTitle(job.getTitle());
        jobWithCompanyDTO.setDescription(job.getDescription());
        jobWithCompanyDTO.setLocation(job.getLocation());
        jobWithCompanyDTO.setMaxSalary(job.getMaxSalary());
        jobWithCompanyDTO.setMinSalary(job.getMinSalary());
        jobWithCompanyDTO.setCompany(company);
        jobWithCompanyDTO.setReviews(reviews);

        return jobWithCompanyDTO;
    }

}
