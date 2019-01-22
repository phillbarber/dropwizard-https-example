#!/bin/bash
set -e;

KEYS_DIR="target/keys/remote-server"
PRIVATE_KEY="$KEYS_DIR/1-private-key.pem"
CERTIFICATE_SIGNING_REQUEST="$KEYS_DIR/2-certificate-signing-request.csr"
PUBLIC_X509_CERT="$KEYS_DIR/3-x509-cert.pem"
PRIVATE_KEY_AND_X509_CERT="$KEYS_DIR/4-private-key-and-x509-cert.pem"
PKCS_KEY_STORE="$KEYS_DIR/5-keystore.pkcs12"
JAVA_KEY_STORE="$KEYS_DIR/6-java-keystore.jks"
PKCS_KEY_STORE_FROM_JAVA="$KEYS_DIR/7-keystore.pkcs12"
ANOTHER_JAVA_KEY_STORE="$KEYS_DIR/8-java-keystore.jks"
ALIAS="some-alias"
PASSWORD="abcdefg"
#PUBLIC_KEY="$KEYS_DIR/public-key.pem"

#Function that executes a command and prints it first - Thanks to Soth: https://stackoverflow.com/users/182619/soth, https://stackoverflow.com/questions/2853803/how-to-echo-shell-commands-as-they-are-executed
executeAndPrint() { echo "\$ $@" ; "$@" ; }

echo "Initialise directory for keys (if required)"
mkdir -p ${KEYS_DIR}
rm ${KEYS_DIR}/*


#Thanks to this page for commands: https://blog.knoldus.com/create-a-self-signed-ssl-certificate-using-openssl/
#https://www.openssl.org/docs/man1.0.2/apps/openssl.html

echo "1. Generate a 2048 bit(recommended) RSA private key"
executeAndPrint openssl genpkey -algorithm RSA -out ${PRIVATE_KEY} -pkeyopt rsa_keygen_bits:2048

echo "2. Generate a CSR(Certificate Signing Request).  The CA will use the .csr file and issue the certificate, but in our case we will use this .csr file to create a self-signed certificate"
executeAndPrint openssl req -config certificate-info.oid -new -key ${PRIVATE_KEY} -out ${CERTIFICATE_SIGNING_REQUEST}

echo "3. Create the self-signed x509 public certificate suitable for use on the web server and (potentially) clients using the server"
executeAndPrint openssl x509 -setalias ${ALIAS} -req -days 365 -in ${CERTIFICATE_SIGNING_REQUEST} -signkey ${PRIVATE_KEY} -out ${PUBLIC_X509_CERT}
#pretty sure this file is fine as the following works ok:
#openssl x509 -text -in target/keys/remote-server/3-x509-cert.pem

echo "4. Create a new file with the private key and certificate"

cat ${PRIVATE_KEY} > ${PRIVATE_KEY_AND_X509_CERT}
cat ${PUBLIC_X509_CERT} >> ${PRIVATE_KEY_AND_X509_CERT}

echo "5. Create pkc12 file"
executeAndPrint openssl pkcs12 -name ${ALIAS} -password pass:${PASSWORD} -export -in ${PRIVATE_KEY_AND_X509_CERT} -out ${PKCS_KEY_STORE}

echo "6. Create a java key store file- shouldnt be necessary!!!! - for some reason this works but above does not"
executeAndPrint keytool -importkeystore -srckeystore ${PKCS_KEY_STORE} -srcstoretype pkcs12 -srcalias ${ALIAS} -destkeystore ${JAVA_KEY_STORE} -deststoretype jks -srcstorepass ${PASSWORD} -deststorepass ${PASSWORD} -destalias ${ALIAS}

echo "7. Crazy but will try - create pkcs12 from keystore file"
executeAndPrint keytool -importkeystore -srckeystore ${JAVA_KEY_STORE} -destkeystore ${PKCS_KEY_STORE_FROM_JAVA} -srcstoretype JKS -deststoretype PKCS12 -srcstorepass ${PASSWORD} -srcalias ${ALIAS} -deststorepass ${PASSWORD} -destalias ${ALIAS}

echo "8. Create ANOTHER java key store file- shouldnt be necessary!!!! - for some reason this ALSO works but above does not"
executeAndPrint keytool -importkeystore -srckeystore ${PKCS_KEY_STORE_FROM_JAVA} -srcstoretype pkcs12 -srcalias ${ALIAS} -destkeystore ${ANOTHER_JAVA_KEY_STORE} -deststoretype jks -srcstorepass ${PASSWORD} -deststorepass ${PASSWORD} -destalias ${ALIAS}
