/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.guigoh.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author IPTI
 */
@Entity
@Table(name = "occupations_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OccupationsType.findAll", query = "SELECT o FROM OccupationsType o"),
    @NamedQuery(name = "OccupationsType.findById", query = "SELECT o FROM OccupationsType o WHERE o.id = :id"),
    @NamedQuery(name = "OccupationsType.findByName", query = "SELECT o FROM OccupationsType o WHERE o.name = :name")})
public class OccupationsType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "occupationsTypeId")
    private Collection<Occupations> occupationsCollection;

    public OccupationsType() {
    }

    public OccupationsType(Integer id) {
        this.id = id;
    }

    public OccupationsType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<Occupations> getOccupationsCollection() {
        return occupationsCollection;
    }

    public void setOccupationsCollection(Collection<Occupations> occupationsCollection) {
        this.occupationsCollection = occupationsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OccupationsType)) {
            return false;
        }
        OccupationsType other = (OccupationsType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.guigoh.entity.OccupationsType[ id=" + id + " ]";
    }
    
}
