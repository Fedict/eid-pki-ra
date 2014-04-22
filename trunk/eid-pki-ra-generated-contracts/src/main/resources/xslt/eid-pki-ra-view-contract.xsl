<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:contract="urn:be:fedict:eid:pkira:contracts" xmlns:dsig="http://www.w3.org/2000/09/xmldsig#">
    <xsl:output method="html" omit-xml-declaration="yes" indent="no" encoding="UTF-8" doctype-public="-//W3C//DTD HTML 4.0//EN"/>

    <xsl:template match="/">
        <html>
            <head>
                <style>
                    .key { font-weight: bold; }
                </style>
                <xsl:apply-templates mode="head" />
            </head>
            <body>
                <xsl:apply-templates mode="body"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template mode="head" match="/contract:CertificateSigningRequest">
        <title>Certificate Signing Request</title>
    </xsl:template>

    <xsl:template mode="head" match="/contract:CertificateRevocationRequest">
        <title>Certificate Revocation Request</title>
    </xsl:template>

    <xsl:template mode="body" match="/contract:CertificateSigningRequest">
        <h1>Certificate Signing Request</h1>

        <h2>CSR</h2>
        <pre class="value"><xsl:value-of select="contract:CSR" /></pre>

        <h2>Extra information</h2>
        <p>
            <span class="key">Request ID:</span>
            <span class="value"><xsl:value-of select="contract:RequestId" /></span>
        </p>
        <p>
            <span class="key">Subject: </span>
            <span class="value"><xsl:value-of select="contract:DistinguishedName" /></span>
        </p>
        <p>
            <span class="key">Subject Alternative Names: </span>
            <span class="value">
                <xsl:for-each select="contract:AlternativeName">
                    <xsl:value-of select="."/>
                    <xsl:if test="position() != last()">
                        <xsl:text> / </xsl:text>
                    </xsl:if>
                </xsl:for-each>
            </span>
        </p>
        <p>
            <span class="key">Certificate type: </span>
            <span class="value"><xsl:value-of select="contract:CertificateType" /></span>
        </p>
        <p>
            <span class="key">Validity period: </span>
            <span class="value"><xsl:value-of select="contract:ValidityPeriodMonths" /> months</span>
        </p>
        <p>
            <span class="key">Description: </span>
            <span class="value"><xsl:value-of select="contract:Description" /></span>
        </p>

        <xsl:apply-templates select="contract:Operator" />
        <xsl:apply-templates select="contract:LegalNotice" />
        <xsl:apply-templates select="dsig:Signature" />
    </xsl:template>

    <xsl:template mode="body" match="/contract:CertificateRevocationRequest">
        <h1>Certificate Revocation Request</h1>

        <h2>Certificate</h2>
        <pre class="value"><xsl:value-of select="contract:Certificate" /></pre>

        <h2>Extra information</h2>
        <p>
            <span class="key">Request ID:</span>
            <span class="value"><xsl:value-of select="contract:RequestId" /></span>
        </p>
        <p>
            <span class="key">Subject: </span>
            <span class="value"><xsl:value-of select="contract:DistinguishedName" /></span>
        </p>
        <p>
            <span class="key">Validity: </span>
            <span class="value"><xsl:value-of select="contract:ValidityStart" /> - <xsl:value-of select="contract:ValidityEnd" /></span>
        </p>
        <p>
            <span class="key">Description: </span>
            <span class="value"><xsl:value-of select="contract:Description" /></span>
        </p>

        <xsl:apply-templates select="contract:Operator" />
        <xsl:apply-templates select="contract:LegalNotice" />
        <xsl:apply-templates select="dsig:Signature" />
    </xsl:template>

    <xsl:template match="contract:LegalNotice">
        <h2>Legal notice</h2>
        <p>
            <span class="value"><xsl:value-of select="." /></span>
        </p>
    </xsl:template>

    <xsl:template match="contract:Operator">
        <h2>Operator</h2>
        <p>
            <span class="key">Name: </span>
            <span class="value"><xsl:value-of select="contract:Name" /></span>
        </p>
        <p>
            <span class="key">Function: </span>
            <span class="value"><xsl:value-of select="contract:Function" /></span>
        </p>
        <p>
            <span class="key">Telephone: </span>
            <span class="value"><xsl:value-of select="contract:Phone" /></span>
        </p>
    </xsl:template>

    <xsl:template match="dsig:Signature">
        <h2>Signature</h2>
        <p>
            <span class="key">Signed with certificate: </span>
            <pre class="value"><xsl:value-of select="dsig:KeyInfo/dsig:X509Data/dsig:X509Certificate" /></pre>
        </p>
    </xsl:template>
</xsl:stylesheet>