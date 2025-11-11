package com.akriti.akriti.modules.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Table(name = "user_details")
@Entity
public class UserDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String feeCharge;
    private String nmcNumber;
    private String bio;

    @ElementCollection
    @CollectionTable(name = "user_organizations", joinColumns = @JoinColumn(name = "user_details_id"))
    private List<Organizations> organizations;

    @ElementCollection
    @CollectionTable(name = "user_education", joinColumns = @JoinColumn(name = "user_details_id"))
    private List<Education> education;

    @ElementCollection
    @CollectionTable(name = "user_certificates", joinColumns = @JoinColumn(name = "user_details_id"))
    private List<Certificates> certificates;
}

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
class Organizations{

    @Column(name="organization_name", nullable = false)
    private String organization;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;


    @Column(name = "end_date", nullable = true)
    private LocalDate endDate;
    private String bio;
}

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
class Education{

    @Column(name = "degree_name", nullable = false)
    private String degreeName;

    @Column(nullable = false)
    private String university;

    @Column(nullable = false)
    private String college;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    private String description;
}

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
class Certificates{
    @Column(name = "certificate_name", nullable = false)
    private String certificateName;

    @Column(name = "document", nullable = false)
    private String document;
}

