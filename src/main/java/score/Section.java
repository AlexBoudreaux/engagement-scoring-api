package score;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

@Entity
public class Section extends PanacheEntity {

  public String name;
  public Long weight;

  @OneToMany(mappedBy = "section")
  public List<PossibleResponse> possibleResponses = new ArrayList<>();;

  @ManyToOne
  @JoinColumn(name = "FK_TemplateId")
  private Template template;

  public void setTemplate(Template t) {
    this.template = t;
  }
}
