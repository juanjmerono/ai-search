package dev.example.services;

import static java.util.Arrays.asList;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

@Entity
@Indexed
@Table(name="SERVICES", schema="API_SERVICES")
public class Service {

    @Id
    @Column(name="ID", unique = true, nullable = false, length = 25)
    private String id;

    @Column(name="NAME", nullable = false, length = 250)
    @Field(termVector = TermVector.YES)
    private String name;

    @Column(name="DESC", nullable = false, length = 4000)
    @Field(termVector = TermVector.YES)
    private String desc;

    @Column(name="URL", nullable = false, length = 512)
    private String url;

    private Service() {}

    private Service(String id, String name, String url, String desc) {
        this.id = id; this.name = name; this.url = url; this.desc = desc;
    }

    public static Service of(String id, String name, String url, String desc) {
        return new Service(id, name, url, desc);
    }

    public String id() { return id; }
    public String desc() { return desc; }
    public String url() { return url; }
    public String name() { return name; }
    public List<String> nameAndDescList() { return asList(name,desc); }
}
