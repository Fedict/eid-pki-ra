text/html
===
pki-ra@fedict.be
===
Votre certificat pour "${certificate.distinguishedName}" arrive &agrave; &eacute;ch&eacute;ance
===
<p>Cher, Ch&egrave;re</p>

<p>
Le certificat li&eacute; &agrave; "${certificate.distinguishedName}" et au num&eacute;ro de s&eacute;rie ${certificate.serialNumber} expirera le ${certificate.validityEnd?string("dd/MM/yyyy")}.
Il avait &eacute;t&eacute; demand&eacute; pour le domaine de certification "${certificate.certificateDomain.name}".
</p>

<p>
Si vous souhaitez renouveler votre certificat, veuillez utiliser l'application Fedict eID PKI RAA &agrave; l'adresse <a href="${configuration.HOMEPAGE_URL}">${configuration.HOMEPAGE_URL}</a>.
</p>.

<p>
Avec nos salutations les meilleures,<br/>
Fedict.
</p>  
