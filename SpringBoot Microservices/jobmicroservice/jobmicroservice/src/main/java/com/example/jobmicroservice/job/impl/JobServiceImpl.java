package com.example.jobmicroservice.job.impl;


import com.example.jobmicroservice.job.Job;
import com.example.jobmicroservice.job.JobRepository;
import com.example.jobmicroservice.job.JobService;
import com.example.jobmicroservice.job.client.CompanyClient;
import com.example.jobmicroservice.job.client.ReviewClient;
import com.example.jobmicroservice.job.external.Company;
import com.example.jobmicroservice.job.external.Review;
import com.example.jobmicroservice.job.maper.JobMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.jobmicroservice.job.dto.JobDTO;

/*
@Service
public class JobServiceImpl implements JobService {
    //private List<Job> jobs = new ArrayList<>();
    //private Long nextId = 1L; // No need for explicit casting to Long
    @Autowired
    JobRepository jobRepository;
    private Long nextId = 1L;



    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
    }
    //@Transactional
    @Override
    public void createJob(Job job) {
        job.setId(nextId++); // Assign a unique ID to the job before adding it
        jobRepository.save(job);
    }
    @Override
    public Job getJobById(Long id) {
        /*
        for (Job job : jobs) {
            if (job.getId().equals(id)) {
                return job;
            }
        }
        return null;

            return jobRepository.findById(id).orElse(null);


    }
    //@Transactional
    @Override
    public boolean deleteJobById(Long id) {
        /*
        Iterator<Job> iterator = jobs.iterator();
        while (iterator.hasNext()) {
            Job job = iterator.next();
            if (job.getId().equals(id)) {
                iterator.remove();
                return true;
            }
        }
        return false;


        try {
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /*
    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        for (Job job : jobs) {
            if (job.getId().equals(id)) {
                job.setTitle(updatedJob.getTitle());
                job.setDescription(updatedJob.getDescription());
                job.setMinSalary(updatedJob.getMinSalary());
                job.setMaxSalary(updatedJob.getMaxSalary());
                job.setLocation(updatedJob.getLocation());
                return true;
            }
        }
        return false;
    }
    //@Transactional
    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setLocation(updatedJob.getLocation());
            jobRepository.save(job);
            return true;
        }
        return false;
    }







}
*/
@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private CompanyClient companyClient;
    @Autowired
    private ReviewClient reviewClient;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JobRepository jobRepository;
    /*
    @Override
    public List<JobwithCompanyDTO> findAll() {

        List<Job> jobs = jobRepository.findAll();
        List<JobwithCompanyDTO> jobWithCompanyDTOs = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();

        for (Job job : jobs) {
            JobwithCompanyDTO jobWithCompanyDTO = new JobwithCompanyDTO();
            jobWithCompanyDTO.setJob(job);

            Company company = restTemplate.getForObject(
                    "http://localhost:8081/companies/" + job.getCompanyId(),
                    Company.class
            );
            jobWithCompanyDTO.setCompany(company);

            jobWithCompanyDTOs.add(jobWithCompanyDTO);
        }
        return jobWithCompanyDTOs;

    }*/
    /*
    public List<JobwithCompanyDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobwithCompanyDTO> jobWithCompanyDTOs = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();

        for (Job job : jobs) {
            JobwithCompanyDTO jobWithCompanyDTO = new JobwithCompanyDTO();
            jobWithCompanyDTO.setJob(job);

            // Check if companyId is null before making the REST API call
            if (job.getCompanyId() != null) {
                try {
                    Company company = restTemplate.getForObject(
                            "http://localhost:8081/companies/" + job.getCompanyId(),
                            Company.class
                    );
                    jobWithCompanyDTO.setCompany(company);
                } catch (Exception e) {
                    System.out.println("Error fetching company details for Job ID: " + job.getId());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Warning: Job ID " + job.getId() + " has a null company ID.");
            }

            jobWithCompanyDTOs.add(jobWithCompanyDTO);
        }
        return jobWithCompanyDTOs;
    }*/
    @Override
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();

        return jobs.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private JobDTO convertToDto(Job job) {
        JobDTO jobWithCompanyDTO = new JobDTO();

        if (job != null && job.getCompanyId() != null) {
            try {
                /*
                Company company = restTemplate.getForObject(
                        "http://companymicroservice:8081/companies/" + job.getCompanyId(),
                        Company.class
                );*/
                Company company = companyClient.getCompany(job.getCompanyId());
                List<Review> reviews = reviewClient.getReviews(job.getCompanyId());


                /*
                ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
                        "http://reviewmicroservice:8083/reviews?companyId=" + job.getCompanyId(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Review>>() {}
                );*/

                //List<Review> reviews = reviewResponse.getBody();
                if (reviews == null) {
                    reviews = new ArrayList<>();
                }

                jobWithCompanyDTO = JobMapper.mapToJobWithCompanyDto(job, company, reviews);

            } catch (Exception e) {
                System.out.println("Error fetching company or review details for Job ID: " + job.getId());
                e.printStackTrace();
            }
        } else {
            System.out.println("Warning: Job ID " + (job != null ? job.getId() : "UNKNOWN") + " has a null company ID.");
        }

        return jobWithCompanyDTO;
    }





    @Override
    @Transactional
    public void createJob(Job job) {
        if (job.getCompanyId() == null) {
            throw new IllegalArgumentException("companyId cannot be null");
        }
        jobRepository.save(job);

    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);

        if (job == null) {  // Handle null before conversion
            return null;
        }

        return convertToDto(job);
    }



    @Override
    @Transactional
    public boolean deleteJobById(Long id) {
        try {
            jobRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Job not found");
        }
    }

    @Override
    @Transactional
    public boolean updateJob(Long id, Job updatedJob) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));

        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setMinSalary(updatedJob.getMinSalary());
        existingJob.setMaxSalary(updatedJob.getMaxSalary());
        existingJob.setLocation(updatedJob.getLocation());

        jobRepository.save(existingJob);
        return true;
    }
}
