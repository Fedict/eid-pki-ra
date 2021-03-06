text/html
===
pki-ra@fedict.be
===
Your certificate for "${certificate.distinguishedName}" will expire
===
<p>Dear,</p>

<p>
The certificate with subject "${certificate.distinguishedName}" and serial number ${certificate.serialNumber} will expire on ${certificate.validityEnd?string("dd/MM/yyyy")}.
It was requested for the certificate domain "${certificate.certificateDomain.name}".
</p>

<p>
If you want to renew your certificate, please use the Fedict eID PKI RAA application at <a href="${configuration.HOMEPAGE_URL}">${configuration.HOMEPAGE_URL}</a>.
</p>.

<p>
Best regards,<br/>
Fedict.
</p>  