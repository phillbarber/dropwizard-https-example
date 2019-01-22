#!/bin/bash
set -e;

KEYS_DIR="target/keys/remote-server-alternative"
PRIVATE_KEY="$KEYS_DIR/1-private-key.pem"
CERTIFICATE="$KEYS_DIR/2-certificate.crt"
PKCS_KEY_STORE="$KEYS_DIR/3-keystore.pkcs12"
ALIAS="some-alias"
PASSWORD="abcdefg"
#PUBLIC_KEY="$KEYS_DIR/public-key.pem"

#Function that executes a command and prints it first - Thanks to Soth: https://stackoverflow.com/users/182619/soth, https://stackoverflow.com/questions/2853803/how-to-echo-shell-commands-as-they-are-executed
executeAndPrint() { echo "\$ $@" ; "$@" ; }

echo "Initialise directory for keys (if required)"
mkdir -p ${KEYS_DIR}
rm ${KEYS_DIR}/* || true


#Thanks to this page for commands:
# https://www.eclipse.org/jetty/documentation/current/configuring-ssl.html
# https://www.eclipse.org/jetty/documentation/current/configuring-ssl.html#loading-keys-and-certificates

executeAndPrint openssl genrsa -passout pass:${PASSWORD} -aes128 -out ${PRIVATE_KEY}

executeAndPrint openssl req -passin pass:${PASSWORD} -config certificate-info.oid -new -x509 -newkey rsa:2048 -sha256 -key ${PRIVATE_KEY} -out ${CERTIFICATE}

executeAndPrint openssl pkcs12 -passin pass:${PASSWORD} -passout pass:${PASSWORD} -inkey ${PRIVATE_KEY} -in ${CERTIFICATE} -export -out ${PKCS_KEY_STORE}