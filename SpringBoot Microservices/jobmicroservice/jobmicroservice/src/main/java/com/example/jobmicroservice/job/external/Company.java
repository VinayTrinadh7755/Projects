package com.example.jobmicroservice.job.external;



public class Company {

    private Long id;

    private String name;

    private String description;
    /*
    @JsonIgnore
    @OneToMany(mappedBy = "company")
    private List<Job> jobs;
    */



    /*
    @OneToMany(mappedBy = "company")
    private List<Review> reviews;
    */
    public Company() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}

