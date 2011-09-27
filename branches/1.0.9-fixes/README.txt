README for FedICT eID PKI RA Project
====================================

=== 1. Introduction

This project contains the source code tree of the FedICT eID PKI RA.
The source code is hosted at: http://code.google.com/p/eid-pki-ra/


=== 2. Requirements

The following is required for compiling the eID PKI RA software:
* Sun Java 1.6.0_18
* Apache Maven 2.0.10 or 2.2.1

When sitting behind an HTTP proxy and you experience weird download behaviour,
you might want to consider using Apache Maven 2.0.10.


=== 3. Build

The project can be build via:
	mvn clean install

-- TODO

You can speed up the development build cycle by skipping the unit tests via:
	mvn -Dmaven.test.skip=true clean install


=== 4. SDK Release

An SDK build can be performed via:
	
-- TODO


=== 5. Eclipse IDE

-- TODO


=== 6. NetBeans IDE

As of NetBeans version 6.7 this free IDE from Sun has native Maven 2 support.


=== 7. License

The source code of the eID PKI RA Project is licensed under GNU LGPL v3.0.
The source code files remain under control of the GNU LGPL v3.0 license 
unless otherwise decided in the future by _ALL_ eID PKI RA Project 
copyright holders.
The license conditions can be found in the file: LICENSE.txt

