/*
 * eID PKI RA Project.
 * Copyright (C) 2010 FedICT.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version
 * 3.0 as published by the Free Software Foundation.
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, see
 * http://www.gnu.org/licenses/.
 */
package be.fedict.eid.pkira.blm.model.domain;


/**
 * The types of certificate.
 * 
 * @author Jan Van den Bergh
 */
public enum CertificateType {

	SERVER("Server"),
    CLIENT("Client"),
    CODE("Code");
    
	private final String value;

    private CertificateType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}