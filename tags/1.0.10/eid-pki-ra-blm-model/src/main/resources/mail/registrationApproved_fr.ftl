text/html
===
pki-ra@fedict.be
===
Votre enregistrement pour ${certificateDomain.name} a &eacute;t&eacute; approuv&eacute;
===
<p>Cher, Ch&egrave;re ${user.name},</p>

<p>
Vous vous &ecirc;tes r&eacute;cemment enregistr&eacute;(e) pour utiliser le domaine de certification ${certificateDomain.name}. 
Cette demande a &eacute;t&eacute; approuv&eacute;e par nos administrateurs.
Vous pouvez commencer &agrave; utiliser ce domaine d&egrave;s &agrave; pr&eacute;sent.
</p>

<#if reason?has_content>
<p>Raison: ${reason}.</p>
</#if>

<p>
Avec nos salutations les meilleures,<br/>
Fedict.
</p>

<p>
Consultez le site eID PKI RAA &agrave; l'adresse <a href="${configuration.HOMEPAGE_URL}">${configuration.HOMEPAGE_URL}</a>.
</p>
