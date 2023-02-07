package fr.tse.poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "ID of the project", name = "idProject", required = true, value = "123")
    private Long idProject;

    @NotNull
    @ApiModelProperty(notes = "Name of the project", name = "nameProject", required = true, value = "POC")
    private String nameProject;

    @NotNull
    @ApiModelProperty(notes = "Forecast duration of the project", name = "durationForecastProject", required = true, value = "500.0")
    private Double durationForecastProject;

    @ApiModelProperty(notes = "Real duration of the project", name = "durationRealProject", value = "450.50")
    private Double durationRealProject;

}
