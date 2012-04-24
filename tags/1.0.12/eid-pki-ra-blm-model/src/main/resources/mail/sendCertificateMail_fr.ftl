text/html
===
pki-ra@fedict.be
===
Votre certificat
===
<p> Cher, Ch&egrave;re ${user.name},</p>

<p>
Vous trouverez en pi&egrave;ce jointe le certificat que vous avez demand&eacute.
</p>

<p>DÈtails du certificat:
<ul>
<li><b>Type de certificat</b>: ${certificate.certificateType}<br/></li>
<li><b>Objet</b>: ${certificate.distinguishedName}<br/></li>
<li><b>Emetteur</b>: ${certificate.issuer}<br/></li>
<li><b>Valable &agrave; compter du</b>: ${certificate.validityStart?date}<br/></li>
<li><b>Valable jusqu'au </b>: ${certificate.validityEnd?date}<br/></li>
</ul>
</p>

<p>Votre certificat et la cha&icirc;ne des certificats:
<#list certificateChain as certificate>
<ul>${certificate}</ul>
</#list>
</p>

<p>
Avec nos salutations les meilleures,<br/>
Fedict
</p>

<p>
Consultez le site eID PKI RAA &agrave; l'adresse <a href="${configuration.HOMEPAGE_URL}">${configuration.HOMEPAGE_URL}</a>.
</p>
