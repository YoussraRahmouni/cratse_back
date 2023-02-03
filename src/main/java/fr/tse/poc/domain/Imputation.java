package fr.tse.poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Imputation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImputation;

    private LocalDate dateImputation;

    private Double dailyChargeImputation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idProject", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUser", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

}
