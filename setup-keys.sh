#!/bin/bash
set -e;

KEYS_DIR="target/keys/remote-server"
PRIVATE_KEY="$KEYS_DIR/1-private-key.pem"
CERTIFICATE_SIGNING_REQUEST="$KEYS_DIR/2-certificate-signing-request.csr"
X509_CERT="$KEYS_DIR/3-x509-cert.pem"
PRIVATE_KEY_AND_X509_CERT="$KEYS_DIR/4-private-key-and-x509-cert.pem"
PKCS_KEY_STORE="$KEYS_DIR/5-keystore.pkcs12"
#PUBLIC_KEY="$KEYS_DIR/public-key.pem"

#Function that executes a command and prints it first - Thanks to Soth: https://stackoverflow.com/users/182619/soth, https://stackoverflow.com/questions/2853803/how-to-echo-shell-commands-as-they-are-executed
executeAndPrint() { echo "\$ $@" ; "$@" ; }

echo "Initialise directory for keys (if required)"
mkdir -p ${KEYS_DIR}


#Thanks to this page for commands: https://blog.knoldus.com/create-a-self-signed-ssl-certificate-using-openssl/
#https://www.openssl.org/docs/man1.0.2/apps/openssl.html

echo "1. Generate a 2048 bit(recommended) RSA private key"
executeAndPrint openssl genpkey -algorithm RSA -out ${PRIVATE_KEY} -pkeyopt rsa_keygen_bits:2048

echo "2. Generate a CSR(Certificate Signing Request).  The CA will use the .csr file and issue the certificate, but in our case we will use this .csr file to create a self-signed certificate"
executeAndPrint openssl req -config certificate-info.oid -new -key ${PRIVATE_KEY} -out ${CERTIFICATE_SIGNING_REQUEST}

echo "3. Create the self-signed x509 certificate suitable for use on the web server"
executeAndPrint openssl x509 -req -days 365 -in ${CERTIFICATE_SIGNING_REQUEST} -signkey ${PRIVATE_KEY} -out ${X509_CERT}

echo "4. Create a new file with the private key and certificate"

cat ${PRIVATE_KEY} > ${PRIVATE_KEY_AND_X509_CERT}
cat ${X509_CERT} >> ${PRIVATE_KEY_AND_X509_CERT}

echo "5. Create pkc12 file"
executeAndPrint openssl pkcs12 -password pass:abcdefg -export -in ${PRIVATE_KEY_AND_X509_CERT} -out ${PKCS_KEY_STORE}