package ru.skyPro.serviceBankRecommendation3.model.rulesEntity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rules")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String queryName;
    @Lob
    @Column(name = "arguments", columnDefinition = "bytea")
    private byte[] arguments;
    @Column(name = "negate")
    private boolean negate;
    @Column(name = "query")
    private String query;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "recommend_is")
    private Recommendation recommendation;

    public Rule(Long id, String name, byte[] arguments, boolean negate, String query) {
        this.id = id;
        this.queryName = name;
        this.arguments = arguments;
        this.negate = negate;
        this.query = query;
    }

    public Rule(String name, byte[] arguments, boolean negate) {
        this.queryName = name;
        this.arguments = arguments;
        this.negate = negate;
        this.query = query;
    }

    public Rule(Long id, String queryName, String query) {
        this.id = id;
        this.queryName = queryName;
        this.query = query;
    }

    public Rule(Long id, String queryName, byte[] arguments, String query) {
        this.id = id;
        this.queryName = queryName;
        this.arguments = arguments;
        this.query = query;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setArguments(byte[] arguments) {
        this.arguments = arguments;
    }

    public byte[] getArguments() {
        return arguments;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public boolean isNegate() {
        return negate;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }
}
