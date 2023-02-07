package fr.tse.poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Imputation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID of the imputation", name = "idImputation", required = true, value = "123")
    private Long idImputation;

    @NotNull
    @ApiModelProperty(notes = "Date of the imputation", name = "dateImputation", required = true, value = "2023-01-12")
    private LocalDate dateImputation;

    @NotNull
    @ApiModelProperty(notes = "Daily charge of the imputation", name = "dailyChargeImputation", required = true, value = "0.5")
    private Double dailyChargeImputation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idProject", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ApiModelProperty(notes = "Project of the imputation", name = "project", required = true)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUser", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ApiModelProperty(notes = "User of the imputation", name = "user", required = true)
    private User user;

}
