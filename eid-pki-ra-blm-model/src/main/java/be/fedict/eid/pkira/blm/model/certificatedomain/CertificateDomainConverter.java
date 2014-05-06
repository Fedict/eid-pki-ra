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

package be.fedict.eid.pkira.blm.model.certificatedomain;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Converter(forClass = CertificateDomain.class)
@Name("be.fedict.eid.pkira.blm.certificateDomainConverter")
@BypassInterceptors
@Scope(ScopeType.APPLICATION)
public class CertificateDomainConverter implements javax.faces.convert.Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) return null;

        CertificateDomainHome certificateDomainHome = (CertificateDomainHome) Component.getInstance(CertificateDomainHome.NAME, true);
        certificateDomainHome.setId(Integer.parseInt(value));
        return certificateDomainHome.getInstance();
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return null;
        return String.valueOf(((CertificateDomain) value).getId());
    }
}
