#!/bin/bash
set -e;

KEYS_DIR="target/keys/remote-server"
PRIVATE_KEY="$KEYS_DIR/private-key.pem"
PUBLIC_KEY="$KEYS_DIR/public-key.pem"

#Function that executes a command and prints it first - Thanks to Soth: https://stackoverflow.com/users/182619/soth, https://stackoverflow.com/questions/2853803/how-to-echo-shell-commands-as-they-are-executed
executeAndPrint() { echo "\$ $@" ; "$@" ; }

echo "Initialise directory for keys (if required)"
mkdir -p ${KEYS_DIR}

#Thanks to this page for commands: https://en.wikibooks.org/wiki/Cryptography/Generate_a_keypair_using_OpenSSL

executeAndPrint openssl genpkey -pass pass:abcdefg -algorithm RSA -out ${PRIVATE_KEY} -pkeyopt rsa_keygen_bits:2048

executeAndPrint openssl rsa -pubout -in ${PRIVATE_KEY} -out ${PUBLIC_KEY}