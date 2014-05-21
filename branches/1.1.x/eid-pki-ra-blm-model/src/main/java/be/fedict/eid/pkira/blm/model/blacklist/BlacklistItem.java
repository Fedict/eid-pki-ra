/*
 * eID PKI RA Project.
 * Copyright (C) 2010-2014 FedICT.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */

package be.fedict.eid.pkira.blm.model.blacklist;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Index;
import org.hibernate.validator.NotEmpty;

import be.fedict.eid.pkira.blm.model.certificatedomain.CertificateDomain;
import be.fedict.eid.pkira.blm.model.usermgmt.User;

@Entity
@Table(name = "BLACKLIST_ITEM")
@NamedQueries({
        @NamedQuery(
                name="FIND_GLOBAL_OR_BYUSER_OR_BYCERTIFICATEDOMAIN",
                query="SELECT item FROM BlacklistItem item WHERE item.global=true OR :user MEMBER OF item.users OR :certificateDomain MEMBER OF item.certificateDomains")
})
public class BlacklistItem {

    @Id
    @GeneratedValue
    @Column(name = "BLACKLIST_ITEM_ID")
    private Integer id;

    @Column(name = "BLOCKED_CN", nullable = false)
    @NotEmpty(message = "{validation.empty.commonName}")
    private String blockedCN;

    @Column(name="GLOBAL", nullable=false)
    @Index(name="idxGlobal")
    private boolean global = false;

    @JoinTable(name="CERTIFICATE_DOMAIN_BLACKLIST",
            joinColumns = @JoinColumn(name="BLACKLIST_ITEM_ID", referencedColumnName="BLACKLIST_ITEM_ID"),
            inverseJoinColumns = @JoinColumn(name="CERTIFICATE_DOMAIN_ID", referencedColumnName="CERTIFICATE_DOMAIN_ID")
    )
    @ManyToMany
    private List<CertificateDomain> certificateDomains;

    @JoinTable(name="USER_BLACKLIST",
            joinColumns = @JoinColumn(name="BLACKLIST_ITEM_ID", referencedColumnName="BLACKLIST_ITEM_ID"),
            inverseJoinColumns = @JoinColumn(name="USER_ID", referencedColumnName="USER_ID")
    )
    @ManyToMany
    private List<User> users;

    public Integer getId() {
        return id;
    }

    public String getBlockedCN() {
        return blockedCN;
    }

    public void setBlockedCN(String blockedCN) {
        this.blockedCN = blockedCN;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public List<CertificateDomain> getCertificateDomains() {
        return certificateDomains;
    }

    public void setCertificateDomains(List<CertificateDomain> certificateDomains) {
        this.certificateDomains = certificateDomains;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
