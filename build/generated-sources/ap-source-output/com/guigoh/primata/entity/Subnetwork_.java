package com.guigoh.primata.entity;

import com.guigoh.primata.entity.SocialProfile;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.3.2.v20111125-r10461", date="2013-10-26T09:17:13")
@StaticMetamodel(Subnetwork.class)
public class Subnetwork_ { 

    public static volatile SingularAttribute<Subnetwork, Integer> id;
    public static volatile CollectionAttribute<Subnetwork, SocialProfile> socialProfileCollection;
    public static volatile SingularAttribute<Subnetwork, String> description;

}