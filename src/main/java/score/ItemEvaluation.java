package score;

import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

public class ItemEvaluation extends PanacheEntity {

  @ManyToOne
  @JoinColumn(name = "FK_TemplateId")
  public Template template;

  @ManyToMany
  public List<PossibleResponse> selectedResponses;
  
}
