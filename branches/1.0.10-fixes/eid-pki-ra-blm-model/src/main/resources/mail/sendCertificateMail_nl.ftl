text/html
===
pki-ra@fedict.be
===
Uw certificaat
===
<p>Beste ${user.name},</p>

<p>
Bijgevoegd vindt u het certificaat dat u hebt aangevraagd.
</p>

<p>Details van het certificaat:
<ul>
<li><b>Type certificaat</b>: ${certificate.certificateType}<br/></li>
<li><b>Onderwerp</b>: ${certificate.distinguishedName}<br/></li>
<li><b>Verlener</b>: ${certificate.issuer}<br/></li>
<li><b>Geldig vanaf</b>: ${certificate.validityStart?date}<br/></li>
<li><b>Geldig tot</b>: ${certificate.validityEnd?date}<br/></li>
</ul>
</p>

<p>
Met vriendelijke groeten,<br/>
Fedict
</p>

<p>
Bezoek eID PKI RAA op <a href="${configuration.HOMEPAGE_URL}">${configuration.HOMEPAGE_URL}</a>.
</p>
